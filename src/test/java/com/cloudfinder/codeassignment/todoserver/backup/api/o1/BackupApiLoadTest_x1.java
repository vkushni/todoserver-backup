
package com.cloudfinder.codeassignment.todoserver.backup.api.o1 ;


import org.junit.After ;
import org.junit.Before ;
import org.junit.Test ;

import com.cloudfinder.codeassignment.todoserver.backup.api.BaseTest ;

import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Backup API integration tests - supposed to check service functionality by processing small amounts of data very often
 * 
 * @author vk
 */
public class BackupApiLoadTest_x1 extends BaseTest
{
    /**
     * Backup server should be able process 100 backups simultaneosly (depends on actual server configuration)
     * 
     * @throws BackupServerException
     * @throws InterruptedException
     */
    @Test ( timeout = 5000 )
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
        loadUsers ( 100, 10, 10, 50 ) ;
    }

    @After
    @Override
    public void onStop () throws BackupServerException
    {
        super.onStop () ;
    }

}
