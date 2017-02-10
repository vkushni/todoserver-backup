
package todoserver.backup.server.businesslogic.bean ;


import java.util.Date ;
import java.util.List ;
import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.LinkedBlockingQueue ;
import java.util.function.Consumer ;

import javax.annotation.PostConstruct ;
import javax.annotation.PreDestroy ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Qualifier ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.stereotype.Service ;

import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.BackupManager ;
import todoserver.backup.server.dal.BackupStatusDao ;
import todoserver.backup.server.util.ConcurrencyUtil ;

/**
 * Bean implementation - is the 'core' of the service, it is responsible for handling requested backups and their async
 * processing.
 * 
 * @author vk
 */
@Service
public class BackupManagerBean implements BackupManager
{
    // auto-wired components
    @Autowired
    private BackupStatusDao backupStatusDao ;

    @Autowired
    @Qualifier ( "backupConsumer" )
    private Consumer < Long > backupConsumer ;

    // auto-wired configuration
    @Value ( "${maxConcurrentBackupTasks}" )
    private int maxQueueSize ;

    @Value ( "${backupDispatcherThreadCount}" )
    private int dispatcherCount ;

    // internal state
    protected Logger log = LoggerFactory.getLogger ( getClass () ) ;
    private ExecutorService executor ;
    private BlockingQueue < Long > queue ;

    /**
     * If max size is not configured uses queue without size limitation. Uses linked blocking queue
     */
    protected BlockingQueue < Long > createQueue ()
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

    @Override
    public int getBackupCount ()
    {
        return queue.size () ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed () throws BackupServerException
    {
        return queue.size () < maxQueueSize ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List < BackupStatusData > listStatuses () throws BackupServerException
    {
        return backupStatusDao.findAll () ;
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
        executor = ConcurrencyUtil.create ( queue, backupConsumer, dispatcherCount ) ;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Long schedule () throws BackupServerException
    {
        // check if schedule
        if ( false == isAllowed () )
        {
            throw new BackupServerException ( "Concurrent backup threshold reached" ) ;
        }

        // store backup status
        // generate backup id
        Long backupId = backupStatusDao.nextId () ;

        // update backup status
        backupStatusDao.insert ( backupId, new Date () ) ;

        // add to loading queue
        queue.add ( backupId ) ;

        return backupId ;
    }

}
