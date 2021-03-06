package org.codehaus.plexus.ant;

import org.apache.tools.ant.Task;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.embed.Embedder;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public abstract class AbstractPlexusAntTask
    extends Task
{
    protected abstract String getRole();

    public Object getComponent()
        throws Exception
    {
        Embedder embedder = new Embedder();

        ClassWorld classWorld = new ClassWorld( "core", embedder.getClass().getClassLoader() );

        embedder.start( classWorld );

        return embedder.lookup( getRole() );
    }
}
