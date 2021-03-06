package org.codehaus.plexus.logging.slf4j;

/*
 * Copyright 2001-2007 The Codehaus.
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

import org.codehaus.plexus.logging.AbstractLoggerManagerTest;
import org.codehaus.plexus.logging.LoggerManager;

/**
 * SlfLoggerManagerTest
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 */
public class Slf4jLoggerManagerTest
    extends AbstractLoggerManagerTest
{

    protected LoggerManager createLoggerManager()
        throws Exception
    {
        return (LoggerManager) lookup( LoggerManager.ROLE );
    }

    // = SLF4J is a facade, so setting the levels explicitly is no-go

    public void testDebugLevelConfiguration()
        throws Exception
    {
        // pass
    }

    public void testInfoLevelConfiguration()
        throws Exception
    {
        // pass
    }

    public void testWarnLevelConfiguration()
        throws Exception
    {
        // pass
    }

    public void testErrorLevelConfiguration()
        throws Exception
    {
        // pass
    }

    public void testFatalLevelConfiguration()
        throws Exception
    {
        // pass
    }
}
