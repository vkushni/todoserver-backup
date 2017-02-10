
package com.cloudfinder.codeassignment.todoserver.backup.api.o3 ;


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
 * Backup API integration tests - supposed to check service functionality by having data for 300 000 users
 * 
 * @author vk
 */
public class BackupApiTest_x100k extends BaseTest
{
    @Before
    public void onStart () throws BackupServerException
    {
        // initialize
        super.onStart () ;

        // remove all users before test
        userApi.removeUsers () ;

        // create large amount of users with random amount of tasks, range: 5-10
        loadUsers ( 100 * 1000, 10, 10, 100 ) ;
    }

    @After
    @Override
    public void onStop () throws BackupServerException
    {
        super.onStop () ;
    }

    /**
     * Since we create 100 accounts to backup, right after start backup should be in progress
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test
    public void testBackupAndExport () throws BackupServerException, InterruptedException
    {
        // add test backup
        BackupIdData backup = backupApi.scheduleBackup () ;
        Assert.assertNotNull ( backup ) ;

        Long backupId = backup.getBackupId () ;
        Assert.assertNotNull ( backupId ) ;

        // check status
        BackupStatusData afterStart = getBackupStatus ( backupId ) ;
        Assert.assertEquals ( BackupState.IN_PROGRESS, afterStart.getState () ) ;

        // wait until backup
        waitFinish ( backupId ) ;

        // check status
        BackupStatusData afterWaiting = getBackupStatus ( backupId ) ;
        Assert.assertEquals ( BackupState.OK, afterWaiting.getState () ) ;
    }

}
