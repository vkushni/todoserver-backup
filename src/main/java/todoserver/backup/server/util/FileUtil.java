
package todoserver.backup.server.util ;


import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.net.URL ;
import java.net.URLConnection ;
import java.nio.channels.Channels ;
import java.nio.channels.ReadableByteChannel ;

import org.apache.tomcat.util.http.fileupload.IOUtils ;

/**
 * Utility methods for working with files
 * 
 * @author vk
 *
 */
public class FileUtil
{
    /**
     * Creates temporary file
     * 
     * @return
     * @throws IOException
     */
    public static File createTemporaryFile () throws IOException
    {
        return createTemporaryFile ( "bkp" ) ;
    }

    /**
     * Creates temporary file with prefix based on specified parameters
     * 
     * @param prefix
     * @return
     * @throws IOException
     */
    public static File createTemporaryFile ( String prefix ) throws IOException
    {
        // create temp file
        File file = File.createTempFile ( prefix, null ) ;
        if ( file.exists () )
        {
            // delete if such already exists
            file.delete () ;
        }

        // mark for deletion
        file.deleteOnExit () ;

        return file ;
    }

    /**
     * Save file from specified url to file
     * 
     * @param url
     *            - url to retrieve file from
     * @param to
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveAs ( URL url, File to ) throws IOException
    {
        // define resources
        InputStream stream = null ;
        ReadableByteChannel source = null ;
        FileOutputStream destination = null ;

        try
        {
            // tune connection
            URLConnection connection = url.openConnection () ;
            connection.setConnectTimeout ( 0 ) ;
            connection.setReadTimeout ( 0 ) ;

            stream = connection.getInputStream () ;
            source = Channels.newChannel ( stream ) ;

            // open destination
            destination = new FileOutputStream ( to ) ;

            // transfer data
            destination.getChannel ().transferFrom ( source, 0, Long.MAX_VALUE ) ;
        }
        finally
        {
            IOUtils.closeQuietly ( source ) ;
            IOUtils.closeQuietly ( stream ) ;
            IOUtils.closeQuietly ( destination ) ;
        }
    }

}
