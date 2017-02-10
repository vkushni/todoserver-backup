
package todoserver.backup.server.web.rest ;


import java.util.List ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.http.MediaType ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RestController ;

import todoserver.backup.server.api.BackupApi ;
import todoserver.backup.server.api.data.BackupIdData ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.BackupManager ;

/**
 * Rest implementation of backup API
 * 
 * @author vk
 */
@RestController
public class BackupApiRestController implements BackupApi
{
    // auto-wired components
    @Autowired
    private BackupManager backupManager ;

    /**
     * {@inheritDoc}
     */
    @RequestMapping ( value = "/backups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
    @Override
    public BackupIdData scheduleBackup () throws BackupServerException
    {
        // schedule backup
        Long backupId = backupManager.schedule () ;

        // create result
        BackupIdData result = new BackupIdData () ;
        result.setBackupId ( backupId ) ;
        return result ;
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping ( value = "/backups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    @Override
    public List < BackupStatusData > listBackups () throws BackupServerException
    {
        return backupManager.listStatuses () ;
    }
}
