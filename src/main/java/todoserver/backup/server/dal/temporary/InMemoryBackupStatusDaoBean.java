
package todoserver.backup.server.dal.temporary ;


import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Date ;
import java.util.List ;
import java.util.Map ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.concurrent.atomic.AtomicLong ;

import org.springframework.stereotype.Service ;

import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.data.BackupStatusData ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.dal.BackupStatusDao ;

/**
 * In memory implementation of data access object for backup status
 * 
 * @author vk
 */
@Service
public class InMemoryBackupStatusDaoBean implements BackupStatusDao
{
    // internal state
    private final AtomicLong backupIdGenerator = new AtomicLong () ;
    private final Map < Long, BackupStatusData > cache = new ConcurrentHashMap <> () ;

    /**
     * {@inheritDoc}
     */
    @Override
    public List < BackupStatusData > findAll () throws BackupServerException
    {
        if ( cache.isEmpty () )
        {
            return Collections.emptyList () ;
        }

        return new ArrayList <> ( cache.values () ) ;
    }

    @Override
    public BackupStatusData get ( Long backupId ) throws BackupServerException
    {
        return cache.get ( backupId ) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackupState getState ( Long backupId ) throws BackupServerException
    {
        BackupStatusData data = cache.get ( backupId ) ;
        if ( null == data )
        {
            throw new IllegalArgumentException ( "No backup data found for backup id = " + backupId ) ;
        }

        return data.getState () ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert ( Long backupId, Date dateRequested ) throws BackupServerException
    {
        // create status
        BackupStatusData status = new BackupStatusData () ;
        status.setState ( BackupState.IN_PROGRESS ) ;
        status.setDateRequested ( dateRequested ) ;
        status.setBackupId ( backupId ) ;

        // store status
        cache.put ( backupId, status ) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long nextId () throws BackupServerException
    {
        long value = backupIdGenerator.incrementAndGet () ;
        return Long.valueOf ( value ) ;
    }

    @Override
    public void updateState ( Long backupId, BackupState state ) throws BackupServerException
    {
        // get status data
        BackupStatusData statusData = cache.get ( backupId ) ;
        if ( null == statusData )
        {
            throw new BackupServerException ( "Can't update backup status, backup was not found for backup id = " + backupId ) ;
        }

        boolean currentlyFailed = BackupState.FAILED == statusData.getState () ;

        // mark failed
        if ( BackupState.FAILED == state )
        {
            if ( !currentlyFailed )
            {
                statusData.setState ( state ) ;
                statusData.setDateFinished ( new Date () ) ;
            }
            return ;
        }

        if ( currentlyFailed )
        {
            throw new BackupServerException (
                    "Can't mark backup as done, because recently it was marked as failed for backup id = " + backupId ) ;
        }

        // mark in progress (started)
        if ( BackupState.IN_PROGRESS == state )
        {
            statusData.setDateStarted ( new Date () ) ;
            return ;
        }

        // mark ok
        if ( BackupState.OK == state )
        {
            statusData.setState ( state ) ;
            statusData.setDateFinished ( new Date () ) ;
        }
    }
}
