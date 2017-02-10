
package todoserver.backup.server.dal ;


import java.util.List ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.BackupProgressData ;

/**
 * Provides convenient methods to access data
 * 
 * @author vk
 */
public interface BackupProgressDao
{
    /**
     * Get current progress
     * 
     * @param backupId
     *            - backup id
     * @return
     * @throws BackupServerException
     */
    BackupProgressData get ( Long backupId ) throws BackupServerException ;

    /**
     * Add progress record for backup
     * 
     * @param backupId
     *            - backup id
     * @param total
     *            - total users to be processed
     * @throws BackupServerException
     */
    void insert ( Long backupId ) throws BackupServerException ;

    /**
     * Remove progress record
     * 
     * @param backupId
     *            - backup id
     * @throws BackupServerException
     */
    void remove ( Long backupId ) throws BackupServerException ;

    /**
     * List all records
     * 
     * @return
     */
    List < BackupProgressData > findAll () ;
}
