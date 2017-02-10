
package todoserver.backup.server.api.data ;


import java.util.Date ;

import com.fasterxml.jackson.annotation.JsonProperty ;

/**
 * Data object represents status of the backup
 * 
 * @author vk
 */
public class BackupStatusData extends BackupIdData
{
    // internal state
    @JsonProperty
    private Date dateRequested ;

    @JsonProperty
    private Date dateStarted ;

    @JsonProperty
    private Date dateFinished ;

    @JsonProperty ( "status" )
    private BackupState state ;

    public Date getDateFinished ()
    {
        return dateFinished ;
    }

    public Date getDateRequested ()
    {
        return dateRequested ;
    }

    public Date getDateStarted ()
    {
        return dateStarted ;
    }

    public BackupState getState ()
    {
        return state ;
    }

    public void setDateFinished ( Date dateFinished )
    {
        this.dateFinished = dateFinished ;
    }

    public void setDateRequested ( Date dateRequested )
    {
        this.dateRequested = dateRequested ;
    }

    public void setDateStarted ( Date dateStarted )
    {
        this.dateStarted = dateStarted ;
    }

    public void setState ( BackupState state )
    {
        this.state = state ;
    }

}
