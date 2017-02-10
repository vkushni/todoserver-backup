
package todoserver.backup.server.dal.temporary ;


import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.util.Map ;
import java.util.concurrent.ConcurrentHashMap ;

import org.apache.tomcat.util.http.fileupload.IOUtils ;
import org.springframework.stereotype.Service ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.dal.ExportDao ;
import todoserver.backup.server.util.FileUtil ;

/**
 * In memory implementation of data access object for backup data
 * 
 * @author vk
 */
@Service
public class InMemoryExportDaoBean implements ExportDao
{
    // internal state
    private final Map < Long, OutputStream > streams = new ConcurrentHashMap <> ( 100 ) ;
    private final Map < Long, File > files = new ConcurrentHashMap <> ( 100 ) ;

    private File createExportFile ( Long backupId ) throws IOException
    {
        return FileUtil.createTemporaryFile ( "backup" + backupId ) ;
    }

    @Override
    public void deleteFile ( Long backupId ) throws BackupServerException
    {
        File file = files.remove ( backupId ) ;
        file.deleteOnExit () ;
    }

    @Override
    public File getFile ( Long backupId ) throws BackupServerException
    {
        return files.get ( backupId ) ;
    }

    @Override
    public OutputStream getStream ( Long backupId ) throws BackupServerException
    {
        return streams.get ( backupId ) ;
    }

    @Override
    @SuppressWarnings ( "resource" )
    public void insert ( Long backupId ) throws BackupServerException
    {
        try
        {
            File file = createExportFile ( backupId ) ;
            files.put ( backupId, file ) ;

            BufferedOutputStream stream = new BufferedOutputStream ( new FileOutputStream ( file ) ) ;
            streams.put ( backupId, stream ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Unable create storage for backup id = " + backupId, e ) ;
        }
    }

    @Override
    @SuppressWarnings ( "resource" )
    public void removeStream ( Long backupId ) throws BackupServerException
    {
        OutputStream stream = streams.remove ( backupId ) ;
        IOUtils.closeQuietly ( stream ) ;
    }

}
