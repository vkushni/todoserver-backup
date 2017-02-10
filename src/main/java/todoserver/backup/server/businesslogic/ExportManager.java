
package todoserver.backup.server.businesslogic ;


import java.io.File ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.TodoData ;
import todoserver.backup.server.data.UserData ;

/**
 * Provides contract for managing data extracted during backup process
 * 
 * @author vk
 */
public interface ExportManager
{
    /**
     * Backup data of concrete provided todo
     * 
     * @param backupId
     *            - current backup id
     * @param user
     *            - current user data
     * @param todo
     *            - concrete todo item
     * @throws BackupServerException
     */
    void add ( Long backupId, UserData user, TodoData todo ) throws BackupServerException ;

    /**
     * Close temporary file for backup
     * 
     * @param backupId
     * @throws BackupServerException
     */
    void finish ( Long backupId ) throws BackupServerException ;

    /**
     * Get data container for concrete backup
     * 
     * @param backupId
     *            - current backup id
     * @return
     * @throws BackupServerException
     */
    File get ( Long backupId ) throws BackupServerException ;

    /**
     * Initialize backup storage for specified backup
     * 
     * @param backupId
     *            - current backup id
     * @throws BackupServerException
     */
    void initalize ( Long backupId ) throws BackupServerException ;

}
