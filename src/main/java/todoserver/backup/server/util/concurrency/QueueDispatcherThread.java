
package todoserver.backup.server.util.concurrency ;


import java.util.concurrent.BlockingQueue ;
import java.util.function.Consumer ;

/**
 * Generic blocking queue dispatcher thread
 * 
 * @author vkushnir
 * @param <T>
 */
public class QueueDispatcherThread < T > extends Thread implements Cloneable
{
    // internal state
    private Consumer < T > queueConsumer ;
    private BlockingQueue < T > queue ;

    public QueueDispatcherThread ( BlockingQueue < T > queue, Consumer < T > queueConsumer )
    {
        this.queue = queue ;
        this.queueConsumer = queueConsumer ;
    }

    /**
     * Dispatch blocking queue
     */
    @Override
    public void run ()
    {
        while ( true )
        {
            try
            {
                T element = queue.take () ;
                queueConsumer.accept ( element ) ;
            }
            catch ( InterruptedException e )
            {
                // ignore exceptions
                sleep () ;
            }
        }
    }

    private void sleep ()
    {
        try
        {
            Thread.sleep ( 100 ) ;
        }
        catch ( InterruptedException e1 )
        {
            // ignore
        }
    }
}
