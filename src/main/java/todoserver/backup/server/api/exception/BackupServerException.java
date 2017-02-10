
package todoserver.backup.server.api.exception ;


/**
 * Generic exception produced by backup server
 * 
 * @author vk
 */
public class BackupServerException extends Exception
{
    // define constants
    private static final long serialVersionUID = -6162434009823591591L ;

    public BackupServerException ( String message )
    {
        super ( message ) ;
    }

    public BackupServerException ( String message, Throwable cause )
    {
        super ( message, cause ) ;
    }
}
