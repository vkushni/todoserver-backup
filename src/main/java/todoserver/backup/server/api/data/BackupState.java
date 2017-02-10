
package todoserver.backup.server.api.data ;


import com.fasterxml.jackson.annotation.JsonValue ;

/**
 * Define possible backup states
 * 
 * @author vk
 */
public enum BackupState
{
    /**
     * Backup is still in progress
     */
    IN_PROGRESS ( "In progress" ),

    /**
     * Backup successfully finished
     */
    OK ( "OK" ),

    /**
     * Backup failed
     */
    FAILED ( "Failed" ),

    // placeholder
    ;

    public static BackupState valueOf ( Integer value )
    {
        return values ()[value.intValue ()] ;
    }

    private final String value ;

    private BackupState ( String value )
    {
        this.value = value ;
    }

    @JsonValue
    public String getValue ()
    {
        return value ;
    }
}
