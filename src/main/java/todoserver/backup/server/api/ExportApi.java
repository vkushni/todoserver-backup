
package todoserver.backup.server.api ;


import org.springframework.core.io.FileSystemResource ;

import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provides API for managing backup data
 * 
 * @author vk
 */
public interface ExportApi
{
    /**
     * Get backup data
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    FileSystemResource exportBackup ( Long backupId ) throws BackupServerException ;
}
