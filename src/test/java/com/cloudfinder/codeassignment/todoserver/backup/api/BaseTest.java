
package com.cloudfinder.codeassignment.todoserver.backup.api ;


import java.util.ArrayList ;
import java.util.Date ;
import java.util.List ;
import java.util.concurrent.locks.Lock ;
import java.util.concurrent.locks.ReentrantLock ;

import org.junit.Assert ;
import org.springframework.web.client.RestTemplate ;

import com.cloudfinder.codeassignment.todoserver.api.UserApi ;
import com.cloudfinder.codeassignment.todoserver.api.data.TodoSourceData ;
import com.cloudfinder.codeassignment.todoserver.api.data.UserDataRequest ;
import com.cloudfinder.codeassignment.todoserver.backup.api.rest.BackupApiRestClient ;
import com.cloudfinder.codeassignment.todoserver.backup.api.rest.UserApiRestClient ;

import todoserver.backup.server.api.BackupApi ;
import todoserver.backup.server.api.data.BackupIdData ;
import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Provides some common functions for
 * 
 * @author vk
 */
public abstract class BaseTest
{
    // define constants
    protected static final long INTERVAL_5_MINUTES = 5 * 60 * 1000 ;

    // lock requried to run all test in a straight sequence
    private static final Lock sequential = new ReentrantLock () ;

    /**
     * Generate random time based number in range: [0; max)
     * 
     * @param max
     * @return
     */
    protected static int random ( int max )
    {
        return (int) Math.abs ( System.nanoTime () % max ) ;
    }

    /**
     * Generate random time based number in range [min;max)
     * 
     * @param min
     * @param max
     * @return
     */
    protected static int random ( int min, int max )
    {
        if ( min == max )
        {
            return min ;
        }

        int diff = Math.abs ( max - min ) ;
        return min + random ( diff ) ;
    }

    // internal state
    protected UserApi userApi ;
    protected BackupApi backupApi ;

    /**
     * Create todo with dummy data for provided random number
     * 
     * @param randomId
     *            - value used to make todo data unique
     * @return
     */
    private TodoSourceData createTodo ( long randomId )
    {
        TodoSourceData todo = new TodoSourceData () ;
        todo.setSubject ( "Do something for #" + randomId ) ;
        todo.setDueDate ( new Date () ) ;
        todo.setDone ( Boolean.FALSE ) ;
        return todo ;
    }

    /**
     * Create list of todo's with dummy data for provided random number
     * 
     * @param randomId
     *            - value used to make todo's data unique
     * @param amount
     *            - specify amount of tasks to create
     * @return
     */
    private List < TodoSourceData > createTodos ( long randomId, int amount )
    {
        List < TodoSourceData > result = new ArrayList <> ( amount ) ;
        for ( int i = 0 ; i < amount ; i++ )
        {
            result.add ( createTodo ( randomId ) ) ;
        }
        return result ;
    }

    /**
     * Create user data object with dummy data
     * 
     * @param randomId
     * @param todos
     * @return
     */
    private UserDataRequest createUser ( int randomId )
    {
        UserDataRequest user = new UserDataRequest () ;
        user.setEmail ( "test.mail+" + randomId + "@mailserver.com" ) ;
        user.setUserId ( Integer.valueOf ( randomId ) ) ;
        user.setUserName ( "My name for " + randomId ) ;
        return user ;
    }

    /**
     * Create user data object with dummy data
     * 
     * @param randomId
     *            - value used to make todo's data unique
     * @param tasks
     *            - amount of tasks to generate
     * 
     * @return
     */
    private UserDataRequest createUser ( int randomId, int tasks )
    {
        List < TodoSourceData > todos = createTodos ( randomId, tasks ) ;

        UserDataRequest user = createUser ( randomId ) ;
        user.setTodos ( todos ) ;
        return user ;
    }

    /**
     * Generates array of user data objects, each user is filled with random amount of todo tasks
     * 
     * @param users
     *            - amount of users to generate
     * @param minTodos
     *            - min amount of todo items per user
     * @param maxTodos
     *            - max amount of todo items per user
     * @return
     */
    private UserDataRequest[] createUsers ( int users, int minTodos, int maxTodos )
    {
        UserDataRequest[] result = new UserDataRequest[users] ;
        for ( int i = 0 ; i < result.length ; i++ )
        {
            int tasks = random ( minTodos, maxTodos ) ;
            int randomId = random ( Integer.MAX_VALUE ) ;

            result[i] = createUser ( randomId, tasks ) ;
        }
        return result ;
    }

