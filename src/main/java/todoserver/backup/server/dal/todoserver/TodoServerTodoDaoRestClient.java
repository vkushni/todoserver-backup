
package todoserver.backup.server.dal.todoserver ;


import java.io.File ;
import java.net.URL ;
import java.text.MessageFormat ;
import java.util.Iterator ;

import org.springframework.beans.factory.annotation.Value ;
import org.springframework.retry.annotation.Retryable ;
import org.springframework.stereotype.Component ;

import com.fasterxml.jackson.databind.JsonNode ;
import com.google.common.collect.Iterators ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.dal.TodoDao ;
import todoserver.backup.server.data.TodoData ;
import todoserver.backup.server.util.FileUtil ;
import todoserver.backup.server.util.JsonUtil ;
import todoserver.backup.server.util.json.JsonNodeToTypeFunction ;

/**
 * Rest client implementation for data access object for todos. Data is retrieved from todo server which hostname is
 * configurable.
 * 
 * @author vk
 */
@Component
public class TodoServerTodoDaoRestClient implements TodoDao
{
    // auto-wired configuration
    @Value ( "${todoserver.hostname}" )
    private String serverHostname ;

    @Value ( "${todoserver.url.pattern.getTodos}" )
    private String findAllUrlPattern ;

    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable ( maxAttempts = 15, include = BackupServerException.class )
    public Iterator < TodoData > findAll ( Integer userId ) throws BackupServerException
    {
        try
        {
            // create url
            String url = MessageFormat.format ( findAllUrlPattern, serverHostname, userId + "" ) ;

            // create tmp file
            File file = FileUtil.createTemporaryFile () ;

            // save json into a file
            FileUtil.saveAs ( new URL ( url ), file ) ;

            JsonNode jsonNode = JsonUtil.getMapper ().readTree ( file ) ;
            return Iterators.transform ( jsonNode.iterator (), new JsonNodeToTypeFunction <> ( TodoData.class ) ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Was unable list todos for user id = " + userId, e ) ;
        }
    }

}
