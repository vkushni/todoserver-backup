
package com.cloudfinder.codeassignment.todoserver.backup.api.o3 ;


import org.junit.After ;
import org.junit.Before ;
import org.junit.Test ;

import com.cloudfinder.codeassignment.todoserver.backup.api.BaseTest ;

import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Backup API integration tests - supposed to check service functionality by processing data for 300 000 users very
 * often
 * 
 * @author vk
 */
public class BackupApiLoadTest_x100k extends BaseTest
{
    /**
     * Backup server should be able process 100 backups simultaneosly (depends on actual server configuration)
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test
    public void backupServerShouldStayAliveWithScheduledBackups_x100 () throws BackupServerException, InterruptedException
    {
        Long[] backupIds = scheduleBackups ( 100 ) ;
        waitFinish ( backupIds ) ;
    }

    @Before
    public void onStart () throws BackupServerException
    {
        // initialize
        super.onStart () ;

        // remove all users before test
        userApi.removeUsers () ;

        // create users
        loadUsers ( 100 * 1000, 10, 10, 100 ) ;
    }

    @After
    @Override
    public void onStop () throws BackupServerException
    {
        super.onStop () ;
    }
}
