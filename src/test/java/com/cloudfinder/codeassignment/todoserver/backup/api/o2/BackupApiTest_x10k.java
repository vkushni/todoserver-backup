
package com.cloudfinder.codeassignment.todoserver.backup.api.o2 ;


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
 * Backup API integration tests - supposed to check service functionality by having data for 10 000 users
 * 
 * @author vk
 */
public class BackupApiTest_x10k extends BaseTest
{
    /**
     * Right after scheduling backup should be in-progress because server will need some time to finish backup
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test
    public void backupIsInProgress () throws BackupServerException, InterruptedException
    {
        // add test backup
        BackupIdData backup = backupApi.scheduleBackup () ;
        Assert.assertNotNull ( backup ) ;

        Long backupId = backup.getBackupId () ;
        Assert.assertNotNull ( backupId ) ;

        BackupStatusData backupStatus = getBackupStatus ( backupId ) ;
        Assert.assertEquals ( BackupState.IN_PROGRESS, backupStatus.getState () ) ;
    }

    /**
     * Backup should be ready in 5 minutes (it make sense only in case there is no other backups scheduled before or
     * during this test)
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test ( timeout = INTERVAL_5_MINUTES )
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

        Assert.assertTrue ( true ) ;
    }

    @Before
    public void onStart () throws BackupServerException
    {
        // initialize
        super.onStart () ;

        // remove all users before test
        userApi.removeUsers () ;

        // create 100 users
        loadUsers ( 10000, 10, 10, 100 ) ;
    }

    @After
    @Override
    public void onStop () throws BackupServerException
    {
        super.onStop () ;
    }

}
