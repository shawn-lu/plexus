package org.codehaus.plexus.security.ui.web;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
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

import java.util.Properties;

import junit.framework.TestCase;

/**
 * HttpUtilsTest 
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 */
public class HttpUtilsTest
    extends TestCase
{
    public void testComplexHeaderToProperties()
    {
        String rawheader = "realm=\"Somewhere Over The Rainbow\", domain = \"kansas.co.us\", nonce=\"65743ABCF";

        Properties props = HttpUtils.complexHeaderToProperties( rawheader, ",", "=" );

        assertNotNull( props );
        assertEquals( 3, props.size() );
        assertEquals( "Somewhere Over The Rainbow", props.get( "realm" ) );
        assertEquals( "kansas.co.us", props.get( "domain" ) );
        assertEquals( "65743ABCF", props.get( "nonce" ) );
    }
}
