package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;

public class ConfigurablePhase
    extends AbstractPhase
{
    public void execute( Object object, ComponentManager manager )
        throws Exception
    {
        if ( object instanceof Configurable )
        {
            ( (Configurable) object ).configure( manager.getComponentDescriptor().getConfiguration() );
        }
    }
}
