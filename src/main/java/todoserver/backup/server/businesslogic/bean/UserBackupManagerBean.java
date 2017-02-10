
package todoserver.backup.server.businesslogic.bean ;


import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.LinkedBlockingQueue ;
import java.util.function.Consumer ;

import javax.annotation.PostConstruct ;
import javax.annotation.PreDestroy ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Qualifier ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.stereotype.Service ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.UserBackupManager ;
import todoserver.backup.server.data.UserData ;
import todoserver.backup.server.util.ConcurrencyUtil ;
import todoserver.backup.server.util.JsonUtil ;

/**
 * Bean implementation - is a singleton which generate tasks for each backup requested. It set's up queue dispatching
 * using features from parent class. All these extras serve to help backup of concrete user and as result all his todo
 * items.
 * 
 * @author vk
 */
@Service
public class UserBackupManagerBean implements UserBackupManager
{
    /**
     * Just a data container for the data to be placed in the queue
     * 
     * @author vk
     *
     */
    public static class BackupEvent
    {
        // internal state
        public Long backupId ;
        public UserData user ;

        @Override
        public String toString ()
        {
            return JsonUtil.toJson ( this, super.toString () ) ;
        }
    }

    // auto-wired components
    @Autowired
    @Qualifier ( "userBackupConsumer" )
    private Consumer < BackupEvent > userBackupConsumer ;

    // auto-wired configuration
    @Value ( "${maxLoadUserQueueSize}" )
    private int maxQueueSize ;

    @Value ( "${userDispatcherThreadCount}" )
    private int dispatcherCount ;

    // internal state
    private ExecutorService executor ;
    private BlockingQueue < BackupEvent > queue ;

    /**
     * {@inheritDoc}
     */
    @Override
    public void backup ( Long backupId, UserData user ) throws BackupServerException
    {
        BackupEvent event = new BackupEvent () ;
        event.backupId = backupId ;
        event.user = user ;

        try
        {
            queue.put ( event ) ;
        }
        catch ( InterruptedException e )
        {
            throw new BackupServerException ( "Was unable enqueue item for processing = " + event, e ) ;
        }
    }

    /**
     * If max size is not configured uses queue without size limitation. Uses linked blocking queue
     */
    protected BlockingQueue < BackupEvent > createQueue ()
    {
        if ( 0 == maxQueueSize )
        {
            return new LinkedBlockingQueue <> () ;
        }
        else
        {
            return new LinkedBlockingQueue <> ( maxQueueSize ) ;
        }
    }

    /**
     * Generic initialization on construct
     */
    @PostConstruct
    public void onConstruct ()
    {
        // setup queues
        queue = createQueue () ;

        // setup dispatchers executor
        executor = ConcurrencyUtil.create ( queue, userBackupConsumer, dispatcherCount ) ;
    }

    /**
     * Generic de-initialization on destroy
     */
    @PreDestroy
    public void onDestroy ()
    {
        queue.clear () ;
        executor.shutdown () ;
    }

}
