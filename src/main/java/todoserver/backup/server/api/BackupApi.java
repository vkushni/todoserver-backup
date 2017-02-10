
package todoserver.backup.server.api ;


import java.util.List ;

import todoserver.backup.server.api.data.BackupIdData ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provide API for managing backups
 * 
 * @author vk
 */
public interface BackupApi
{
    /**
     * List all backups
     * 
     * @return
     * @throws BackupServerException
     */
    List < BackupStatusData > listBackups () throws BackupServerException ;

    /**
     * Schedule backup of all accounts, including all todo's for each account
     * 
     * @return
     * @throws BackupServerException
     */
    BackupIdData scheduleBackup () throws BackupServerException ;
}
