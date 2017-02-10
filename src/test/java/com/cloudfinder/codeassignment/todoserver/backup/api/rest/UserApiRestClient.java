
package com.cloudfinder.codeassignment.todoserver.backup.api.rest ;


import java.io.File ;
import java.net.URL ;
import java.text.MessageFormat ;
import java.util.Iterator ;

import org.springframework.web.client.RestTemplate ;

import com.cloudfinder.codeassignment.todoserver.api.UserApi ;
import com.cloudfinder.codeassignment.todoserver.api.data.UserDataRequest ;
import com.fasterxml.jackson.databind.JsonNode ;
import com.google.common.collect.Iterators ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.TodoData ;
import todoserver.backup.server.data.UserData ;
import todoserver.backup.server.util.FileUtil ;
import todoserver.backup.server.util.JsonUtil ;
import todoserver.backup.server.util.json.JsonNodeToTypeFunction ;

public class UserApiRestClient implements UserApi
{
    // define constants
    private static final String TODO_SERVER_USER = "http://{0}/users" ;
    private static final String TODO_SERVER_USER_ID = "http://{0}/users/{1}" ;
    private static final String TODO_SERVER_USER_LOAD = "http://{0}/users/load" ;
    private static final String TODO_SERVER_TODO = "http://{0}/users/{1}/todos" ;

    // internal state
    private final RestTemplate restTemplate ;
    private final String server ;

    public UserApiRestClient ( RestTemplate restTemplate, String server )
    {
        this.restTemplate = restTemplate ;
        this.server = server ;
    }

    @Override
    public Iterator < UserData > iterateAll () throws BackupServerException
    {
        // create url
        String url = MessageFormat.format ( TODO_SERVER_USER, server ) ;

        try
        {
            // create tmp file
            File file = FileUtil.createTemporaryFile () ;

            // save json into a file
            FileUtil.saveAs ( new URL ( url ), file ) ;

            JsonNode jsonNode = JsonUtil.getMapper ().readTree ( file ) ;
            return Iterators.transform ( jsonNode.iterator (), new JsonNodeToTypeFunction <> ( UserData.class ) ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Was unable list users", e ) ;
        }
    }

    @Override
    public Iterator < TodoData > findAll ( Integer userId ) throws BackupServerException
    {
        // create url
        String url = MessageFormat.format ( TODO_SERVER_TODO, server, userId ) ;

        try
        {
            // create tmp file
            File file = FileUtil.createTemporaryFile () ;

            // save json into a file
            FileUtil.saveAs ( new URL ( url ), file ) ;

            JsonNode jsonNode = JsonUtil.getMapper ().readTree ( file ) ;
            return Iterators.transform ( jsonNode.iterator (), new JsonNodeToTypeFunction <> ( TodoData.class ) ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't find users for url = " + url, e ) ;
        }
    }

    @Override
    public void load ( UserDataRequest... requests ) throws BackupServerException
    {
        String url = MessageFormat.format ( TODO_SERVER_USER_LOAD, server ) ;
        try
        {
            restTemplate.postForObject ( url, requests, String.class ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't create user for url = " + url + ", requests = " + JsonUtil.toJson ( requests ), e ) ;
        }
    }

    @Override
    public void removeUser ( Integer userId ) throws BackupServerException
    {
        try
        {
            String url = MessageFormat.format ( TODO_SERVER_USER_ID, server, userId.toString () ) ;
            restTemplate.delete ( url ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't remove user for user id = " + userId, e ) ;
        }
    }

    @Override
    public void removeUsers () throws BackupServerException
    {
        for ( Iterator < UserData > users = iterateAll () ; users.hasNext () ; )
        {
            removeUser ( users.next ().getUserId () ) ;
        }
    }

}
