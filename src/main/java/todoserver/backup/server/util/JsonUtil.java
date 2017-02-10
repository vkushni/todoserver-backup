
package todoserver.backup.server.util ;


import com.fasterxml.jackson.annotation.JsonInclude.Include ;
import com.fasterxml.jackson.core.JsonParser.Feature ;
import com.fasterxml.jackson.core.type.TypeReference ;
import com.fasterxml.jackson.databind.DeserializationFeature ;
import com.fasterxml.jackson.databind.ObjectMapper ;
import com.fasterxml.jackson.databind.SerializationFeature ;

import todoserver.backup.server.api.exception.BackupServerException ;

/**
 * Utility methods for working with json
 * 
 * @author vk
 */
public class JsonUtil
{
    // define constants
    private static final ObjectMapper OM_INSTANCE = createObjectMapper () ;

    /*
     * Create object mapper with default settings
     */
    private static ObjectMapper createObjectMapper ()
    {
        ObjectMapper mapper = new ObjectMapper () ;
        mapper.enable ( Feature.AUTO_CLOSE_SOURCE ) ;
        mapper.enable ( SerializationFeature.CLOSE_CLOSEABLE ) ;
        mapper.setSerializationInclusion ( Include.NON_NULL ) ;
        mapper.disable ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES ) ;
        return mapper ;
    }

    /**
     * Serialize object from json
     * 
     * @param type
     *            - type of the object result
     * @param json
     *            - json to use
     * 
     * @return object representation of retrieved json
     * @throws BackupServerException
     *             in case of any error
     */
    public static < T > T fromJson ( Class < T > type, String json ) throws BackupServerException
    {
        try
        {
            return getMapper ().readValue ( json, type ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't parse json for json = " + json + ", object type = " + type, e ) ;
        }
    }

    /**
     * Serialize object from json
     * 
     * @param typeReference
     *            - type meta data container
     * @param json
     *            - json to use
     * @return
     * @throws BackupServerException
     */
    public static < T > T fromJson ( TypeReference < T > typeReference, String json ) throws BackupServerException
    {
        try
        {
            return getMapper ().readValue ( json, typeReference ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't parse json for json = " + json + ", object type reference = " + typeReference, e ) ;
        }
    }

    /**
     * Get default instance of object mapper
     * 
     * @return
     */
    public static ObjectMapper getMapper ()
    {
        return OM_INSTANCE ;
    }

    /**
     * Serialize object into json
     * 
     * @param object
     * @return
     * @throws BackupServerException
     */
    public static String toJson ( Object object ) throws BackupServerException
    {
        try
        {
            return getMapper ().writeValueAsString ( object ) ;
        }
        catch ( Exception e )
        {
            throw new BackupServerException ( "Can't write json for the object, object = " + object, e ) ;
        }
    }

    /**
     * Serialize object into json
     * 
     * @param object
     *            - object to serialize
     * @param defaultValue
     *            - default value to be returned in case of any error
     * @return
     */
    public static String toJson ( Object object, String defaultValue )
    {
        try
        {
            return getMapper ().writeValueAsString ( object ) ;
        }
        catch ( Exception e )
        {
            // ignore exception
        }

        return defaultValue ;
    }
}
