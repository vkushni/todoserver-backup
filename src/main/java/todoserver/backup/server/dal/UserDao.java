
package todoserver.backup.server.dal ;


import java.util.Iterator ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.UserData ;

/**
 * Provides convenient methods to access data
 * 
 * @author vk
 */
public interface UserDao
{
    /**
     * Iterate over all users
     * 
     * @return
     * @throws BackupServerException
     */
    Iterator < UserData > iterateAll () throws BackupServerException ;
}
