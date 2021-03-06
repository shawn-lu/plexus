package org.codehaus.plexus.archiver.util;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.lang.reflect.Method;

public final class ArchiveEntryUtils
{

    public static boolean jvmFilePermAvailable = false;

    static
    {
        try
        {
            jvmFilePermAvailable = File.class.getMethod( "setReadable", new Class[] { Boolean.TYPE } ) != null;
        }
        catch ( final Exception e )
        {
            // ignore exception log this ?
        }
    }

    private ArchiveEntryUtils()
    {
        // no op
    }

    /**
     * @since 1.1
     * @param file
     * @param mode
     * @param logger
     * @param useJvmChmod
     *            will use jvm file permissions <b>not available for group level</b>
     * @throws ArchiverException
     */
    public static void chmod( final File file, final int mode, final Logger logger, boolean useJvmChmod )
        throws ArchiverException
    {
        if ( !Os.isFamily( Os.FAMILY_UNIX ) )
        {
            return;
        }

        final String m = Integer.toOctalString( mode & 0xfff );

        if ( useJvmChmod && !jvmFilePermAvailable )
        {
            logger.info( "you want to use jvmChmod but it's not possible where your current jvm" );
            useJvmChmod = false;
        }

        if ( useJvmChmod && jvmFilePermAvailable )
        {
            applyPermissionsWithJvm( file, m, logger );
            return;
        }

        try
        {
            final Commandline commandline = new Commandline();

            commandline.setWorkingDirectory( file.getParentFile()
                                                 .getAbsolutePath() );

            if ( logger.isDebugEnabled() )
            {
                logger.debug( file + ": mode " + Integer.toOctalString( mode ) + ", chmod " + m );
            }

            commandline.setExecutable( "chmod" );

            commandline.createArg()
                       .setValue( m );

            final String path = file.getAbsolutePath();

            commandline.createArg()
                       .setValue( path );

            // commenting this debug statement, since it can produce VERY verbose output...
            // this method is called often during archive creation.
            // logger.debug( "Executing:\n\n" + commandline.toString() + "\n\n" );

            final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

            final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();

            final int exitCode = CommandLineUtils.executeCommandLine( commandline, stderr, stdout );

            if ( exitCode != 0 )
            {
                logger.warn( "-------------------------------" );
                logger.warn( "Standard error:" );
                logger.warn( "-------------------------------" );
                logger.warn( stderr.getOutput() );
                logger.warn( "-------------------------------" );
                logger.warn( "Standard output:" );
                logger.warn( "-------------------------------" );
                logger.warn( stdout.getOutput() );
                logger.warn( "-------------------------------" );

                throw new ArchiverException( "chmod exit code was: " + exitCode );
            }
        }
        catch ( final CommandLineException e )
        {
            throw new ArchiverException( "Error while executing chmod.", e );
        }

    }

    /**
     * <b>jvm chmod will be used only if System property <code>useJvmChmod</code> set to true</b>
     * 
     * @param file
     * @param mode
     * @param logger
     * @throws ArchiverException
     */
    public static void chmod( final File file, final int mode, final Logger logger )
        throws ArchiverException
    {
        chmod( file, mode, logger, Boolean.getBoolean( "useJvmChmod" ) && jvmFilePermAvailable );
    }

    private static void applyPermissionsWithJvm( final File file, final String mode, final Logger logger )
    {
        final FilePermission filePermission = FilePermissionUtils.getFilePermissionFromMode( mode, logger );

        Method method;
        try
        {
            method = File.class.getMethod( "setReadable", new Class[] { Boolean.TYPE, Boolean.TYPE } );

            method.invoke( file,
                           new Object[] { Boolean.valueOf( filePermission.isReadable() ),
                               Boolean.valueOf( filePermission.isOwnerOnlyReadable() ) } );

            method = File.class.getMethod( "setExecutable", new Class[] { Boolean.TYPE, Boolean.TYPE } );
            method.invoke( file,
                           new Object[] { Boolean.valueOf( filePermission.isExecutable() ),
                               Boolean.valueOf( filePermission.isOwnerOnlyExecutable() ) } );

            method = File.class.getMethod( "setWritable", new Class[] { Boolean.TYPE, Boolean.TYPE } );
            method.invoke( file,
                           new Object[] { Boolean.valueOf( filePermission.isWritable() ),
                               Boolean.valueOf( filePermission.isOwnerOnlyWritable() ) } );
        }
        catch ( final Exception e )
        {
            logger.error( "error calling dynamically file permissons with jvm " + e.getMessage(), e );
            throw new RuntimeException( "error calling dynamically file permissons with jvm " + e.getMessage(), e );
        }
    }

}
