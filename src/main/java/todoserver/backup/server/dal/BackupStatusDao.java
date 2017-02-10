
package todoserver.backup.server.dal ;


import java.util.Date ;
import java.util.List ;

import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provides convenient methods to access data
 * 
 * @author vk
 */
public interface BackupStatusDao
{
    /**
     * List all backups
     * 
     * @return
     * @throws BackupServerException
     */
    List < BackupStatusData > findAll () throws BackupServerException ;

    /**
     * Get backup status data
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    BackupStatusData get ( Long backupId ) throws BackupServerException ;

    /**
     * Get backup state
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    BackupState getState ( Long backupId ) throws BackupServerException ;

    /**
     * Store status for newly scheduled backup
     * 
     * @param backupId
     * @param dateRequested
     * @throws BackupServerException
     */
    void insert ( Long backupId, Date dateRequested ) throws BackupServerException ;

    /**
     * Provides next available backup id
     * 
     * @return
     * @throws BackupServerException
     */
    Long nextId () throws BackupServerException ;

    /**
     * Update backup state
     * 
     * @param backupId
     *            - backup id
     * @param state
     *            - new backup state
     * @throws BackupServerException
     */
    void updateState ( Long backupId, BackupState state ) throws BackupServerException ;
}
