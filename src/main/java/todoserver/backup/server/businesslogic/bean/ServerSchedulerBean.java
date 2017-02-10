
package todoserver.backup.server.businesslogic.bean ;


import java.util.List ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.scheduling.annotation.Scheduled ;
import org.springframework.stereotype.Component ;

import todoserver.backup.server.businesslogic.BackupManager ;
import todoserver.backup.server.dal.BackupProgressDao ;
import todoserver.backup.server.data.BackupProgressData ;

/**
 * Schedules actions for server
 * 
 * @author vk
 */
@Component
public class ServerSchedulerBean
{
    // auto-wired components
    @Autowired
    private BackupManager backupManager ;

    @Autowired
    private BackupProgressDao backupProgressDao ;

    // internal state
    private Logger log = LoggerFactory.getLogger ( getClass () ) ;

    /**
     * Print current server status
     */
    @Scheduled ( fixedRate = 1000 )
    public void printBackupProgress ()
    {
        // print backups to be done
        int counter = backupManager.getBackupCount () ;
        if ( counter > 0 )
        {
            log.info ( "Backups not started, count = " + counter ) ;
        }

        // print backups currently in progress
        List < BackupProgressData > backups = backupProgressDao.findAll () ;
        for ( BackupProgressData progress : backups )
        {
            log.info ( "Backup is in progress, status = " + progress ) ;
        }
    }
}
