package org.codehaus.plexus.logging.log4j;

/* ----------------------------------------------------------------------------
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.codehaus.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Plexus", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact codehaus@codehaus.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ----------------------------------------------------------------------------
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.codehaus.org/>.
 *
 * ----------------------------------------------------------------------------
 */

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.plexus.logging.AbstractLoggerManager;

import java.util.Properties;

/**
 <pre>
 <logging>

 <!-- LoggerManger -->
 <logger-manager-type>log4j</logger-manager-type>

 <!-- Loggers -->

 <logger>
 <id>root</id>
 <appender-id>default</appender-id>
 <priority>INFO</priority>
 </logger>

 <!-- Appenders -->

 <appender>
 <id>default</id>
 <type>file</type>
 <type-configuration>
 <file>${plexus.home}/logs/plexus.log</file>
 <append>true</append>
 </type-configuration>
 <threshold>INFO</threshold>
 <layout>pattern-layout</layout>
 <conversion-pattern>%-4r [%t] %-5p %c %x - %m%n</conversion-pattern>
 </appender>
 </logging>
 </pre>
 */
public class Log4JLoggerManager
    extends AbstractLoggerManager
    implements Configurable, Initializable, Startable
{
    // Sink tags.

    /** */
    public static final String SINK_TAG = "appender";
    /** */
    public static final String ID_TAG = "id";
    /** */
    public static final String TYPE_TAG = "type";
    /** */
    public static final String THRESHOLD_TAG = "threshold";
    /** */
    public static final String LAYOUT_TAG = "layout";
    /** */
    public static final String CONVERSION_PATTERN_TAG = "conversion-pattern";

    // Logger tags.

    /** */
    public static final String LOGGER_TAG = "logger";
    /** */
    public static final String SINK_ID_TAG = "appender-id";
    /** */
    public static final String PRIORITY_TAG = "priority";
    /** */
    public static final String ROOT_LOGGER_TAG = "root";


    // Type tags.

    /** */
    public static final String TYPE_CONFIGURATION_TAG = "type-configuration";

    /** */
    public static final String FILE_TAG = "file";

    /** */
    public static final String APPEND_TAG = "append";

    /** Root logger set flag. */
    private boolean rootLoggerSet = false;

    /** Logger configurations. */
    private Configuration[] loggerConfigurations;

    /** Appender configurations. */
    private Configuration[] appenderConfigurations;

    /** Log4j properties used to init log4j. */
    private Properties log4JProperties;

    /** Constructor. */
    public Log4JLoggerManager()
    {
    }

    /**
     *
     * @return
     */
    public Properties getLog4JProperties()
    {
        return log4JProperties;
    }

    // ----------------------------------------------------------------------
    // Lifecycle Management
    // ----------------------------------------------------------------------

    public void setLog4JProperties( Properties log4JProperties )
    {
        this.log4JProperties = log4JProperties;
    }

    /** @see Configurable#configure */
    public void configure( Configuration configuration )
        throws ConfigurationException
    {
        appenderConfigurations = configuration.getChildren( SINK_TAG );
        loggerConfigurations = configuration.getChildren( LOGGER_TAG );
    }

    /** @see Initializable#initialize */
    public void initialize()
        throws Exception
    {
        log4JProperties = new Properties();

        for ( int i = 0; i < appenderConfigurations.length; i++ )
        {
            log4JProperties.putAll( createAppenderProperties( appenderConfigurations[i] ) );
        }

        for ( int i = 0; i < loggerConfigurations.length; i++ )
        {
            log4JProperties.putAll( createLoggerProperties( loggerConfigurations[i] ) );
        }

        // If there is no default logger we will make one that logs
        // to the console.
        if ( rootLoggerSet == false )
        {
            log4JProperties.putAll( createDefaultRootLoggerProperties() );
        }
    }

    /** @see Startable#start */
    public void start()
        throws Exception
    {
        PropertyConfigurator.configure( log4JProperties );
    }

    /** @see Startable#stop */
    public void stop()
        throws Exception
    {
    }

    public Properties createDefaultRootLoggerProperties()
    {
        Properties p = new Properties();

        p.setProperty( "log4j.rootLogger", "INFO,console" );
        p.setProperty( "log4j.appender.console", "org.codehaus.log4j.ConsoleAppender" );
        p.setProperty( "log4j.appender.console.threshold", " INFO" );
        p.setProperty( "log4j.appender.console.layout", "org.codehaus.log4j.PatternLayout" );
        p.setProperty( "log4j.appender.console.layout.ConversionPattern", "%-4r [%t] %-5p %c %x - %m%n" );

        return p;
    }

    /**
     *
     * @param loggerConfiguration
     * @return Log4j loggerConfiguration properties.
     * @throws ConfigurationException
     */
    Properties createLoggerProperties( Configuration loggerConfiguration )
        throws ConfigurationException
    {
        Properties loggerProperties = new Properties();

        String id = loggerConfiguration.getChild( ID_TAG ).getValue();
        String appenderId = loggerConfiguration.getChild( SINK_ID_TAG ).getValue();
        String priority = loggerConfiguration.getChild( PRIORITY_TAG ).getValue();

        if ( id.equals( ROOT_LOGGER_TAG ) )
        {
            loggerProperties.setProperty( "log4j.rootLogger", priority + "," + appenderId );
            rootLoggerSet = true;
        }
        else
        {
            loggerProperties.setProperty( "log4j.logger", priority + "," + appenderId );
        }

        return loggerProperties;
    }

    /**
     * We need to make property entries like this from an appending
     * configuration entry:
     *
     * log4j.appenderConfiguration.testlog = org.codehaus.log4j.FileAppender
     * log4j.appenderConfiguration.testlog.file = test.log
     * log4j.appenderConfiguration.testlog.append = false
     * log4j.appenderConfiguration.testlog.threshold = INFO
     * log4j.appenderConfiguration.testlog.layout = org.codehaus.log4j.PatternLayout
     * log4j.appenderConfiguration.testlog.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
     *
     * @return Log4j appenderConfiguration properties.
     */
    Properties createAppenderProperties( Configuration appenderConfiguration )
        throws ConfigurationException
    {
        Properties appenderProperties = new Properties();

        String id = appenderConfiguration.getChild( ID_TAG ).getValue();
        String type = appenderConfiguration.getChild( TYPE_TAG ).getValue();

        // We need to look at the type and set any type specific properties.
        Configuration typeConfiguration = appenderConfiguration.getChild( TYPE_CONFIGURATION_TAG );
        String file = typeConfiguration.getChild( FILE_TAG ).getValue();
        String append = typeConfiguration.getChild( APPEND_TAG ).getValue();

        String threshold = appenderConfiguration.getChild( THRESHOLD_TAG ).getValue();
        String layout = appenderConfiguration.getChild( LAYOUT_TAG ).getValue();
        String conversionPattern = appenderConfiguration.getChild( CONVERSION_PATTERN_TAG ).getValue();

        String base = "log4j.appender." + id;

        if ( type.equals( "rollingFile" ) )
        {
            appenderProperties.setProperty( base, "org.codehaus.log4j.RollingFileAppender" );
            appenderProperties.setProperty( base + ".MaxBackupIndex",
                                            typeConfiguration.getChild( "maxBackupIndex" ).getValue() );
            appenderProperties.setProperty( base + ".MaxFileSize",
                                            typeConfiguration.getChild( "maxFileSize" ).getValue() );
        }
        else
        {
            appenderProperties.setProperty( base, "org.codehaus.log4j.FileAppender" );
        }

        appenderProperties.setProperty( base + ".file", file );
        appenderProperties.setProperty( base + ".append", append );
        appenderProperties.setProperty( base + ".threshold", threshold );
        appenderProperties.setProperty( base + ".layout", "org.codehaus.log4j.PatternLayout" );
        appenderProperties.setProperty( base + ".layout.conversionPattern", conversionPattern );

        return appenderProperties;
    }

    public Logger getRootLogger()
    {
        return new Log4JLogger( org.apache.log4j.Logger.getRootLogger() );
    }

    public Logger getLogger( String name )
    {
        return new Log4JLogger( org.apache.log4j.Logger.getLogger( name ) );
    }
}
