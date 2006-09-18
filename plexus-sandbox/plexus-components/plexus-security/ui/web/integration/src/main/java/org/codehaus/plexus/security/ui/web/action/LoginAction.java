package org.codehaus.plexus.security.ui.web.action;

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.security.authentication.AuthenticationDataSource;
import org.codehaus.plexus.security.authentication.AuthenticationException;
import org.codehaus.plexus.security.policy.AccountLockedException;
import org.codehaus.plexus.security.policy.MustChangePasswordException;
import org.codehaus.plexus.security.system.SecuritySession;
import org.codehaus.plexus.security.user.UserNotFoundException;
import org.codehaus.plexus.util.StringUtils;

/**
 * LoginAction
 *
 * @author Jesse McConnell <jmcconnell@apache.org>
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 * 
 * @plexus.component role="com.opensymphony.xwork.Action"
 *                   role-hint="pss-login"
 *                   instantiation-strategy="per-lookup"
 */
public class LoginAction
    extends AbstractAuthenticationAction
{
    private static final String LOGIN_SUCCESS = "security-login-success";
    private static final String LOGIN_CANCEL = "security-login-cancel";
    private static final String PASSWORD_CHANGE = "must-change-password";
    private static final String ACCOUNT_LOCKED = "security-login-locked";
    
    // ------------------------------------------------------------------
    // Plexus Component Requirements
    // ------------------------------------------------------------------

    private String username;

    private String password;
    
    private boolean cancelButton;

    // ------------------------------------------------------------------
    // Action Entry Points - (aka Names)
    // ------------------------------------------------------------------
    
    public String show()
    {
        // System.err.println("LoginAction.show()");        
        return INPUT;
    }
    
    public String login()
    {
        if ( cancelButton )
        {
            return LOGIN_CANCEL;
        }
        
        if ( StringUtils.isEmpty(username)  )
        {
            addFieldError( "username", "Username cannot be empty." );
            return ERROR;
        }
        
        // An attempt should log out your authentication tokens first!
        setAuthTokens( null, null, false );

        clearErrorsAndMessages();

        try
        {
            SecuritySession securitySession = securitySystem
                .authenticate( new AuthenticationDataSource( username, password ) );

            if ( securitySession.getAuthenticationResult().isAuthenticated() )
            {
                // Success!  Create tokens.
                setAuthTokens( securitySession, securitySession.getUser(), true );
                return LOGIN_SUCCESS;
            }
            else
            {
                getLogger().debug( "Login Action failed against principal : "
                                       + securitySession.getAuthenticationResult().getPrincipal(),
                                   securitySession.getAuthenticationResult().getException() );
                addActionError( "Authentication failed" );
                return ERROR;
            }
        }
        catch ( AuthenticationException ae )
        {
            addActionError( ae.getMessage() );
            return ERROR;
        }
        catch ( UserNotFoundException ue )
        {
            addActionError( ue.getMessage() );
            return ERROR;
        }
        catch ( AccountLockedException e )
        {
            addActionError( "Your Account is Locked." );
            return ACCOUNT_LOCKED;
        }
        catch ( MustChangePasswordException e )
        {
            addActionError( "You must change your password." );
            return PASSWORD_CHANGE;
        }
    }

    // ------------------------------------------------------------------
    // Parameter Accessor Methods
    // ------------------------------------------------------------------

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public boolean isCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton( boolean cancelButton )
    {
        this.cancelButton = cancelButton;
    }
}
