
package todoserver.backup.server.data ;


import java.util.Date ;

import com.fasterxml.jackson.annotation.JsonFormat ;
import com.fasterxml.jackson.annotation.JsonProperty ;

import todoserver.backup.server.util.JsonUtil ;

/**
 * Data object for keeping todo's data
 * 
 * @author vk
 */
public class TodoData
{
    // internal state
    @JsonProperty
    private Long id ;

    @JsonProperty
    private Boolean done ;

    @JsonProperty
    private String subject ;

    @JsonProperty
    @JsonFormat ( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date dueDate ;

    public Boolean getDone ()
    {
        return done ;
    }

    public Date getDueDate ()
    {
        return dueDate ;
    }

    public Long getId ()
    {
        return id ;
    }

    public String getSubject ()
    {
        return subject ;
    }

    public void setDone ( Boolean done )
    {
        this.done = done ;
    }

    public void setDueDate ( Date dueDate )
    {
        this.dueDate = dueDate ;
    }

    public void setId ( Long id )
    {
        this.id = id ;
    }

    public void setSubject ( String subject )
    {
        this.subject = subject ;
    }

    @Override
    public String toString ()
    {
        return JsonUtil.toJson ( this, super.toString () ) ;
    }
}
