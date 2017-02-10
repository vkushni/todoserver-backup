
package com.cloudfinder.codeassignment.todoserver.api ;


import com.cloudfinder.codeassignment.todoserver.api.data.UserDataRequest ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.dal.TodoDao ;
import todoserver.backup.server.dal.UserDao ;

/**
 * Provide API for managing users on TodoServerStateManager
 * 
 * @author vk
 */
public interface UserApi extends UserDao, TodoDao
{
    /**
     * Create users for each provided request
     * 
     * @param users
     *            - user requests
     * @throws BackupServerException
     */
    void load ( UserDataRequest... users ) throws BackupServerException ;

    /**
     * Remove single user by user id
     * 
     * @param userId
     * @throws BackupServerException
     */
    void removeUser ( Integer userId ) throws BackupServerException ;

    /**
     * Remove all users
     * 
     * @throws BackupServerException
     */
    void removeUsers () throws BackupServerException ;
}
