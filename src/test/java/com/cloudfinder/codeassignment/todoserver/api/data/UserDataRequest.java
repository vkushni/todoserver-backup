
package com.cloudfinder.codeassignment.todoserver.api.data ;


import java.util.List ;

import com.fasterxml.jackson.annotation.JsonProperty ;

import todoserver.backup.server.util.JsonUtil ;

/**
 * Data object for keeping todo data
 * 
 * @author vk
 */
public class UserDataRequest
{
    // internal state
    @JsonProperty ( "id" )
    private Integer userId ;

    @JsonProperty ( "username" )
    private String userName ;

    @JsonProperty
    private String email ;

    @JsonProperty
    private List < TodoSourceData > todos ;

    public String getEmail ()
    {
        return email ;
    }

    public List < TodoSourceData > getTodos ()
    {
        return todos ;
    }

    public Integer getUserId ()
    {
        return userId ;
    }

    public String getUserName ()
    {
        return userName ;
    }

    public void setEmail ( String email )
    {
        this.email = email ;
    }

    public void setTodos ( List < TodoSourceData > todos )
    {
        this.todos = todos ;
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
