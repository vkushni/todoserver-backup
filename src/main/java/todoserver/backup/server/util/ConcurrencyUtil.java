
package todoserver.backup.server.util ;


import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.Executors ;
import java.util.function.Consumer ;

import todoserver.backup.server.util.concurrency.QueueDispatcherThread ;

/**
 * Utility methods for multi-threading
 * 
 * @author vk
 */
public class ConcurrencyUtil
{
    /**
     * Create executor service for dispatching blocking queue
     * 
     * @param queue
     *            - queue to dispatch
     * @param consumer
     *            - consumer for queue elements
     * @param threads
     *            - amount of dispatcher threads
     * @return
     */
    public static < T > ExecutorService create ( BlockingQueue < T > queue, Consumer < T > consumer, int threads )
    {
        ExecutorService service = Executors.newFixedThreadPool ( threads ) ;
        for ( int i = 0 ; i < threads ; i++ )
        {
            QueueDispatcherThread < T > t = new QueueDispatcherThread <> ( queue, consumer ) ;
            t.setDaemon ( true ) ;
            t.start () ;

            service.execute ( t ) ;
        }
        return service ;
    }
}
