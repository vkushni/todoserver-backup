
package todoserver.backup.server.dal ;


import java.util.Iterator ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.data.TodoData ;

/**
 * Provides convenient methods to access data
 * 
 * @author vk
 */
public interface TodoDao
{
    /**
     * Iterate over all todo's for particular user
     * 
     * @param userId
     * @return
     * @throws BackupServerException
     */
    Iterator < TodoData > findAll ( Integer userId ) throws BackupServerException ;
}
