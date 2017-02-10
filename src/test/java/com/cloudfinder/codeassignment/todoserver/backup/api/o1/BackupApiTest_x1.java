
package com.cloudfinder.codeassignment.todoserver.backup.api.o1 ;


import java.util.List ;

import org.junit.After ;
import org.junit.Assert ;
import org.junit.Before ;
import org.junit.Test ;

import com.cloudfinder.codeassignment.todoserver.backup.api.BaseTest ;

import todoserver.backup.server.api.data.BackupIdData ;
import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Backup API integration tests - supposed to check service functionality by processing small amounts of data
 * 
 * @author vk
 */
public class BackupApiTest_x1 extends BaseTest
{
    /**
     * When there is no user data or amount is small backup should have status in 'OK' state
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test ( timeout = 3000 )
    public void backupIsOk () throws BackupServerException, InterruptedException
    {
        // add test backup
        BackupIdData backup = backupApi.scheduleBackup () ;
        Assert.assertNotNull ( backup ) ;

        Long backupId = backup.getBackupId () ;
        Assert.assertNotNull ( backupId ) ;

        waitFinish ( backupId ) ;

        BackupStatusData backupStatus = getBackupStatus ( backupId ) ;

        Assert.assertEquals ( BackupState.OK, backupStatus.getState () ) ;
    }

    /**
     * Backup status entry should be present in backup status list
     * 
     * @throws BackupServerException
     */
    @Test
    public void backupStatusExists () throws BackupServerException
    {
        // check initial backups amount
        List < BackupStatusData > initial = backupApi.listBackups () ;
        Assert.assertNotNull ( initial ) ;

        int initialSize = initial.size () ;

        // add test backup
        BackupIdData backup = backupApi.scheduleBackup () ;
        Assert.assertNotNull ( backup ) ;

        Long backupId = backup.getBackupId () ;
        Assert.assertNotNull ( backupId ) ;

        List < BackupStatusData > updated = backupApi.listBackups () ;
        Assert.assertNotNull ( updated ) ;
        Assert.assertEquals ( initialSize + 1, updated.size () ) ;

        BackupStatusData existingStatus = find ( updated, backupId ) ;
        Assert.assertNotNull ( existingStatus ) ;
    }

    @Before
    public void onStart () throws BackupServerException
    {
        // initialize
        super.onStart () ;

        // remove all users before test
        userApi.removeUsers () ;

        // create users
        loadUsers ( 15, 10, 10, 15 ) ;
    }

    @After
    @Override
    public void onStop () throws BackupServerException
    {
        super.onStop () ;
    }
}
