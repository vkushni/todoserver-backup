
package com.cloudfinder.codeassignment.todoserver.backup.api.rest ;


import java.text.MessageFormat ;
import java.util.List ;

import org.springframework.web.client.RestTemplate ;

import com.fasterxml.jackson.core.type.TypeReference ;

import todoserver.backup.server.api.BackupApi ;
import todoserver.backup.server.api.data.BackupIdData ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.util.JsonUtil ;

public class BackupApiRestClient implements BackupApi
{
    // define constants
    private static final String BACKUP_RESOURCE = "http://{0}/backups" ;
    private static final TypeReference < List < BackupStatusData > > TYPE_REF_BACKUP_STATUS_DATA = new TypeReference < List < BackupStatusData > > ()
    {
    } ;

    // internal state
    private final RestTemplate restTemplate ;
    private final String server ;

    public BackupApiRestClient ( RestTemplate restTemplate, String server )
    {
        this.restTemplate = restTemplate ;
        this.server = server ;
    }

    @Override
    public BackupIdData scheduleBackup () throws BackupServerException
    {
        String url = MessageFormat.format ( BACKUP_RESOURCE, server ) ;
        try
        {
            String json = restTemplate.postForObject ( url, "", String.class ) ;

            return JsonUtil.fromJson ( BackupIdData.class, json ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't schedule backup for url = " + url, e ) ;
        }
    }

    @Override
    public List < BackupStatusData > listBackups () throws BackupServerException
    {
        try
        {
            String url = MessageFormat.format ( BACKUP_RESOURCE, server ) ;
            String json = restTemplate.getForObject ( url, String.class ) ;

            return JsonUtil.fromJson ( TYPE_REF_BACKUP_STATUS_DATA, json ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't list backups", e ) ;
        }
    }

}
