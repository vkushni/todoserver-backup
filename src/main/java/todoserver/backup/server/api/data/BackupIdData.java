
package todoserver.backup.server.api.data ;


import com.fasterxml.jackson.annotation.JsonProperty ;

import todoserver.backup.server.util.JsonUtil ;

/**
 * Container for backup id
 * 
 * @author vk
 */
public class BackupIdData
{
    @JsonProperty
    private Long backupId ;

    public Long getBackupId ()
    {
        return backupId ;
    }

    public void setBackupId ( Long backupId )
    {
        this.backupId = backupId ;
    }

    @Override
    public String toString ()
    {
        return JsonUtil.toJson ( this, super.toString () ) ;
    }
}
