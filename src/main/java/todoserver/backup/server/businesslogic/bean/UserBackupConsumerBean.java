
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
import todoserver.backup.server.businesslogic.bean.UserBackupManagerBean.BackupEvent ;
import todoserver.backup.server.dal.BackupProgressDao ;
import todoserver.backup.server.dal.BackupStatusDao ;
import todoserver.backup.server.dal.TodoDao ;
import todoserver.backup.server.data.BackupProgressData ;
import todoserver.backup.server.data.TodoData ;

/**
 * Performs backup of particular user for concrete backup
 * 
 * @author vk
 */
@Component
@Qualifier ( "userBackupConsumer" )
public class UserBackupConsumerBean implements Consumer < BackupEvent >
{
    // auto-wired components
    @Autowired
    private TodoDao todoDao ;

    @Autowired
    private BackupStatusDao backupStatusDao ;

    @Autowired
    private BackupProgressDao backupProgressDao ;

    @Autowired
    private ExportManager exportManager ;

    // internal state
    private Logger log = LoggerFactory.getLogger ( getClass () ) ;

    /**
     * Process items retrieved from the queue
     */
    @Override
    public void accept ( BackupEvent event )
    {
        try
        {
            // check if backup is still needed
            boolean failed = isFailed ( event.backupId ) ;
            if ( failed )
            {
                return ;
            }

            // get user data
            Iterator < TodoData > todos = todoDao.findAll ( event.user.getUserId () ) ;

            // backup user data
            while ( todos.hasNext () )
            {
                exportManager.add ( event.backupId, event.user, todos.next () ) ;
            }
        }
        catch ( BackupServerException e )
        {
            log.error ( "Was unable to backup user data for event = " + event, e ) ;
            setFailed ( event.backupId ) ;
        }
        finally
        {
            updateProgressSilently ( event ) ;
        }
    }

    private void cleanupBackup ( Long backupId ) throws BackupServerException
    {
        backupProgressDao.remove ( backupId ) ;
        exportManager.finish ( backupId ) ;
    }

    private boolean isFailed ( Long backupId ) throws BackupServerException
    {
        BackupState state = backupStatusDao.getState ( backupId ) ;
        return (BackupState.FAILED == state) ;
    }

    private void setFailed ( Long backupId )
    {
        try
        {
            backupStatusDao.updateState ( backupId, BackupState.FAILED ) ;
        }
        catch ( BackupServerException e )
        {
            log.error ( "Was unable to update backup status for backup id = " + backupId, e ) ;
        }
    }

    private void updateProgress ( Long backupId ) throws BackupServerException
    {
        // get state
        BackupState state = backupStatusDao.getState ( backupId ) ;

        // handle failed state
        if ( BackupState.FAILED == state )
        {
            cleanupBackup ( backupId ) ;
            return ;
        }

        // get current backup progress
        BackupProgressData progress = backupProgressDao.get ( backupId ) ;
        if ( null == progress )
        {
            return ;
        }

        /*
         * handle normal case 
         */

        // update backup progress
        progress.getProcessedUsers ().incrementAndGet () ;

        // if backup is finished (according to its progress)
        // its state should be updated accordingly
        if ( progress.isFinished () )
        {
            cleanupBackup ( backupId ) ;
            backupStatusDao.updateState ( backupId, BackupState.OK ) ;
        }
    }

    private void updateProgressSilently ( BackupEvent event )
    {
        try
        {
            updateProgress ( event.backupId ) ;
        }
        catch ( BackupServerException e )
        {
            log.error ( "Was unable to update backup proggress for event = " + event, e ) ;
        }
    }
}
