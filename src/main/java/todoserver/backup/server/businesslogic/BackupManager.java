
package todoserver.backup.server.businesslogic ;


import java.util.List ;

import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provide contact for managing backups
 * 
 * @author vk
 */
public interface BackupManager
{
    /**
     * Get amount of backups to process
     * 
     * @return
     */
    int getBackupCount () ;

    /**
     * Check backup is allowed
     * 
     * @return
     * @throws BackupServerException
     */
    boolean isAllowed () throws BackupServerException ;

    /**
     * List all backups
     * 
     * @return
     * @throws BackupServerException
     */
    List < BackupStatusData > listStatuses () throws BackupServerException ;

    /**
     * Schedule backup of all accounts, including all todo's for each account
     * 
     * @return
     * @throws BackupServerException
     */
    Long schedule () throws BackupServerException ;
}
