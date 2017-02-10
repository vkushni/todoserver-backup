
package todoserver.backup.server.businesslogic.bean ;


import java.util.Iterator ;
import java.util.function.Consumer ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Qualifier ;
import org.springframework.stereotype.Component ;

import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.ExportManager ;
import todoserver.backup.server.businesslogic.UserBackupManager ;
import todoserver.backup.server.dal.BackupProgressDao ;
import todoserver.backup.server.dal.BackupStatusDao ;
import todoserver.backup.server.dal.UserDao ;
import todoserver.backup.server.data.BackupProgressData ;
import todoserver.backup.server.data.UserData ;

/**
 * Performs users backup
 * 
 * @author vk
 */
@Component
@Qualifier ( "backupConsumer" )
public class BackupConsumerBean implements Consumer < Long >
{
    // auto-wired components
    @Autowired
    private UserDao userDao ;

    @Autowired
    private BackupStatusDao backupStatusDao ;

    @Autowired
    private ExportManager exportManager ;

    @Autowired
    private BackupProgressDao backupProgressDao ;

    @Autowired
    private UserBackupManager backupUserManager ;

    // internal state
    private Logger log = LoggerFactory.getLogger ( getClass () ) ;

    /**
     * Handling queue item in separate thread
     */
    @Override
    public void accept ( Long backupId )
    {
        log.info ( "Started backup process for backup id = " + backupId ) ;

        try
        {
            // update backup status
            backupStatusDao.updateState ( backupId, BackupState.IN_PROGRESS ) ;

            // list user ids
            boolean first = true ;
            Iterator < UserData > users = userDao.iterateAll () ;
            while ( users.hasNext () )
            {
                if ( first )
                {
                    // initialize backup storage
                    exportManager.initalize ( backupId ) ;

                    // update progress
                    backupProgressDao.insert ( backupId ) ;

                    // update first flag
                    first = false ;
                }

                // update progress
                BackupProgressData progress = backupProgressDao.get ( backupId ) ;
                progress.getTotalUsers ().incrementAndGet () ;

                // generate backup task
                backupUserManager.backup ( backupId, users.next () ) ;
            }

            if ( first )
            {
                setOk ( backupId ) ;
            }
        }
        catch ( BackupServerException e )
        {
            log.error ( "Loading backup failed for backup id = " + backupId, e ) ;
            setFailed ( backupId ) ;
        }

        log.info ( "Finished backup process for backup id = " + backupId ) ;
    }

    private void setFailed ( Long backupId )
    {
        try
        {
            backupStatusDao.updateState ( backupId, BackupState.FAILED ) ;
            backupProgressDao.remove ( backupId ) ;
            exportManager.finish ( backupId ) ;
        }
        catch ( BackupServerException e )
        {
            log.error ( "Unable fail backup for backup id = " + backupId, e ) ;
        }
    }

    private void setOk ( Long backupId ) throws BackupServerException
    {
        backupStatusDao.updateState ( backupId, BackupState.OK ) ;
    }

}
