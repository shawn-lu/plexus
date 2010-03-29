package org.codehaus.plexus.util;

/*
 * Copyright The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Generated by JUnitDoclet, a tool provided by ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org and www.objectfab.de for
 * informations about the tool, the licence and the authors.
 */
public class LineOrientedInterpolatingReaderTest
    extends TestCase
{
    public LineOrientedInterpolatingReaderTest( String name )
    {
        super( name );
    }

    /**
     * The JUnit setup method
     */
    protected void setUp() throws Exception
    {
    }

    /**
     * The teardown method for JUnit
     */
    protected void tearDown() throws Exception
    {
    }

    /*
     * Added and commented by jdcasey@03-Feb-2005 because it is a bug in the
     * InterpolationFilterReader.
     */
    public void testShouldInterpolateExpressionAtEndOfDataWithInvalidEndToken() throws IOException
    {
        String testStr = "This is a ${test";
        LineOrientedInterpolatingReader iReader = new LineOrientedInterpolatingReader(
                                                                                       new StringReader( testStr ),
                                                                                       Collections
                                                                                                  .singletonMap(
                                                                                                                 "test",
                                                                                                                 "TestValue" ) );
        BufferedReader reader = new BufferedReader( iReader );

        String result = reader.readLine();

        assertEquals( "This is a ${test", result );
    }

    public void testDefaultInterpolationWithNonInterpolatedValueAtEnd() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "${name} is an ${noun}. ${not.interpolated}";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an asshole. ${not.interpolated}", bar );
    }

    public void testDefaultInterpolationWithEscapedExpression() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "${name} is an ${noun}. \\${noun} value";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an asshole. ${noun} value", bar );
    }

    public void testDefaultInterpolationWithInterpolatedValueAtEnd() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "${name} is an ${noun}";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an asshole", bar );
    }

    public void testInterpolationWithSpecifiedBoundaryTokens() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "@name@ is an @noun@. @not.interpolated@ baby @foo@. @bar@";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m, "@",
                                                                                      "@" );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an asshole. @not.interpolated@ baby @foo@. @bar@", bar );
    }

    public void testInterpolationWithSpecifiedBoundaryTokensWithNonInterpolatedValueAtEnd() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "@name@ is an @foobarred@";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m, "@",
                                                                                      "@" );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an @foobarred@", bar );
    }

    public void testInterpolationWithSpecifiedBoundaryTokensWithInterpolatedValueAtEnd() throws Exception
    {
        Map m = new HashMap();
        m.put( "name", "jason" );
        m.put( "noun", "asshole" );

        String foo = "@name@ is an @noun@";

        LineOrientedInterpolatingReader reader = new LineOrientedInterpolatingReader( new StringReader( foo ), m, "@",
                                                                                      "@" );

        StringWriter writer = new StringWriter();
        IOUtil.copy( reader, writer );

        String bar = writer.toString();
        assertEquals( "jason is an asshole", bar );
    }
}