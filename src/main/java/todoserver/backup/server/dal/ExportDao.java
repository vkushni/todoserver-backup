
package todoserver.backup.server.dal ;


import java.io.File ;
import java.io.OutputStream ;

import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provides convenient methods to access data
 * 
 * @author vk
 */
public interface ExportDao
{
    /**
     * Get record for specified backup (get for append)
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    OutputStream getStream ( Long backupId ) throws BackupServerException ;

    /**
     * Create record for specified backup
     * 
     * @param backupId
     * @throws BackupServerException
     */
    void insert ( Long backupId ) throws BackupServerException ;

    /**
     * Remove record for specified backup
     * 
     * @param backupId
     */
    void removeStream ( Long backupId ) throws BackupServerException ;

    /**
     * Delete backup file
     * 
     * @param backupId
     * @throws BackupServerException
     */
    void deleteFile ( Long backupId ) throws BackupServerException ;

    /**
     * Get export file
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    File getFile ( Long backupId ) throws BackupServerException ;
}
