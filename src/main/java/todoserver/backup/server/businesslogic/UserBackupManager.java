
package todoserver.backup.server.businesslogic ;


import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.UserData ;

/**
 * Provides contract for backup of user data
 * 
 * @author vk
 */
public interface UserBackupManager
{
    /**
     * Backup user data for specified parameters
     * 
     * @param backupId
     *            - current backup id
     * @param user
     *            - detailed user data
     * @throws BackupServerException
     */
    void backup ( Long backupId, UserData user ) throws BackupServerException ;
}
