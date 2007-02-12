/******************************************************************************
 * $Workfile:  $
 * $Revision$
 * $Author$
 * $Date$
 ******************************************************************************
 * Copyright (c) 2003, Eric Daugherty (http://www.ericdaugherty.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Eric Daugherty nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 * *****************************************************************************
 * For current versions and more information, please visit:
 * http://www.ericdaugherty.com/codehaus/mailserver
 *
 * or contact the author at:
 * codehaus@ericdaugherty.com
 *****************************************************************************/

package org.codehaus.plexus.smtp;

import org.codehaus.plexus.server.ConnectionHandlingException;
import org.codehaus.plexus.server.DefaultServer;
import org.codehaus.plexus.server.ServerConnectionHandler;

import java.net.Socket;

/**
 * Manages the interaction between the server plugins and the
 * SMTP client.  This class provides the implementation
 * of rfc821.
 *
 * @author Eric Daugherty
 * @author Jason van Zyl
 * @plexus.component
 */
public class DefaultSmtpServer
    extends DefaultServer
    implements SmtpServer
{
    /** @plexus.requirement */
    private ServerConnectionHandler handler;

    public void handleConnection( Socket socket )
        throws ConnectionHandlingException
    {
        handler.handleConnection( socket );
    }
}