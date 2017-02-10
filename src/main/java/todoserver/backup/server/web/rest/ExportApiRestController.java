
package todoserver.backup.server.web.rest ;


import java.io.File ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.core.io.FileSystemResource ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RestController ;

import todoserver.backup.server.api.ExportApi ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.ExportManager ;

/**
 * Rest implementation of export API
 * 
 * @author vk
 */
@RestController
public class ExportApiRestController implements ExportApi
{
    // auto-wired components
    @Autowired
    private ExportManager exportBackupManager ;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping ( value = "/exports/{backupId}", method = RequestMethod.GET, produces = "text/csv" )
    public FileSystemResource exportBackup ( @PathVariable Long backupId ) throws BackupServerException
    {
        File file = exportBackupManager.get ( backupId ) ;
        return new FileSystemResource ( file ) ;
    }

}
