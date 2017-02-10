
package todoserver.backup.server.dal.temporary ;


import java.util.ArrayList ;
import java.util.List ;
import java.util.Map ;
import java.util.concurrent.ConcurrentHashMap ;

import org.springframework.stereotype.Service ;

import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.dal.BackupProgressDao ;
import todoserver.backup.server.data.BackupProgressData ;

/**
 * In memory implementation of data access object for backup status
 * 
 * @author vk
 */
@Service
public class InMemoryBackupProgressDaoBean implements BackupProgressDao
{
    // internal state
    private final Map < Long, BackupProgressData > cache = new ConcurrentHashMap <> () ;

    /**
     * {@inheritDoc}
     */
    @Override
    public List < BackupProgressData > findAll ()
    {
        return new ArrayList <> ( cache.values () ) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackupProgressData get ( Long backupId ) throws BackupServerException
    {
        return cache.get ( backupId ) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert ( Long backupId ) throws BackupServerException
    {
        BackupProgressData data = new BackupProgressData () ;
        data.setBackupId ( backupId ) ;

        cache.put ( backupId, data ) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove ( Long backupId ) throws BackupServerException
    {
        cache.remove ( backupId ) ;
    }
}