    /**
     * Find backup within list of backups by provided backup id
     * 
     * @param backups
     * @param backupId
     * @return
     */
    protected BackupStatusData find ( List < BackupStatusData > backups, Long backupId )
    {
        return backups.stream ().filter ( entry -> entry.getBackupId ().equals ( backupId ) ).findFirst ().orElse ( null ) ;
    }

    protected String getBackupServer ()
    {
        return System.getProperty ( "backupserver", "localhost:8080" ) ;
    }

    /**
     * Get backup status from backup server by backup id
     * 
     * @param backupId
     * @return
     * @throws BackupServerException
     */
    protected BackupStatusData getBackupStatus ( Long backupId ) throws BackupServerException
    {
        List < BackupStatusData > statuses = backupApi.listBackups () ;
        Assert.assertNotNull ( statuses ) ;

        BackupStatusData backupStatus = find ( statuses, backupId ) ;
        Assert.assertNotNull ( backupStatus ) ;
        return backupStatus ;
    }

    protected String getTodoServer ()
    {
        return System.getProperty ( "todoserver", "localhost:9000" ) ;
    }

    private boolean isFinished ( Long[] backupIds ) throws BackupServerException
    {
        // get backups statuses
        List < BackupStatusData > statuses = backupApi.listBackups () ;
        Assert.assertNotNull ( statuses ) ;

        // check each status
        for ( BackupStatusData status : statuses )
        {
            if ( BackupState.IN_PROGRESS == status.getState () )
            {
                return false ;
            }
        }

        return true ;
    }

    /**
     * Generate and load specified amount of users to TodoServerStateManager
     * 
     * @param users
     *            - amount of users to generate
     * @param minTodos
     *            - min amount of todo items per user
     * @param maxTodos
     *            - max amount of todo items per user
     * @param usersPerRequest
     *            - amount of users to be loaded per single request to TodoServerStateManager (should be positive number)
     * @throws BackupServerException
     */
    protected void loadUsers ( int users, int minTodos, int maxTodos, int usersPerRequest ) throws BackupServerException
    {
        while ( true )
        {
            if ( users < 0 )
            {
                break ;
            }

            // do my job
            int usersToCreate = Math.min ( users, usersPerRequest ) ;
            UserDataRequest[] entries = createUsers ( usersToCreate, minTodos, maxTodos ) ;
            userApi.load ( entries ) ;
            // for ( UserDataRequest re : entries )
            // {
            // userApi.create ( re ) ;
            // }

            // update progress
            users = users - usersPerRequest ;
        }
    }

    /**
     * Generic initialization
     * 
     * @throws BackupServerException
     */
    protected void onStart () throws BackupServerException
    {
        // init api
        RestTemplate template = new RestTemplate () ;

        userApi = new UserApiRestClient ( template, getTodoServer () ) ;
        backupApi = new BackupApiRestClient ( template, getBackupServer () ) ;

        // let run only test which achieved lock
        sequential.lock () ;
    }

    protected void onStop () throws BackupServerException
    {
        sequential.unlock () ;
    }

    /**
     * Schedules specified amount of backups
     * 
     * @return
     */
    protected Long[] scheduleBackups ( int amount ) throws BackupServerException
    {
        Long[] backupIds = new Long[amount] ;
        for ( int i = 0 ; i < amount ; i++ )
        {
            BackupIdData backup = backupApi.scheduleBackup () ;
            Assert.assertNotNull ( backup ) ;
            Assert.assertNotNull ( backup.getBackupId () ) ;

            backupIds[i] = backup.getBackupId () ;
        }
        return backupIds ;
    }

    /**
     * Waits until all backups will finish
     * 
     * @return
     * @throws InterruptedException
     * @throws BackupServerException
     */
    protected void waitFinish ( Long... backupIds ) throws InterruptedException, BackupServerException
    {
        while ( true )
        {
            // wait a bit
            Thread.sleep ( 500 ) ;

            // check backup status
            if ( isFinished ( backupIds ) )
            {
                break ;
            }
        }
    }
}
