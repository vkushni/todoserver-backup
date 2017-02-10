
package todoserver.backup.server.data ;


import java.util.concurrent.atomic.AtomicInteger ;

import com.fasterxml.jackson.annotation.JsonIgnore ;
import com.fasterxml.jackson.annotation.JsonProperty ;

import todoserver.backup.server.util.JsonUtil ;

/**
 * Data object for keeping backup progress data
 * 
 * @author vk
 */
public class BackupProgressData
{
    // internal state
    @JsonProperty
    private Long backupId ;

    @JsonProperty
    private AtomicInteger processedUsers ;

    @JsonProperty
    private AtomicInteger totalUsers ;

    public BackupProgressData ()
    {
        totalUsers = new AtomicInteger () ;
        processedUsers = new AtomicInteger () ;
    }

    public Long getBackupId ()
    {
        return backupId ;
    }

    public AtomicInteger getProcessedUsers ()
    {
        return processedUsers ;
    }

    public AtomicInteger getTotalUsers ()
    {
        return totalUsers ;
    }

    /**
     * Check if backup progress is finished according to existing progress data
     * 
     * @return
     */
    @JsonIgnore
    public boolean isFinished ()
    {
        return totalUsers.intValue () == processedUsers.intValue () ;
    }

    public void setBackupId ( Long backupId )
    {
        this.backupId = backupId ;
    }

    public void setProcessedUsers ( AtomicInteger processedUsers )
    {
        this.processedUsers = processedUsers ;
    }

    public void setTotalUsers ( AtomicInteger totalUsers )
    {
        this.totalUsers = totalUsers ;
    }

    @Override
    public String toString ()
    {
        return JsonUtil.toJson ( this, super.toString () ) ;
    }

}
