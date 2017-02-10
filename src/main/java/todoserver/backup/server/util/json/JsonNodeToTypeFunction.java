
package todoserver.backup.server.util.json ;


import com.fasterxml.jackson.databind.JsonNode ;
import com.google.common.base.Function ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.util.JsonUtil ;

/**
 * Function for convertion from json node to concrete object type
 * 
 * @author vk
 *
 * @param <T>
 */
public class JsonNodeToTypeFunction < T > implements Function < JsonNode, T >
{
    private Class < T > type ;

    public JsonNodeToTypeFunction ( Class < T > type )
    {
        this.type = type ;
    }

    @Override
    public T apply ( JsonNode node )
    {
        try
        {
            return JsonUtil.fromJson ( type, node.toString () ) ;
        }
        catch ( BackupServerException e )
        {
            throw new RuntimeException ( "Unable parse json node, node = " + node, e ) ;
        }
    }

}
