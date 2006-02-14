package org.codehaus.plexus.ldap.helper;

/*
 * The MIT License
 * Copyright (c) 2005, The Codehaus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.List;
import java.util.Collections;
import java.util.Properties;
import java.util.Map;

/**
 * The configuration for a connection will not change.
 *
 * @author <a href="mailto:trygvis@inamo.no">trygvis</a>
 * @version $Id: LdapConnection.java,v 1.3 2006/02/06 15:15:54 trygvis Exp $
 */
public class LdapConnection
{
    private LdapConnectionConfiguration config;

    private DirContext context;

    private List<Rdn> baseDnRdns;

    public LdapConnection( LdapConnectionConfiguration config, Rdn subRdn )
        throws LdapException
    {
        this.config = config;

        LdapName baseDn = new LdapName( config.getBaseDN().getRdns() );
        if ( subRdn != null )
        {
            baseDn.add( subRdn );
        }
        baseDnRdns = Collections.unmodifiableList( baseDn.getRdns() );

        connect();
    }

    // ----------------------------------------------------------------------
    // Connection Managment
    // ----------------------------------------------------------------------

    public void connect()
        throws LdapException
    {
        Properties env = new Properties();

        for ( Map.Entry entry : config.getExtraProperties().entrySet() )
        {
            env.put( entry.getKey(), entry.getValue() );
        }

        config.check();

        env.put( Context.INITIAL_CONTEXT_FACTORY, config.getContextFactory() );

        if ( config.getPort() != 0 )
        {
            env.put( Context.PROVIDER_URL, "ldap://" + config.getHostname() + ":" + config.getPort() + "/" );
        }
        else
        {
            env.put( Context.PROVIDER_URL, "ldap://" + config.getHostname() + "/" );
        }

        env.put( Context.SECURITY_AUTHENTICATION, "simple" );
        if ( config.getBindDN() != null )
        {
            env.put( Context.SECURITY_PRINCIPAL, config.getBindDN().toString() );
        }
        if ( config.getPassword() != null )
        {
            env.put( Context.SECURITY_CREDENTIALS, config.getPassword() );
        }

        try
        {
            Hashtable<Object, Object> e = new Hashtable<Object, Object>();
            for ( Map.Entry<Object, Object> entry : env.entrySet() )
            {
                e.put( entry.getKey(), entry.getValue() );
            }
            context = new InitialDirContext( e );
        }
        catch ( NamingException ex )
        {
            throw new LdapException( "Could not connect to the server.", ex );
        }
    }

    public void close()
    {
        try
        {
            if ( context != null )
            {
                context.close();
            }
        }
        catch ( NamingException ex )
        {
            // ignore
        }
        finally
        {
            context = null;
        }
    }

    // ----------------------------------------------------------------------
    // Utils
    // ----------------------------------------------------------------------

    public LdapConnectionConfiguration getConfiguration()
    {
        return config;
    }

    public List<Rdn> getBaseDnRdns()
    {
        return baseDnRdns;
    }

    public DirContext getDirContext()
    {
        return context;
    }
}
