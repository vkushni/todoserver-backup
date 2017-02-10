
package todoserver.backup.server.businesslogic.bean ;


import java.io.File ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.nio.charset.Charset ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Component ;

import todoserver.backup.server.api.data.BackupState ;
import todoserver.backup.server.api.exception.BackupServerException ;
import todoserver.backup.server.businesslogic.ExportManager ;
import todoserver.backup.server.dal.BackupStatusDao ;
import todoserver.backup.server.dal.ExportDao ;
import todoserver.backup.server.data.TodoData ;
import todoserver.backup.server.data.UserData ;

/**
 * Bean implementation - typical wrapper component with some data conversion logic between data (business logic to data
 * access)
 * 
 * @author vk
 */
@Component
public class ExportManagerBean implements ExportManager
{
    // define constants
    private static final Charset UTF_8 = Charset.forName ( "UTF-8" ) ;
    private static final byte[] FILE_HEADER = new String ( "Username;TodoItemId;Subject;DueDate;Done" ).getBytes ( UTF_8 ) ;

    // auto-wired components
    @Autowired
    private ExportDao exportDao ;

    @Autowired
    private BackupStatusDao backupStatusDao ;

    @Override
    @SuppressWarnings ( "resource" )
    public void add ( Long backupId, UserData user, TodoData todo ) throws BackupServerException
    {
        // build single data line
        StringBuilder b = new StringBuilder () ;
        b.append ( '\n' ) ;
        b.append ( user.getUserName () ).append ( ';' ) ;
        b.append ( todo.getId () ).append ( ';' ) ;
        b.append ( todo.getSubject () ).append ( ';' ) ;
        b.append ( todo.getDueDate () ).append ( ';' ) ;
        b.append ( todo.getDone () ).append ( ';' ) ;

        byte[] bytes = b.toString ().getBytes ( UTF_8 ) ;

        OutputStream stream = exportDao.getStream ( backupId ) ;
        if ( null == stream )
        {
            throw new BackupServerException (
                    "Backup is not initialized properly, no storage for export data for backup id = " + backupId ) ;
        }

        try
        {
            stream.write ( bytes ) ;
        }
        catch ( IOException e )
        {
            // note: if you see some warning here, please take into account:
            // we do not close stream because we didn't opened it (at least at this place)
            // actually there is no business logic intended to do that
            // It was out of scope of requirements but this behavior
            // is rude but usable for state-less services like this one
            throw new BackupServerException (
                    "Unable update export data for backup id = " + backupId + ", user = " + user + ", todo = " + todo, e ) ;
        }
    }

    @Override
    public void finish ( Long backupId ) throws BackupServerException
    {
        // remove only stream reference, since we are not going to
        // write data anymore. the file is kept because we don't know when
        // client may request data
        exportDao.removeStream ( backupId ) ;
    }

    @Override
    public File get ( Long backupId ) throws BackupServerException
    {
        BackupState status = backupStatusDao.getState ( backupId ) ;
        if ( BackupState.IN_PROGRESS == status )
        {
            throw new BackupServerException ( "Backup is not done yet!" ) ;
        }
        if ( BackupState.FAILED == status )
        {
            throw new BackupServerException ( "Backup failed, no backup data available" ) ;
        }

        return exportDao.getFile ( backupId ) ;
    }

    @Override
    @SuppressWarnings ( "resource" )
    public void initalize ( Long backupId ) throws BackupServerException
    {
        try
        {
            // create storage
            exportDao.insert ( backupId ) ;

            // get stream reference
            OutputStream stream = exportDao.getStream ( backupId ) ;

            // write file header
            stream.write ( FILE_HEADER ) ;
        }
        catch ( IOException e )
        {
            throw new BackupServerException ( "Can't initialize backup data storage for backup id = " + backupId, e ) ;
        }
    }
}
