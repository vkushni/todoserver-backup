
package todoserver.backup.server.data ;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties ;
import com.fasterxml.jackson.annotation.JsonProperty ;

import todoserver.backup.server.util.JsonUtil ;

/**
 * Data object for keeping user data
 * 
 * @author vk
 */
@JsonIgnoreProperties ( { "email", "todos" } )
public class UserData
{
    // internal state
    @JsonProperty ( "id" )
    private Integer userId ;

    @JsonProperty ( "username" )
    private String userName ;

    public Integer getUserId ()
    {
        return userId ;
    }

    public String getUserName ()
    {
        return userName ;
    }

    public void setUserId ( Integer userId )
    {
        this.userId = userId ;
    }

    public void setUserName ( String userName )
    {
        this.userName = userName ;
    }

    @Override
    public String toString ()
    {
        return JsonUtil.toJson ( this, super.toString () ) ;
    }

}
