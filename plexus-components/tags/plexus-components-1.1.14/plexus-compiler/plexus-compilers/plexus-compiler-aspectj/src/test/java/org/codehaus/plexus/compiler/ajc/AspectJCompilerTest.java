package org.codehaus.plexus.compiler.ajc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.codehaus.plexus.compiler.AbstractCompilerTest;

/**
 * @author <a href="mailto:jason@plexus.org">Jason van Zyl</a>
 * @version $Id$
 */
public class AspectJCompilerTest
    extends AbstractCompilerTest
{
    public AspectJCompilerTest()
    {
        super();
    }

    protected String getRoleHint()
    {
        return "aspectj";
    }

    protected int expectedErrors()
    {
        return 1;
    }

    protected Collection expectedOutputFiles()
    {
        return Arrays.asList( new String[] { "org/codehaus/foo/ExternalDeps.class", "org/codehaus/foo/Person.class" } );
    }

    protected List getClasspath()
        throws Exception
    {
        List cp = super.getClasspath();

        cp.add( getLocalArtifactPath( "aspectj", "aspectjrt", "1.5.0", "jar" ).getAbsolutePath() );

        return cp;
    }

}
