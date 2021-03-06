package org.codehaus.plexus;

import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.manager.UndefinedComponentManagerException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.lifecycle.UndefinedLifecycleHandlerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Jason van Zyl
 */
public class DefaultComponentLookupManager
    implements ComponentLookupManager
{
    private MutablePlexusContainer container;

    // ----------------------------------------------------------------------
    // Component Lookup
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Try to lookup the component manager for the requested component.
    //
    // component manager exists:
    //   -> return a component from the component manager.
    //
    // component manager doesn't exist;
    //   -> lookup component descriptor for the requested component.
    //   -> instantiate component manager for this component.
    //   -> track the component manager for this component by the component class name.
    //   -> return a component from the component manager.
    // ----------------------------------------------------------------------

    public Object lookup( String componentKey )
        throws ComponentLookupException
    {
        Object component;

        ComponentManager componentManager = container.getComponentManagerManager().findComponentManagerByComponentKey( componentKey );

        // The first time we lookup a component a component manager will not exist so we ask the
        // component manager manager to create a component manager for us. Also if we are reloading
        // components then we'll also get a new component manager.

        if ( container.isReloadingEnabled() || componentManager == null )
        {
            ComponentDescriptor descriptor = container.getComponentRepository().getComponentDescriptor( componentKey );

            if ( descriptor == null )
            {
                if ( container.getParentContainer() != null )
                {
                    return container.getParentContainer().lookup( componentKey );
                }

                container.getLogger().debug( "Nonexistent component: " + componentKey );

                String message =
                    "Component descriptor cannot be found in the component repository: " + componentKey + ".";

                throw new ComponentLookupException( message );
            }

            componentManager = createComponentManager( descriptor );
        }

        try
        {
            component = componentManager.getComponent();
        }
        catch ( ComponentInstantiationException e )
        {
            throw new ComponentLookupException(
                "Unable to lookup component '" + componentKey + "', it could not be created", e );
        }
        catch ( ComponentLifecycleException e )
        {
            throw new ComponentLookupException(
                "Unable to lookup component '" + componentKey + "', it could not be started", e );
        }

        container.getComponentManagerManager().associateComponentWithComponentManager( component, componentManager );

        return component;
    }

    private ComponentManager createComponentManager( ComponentDescriptor descriptor )
        throws ComponentLookupException
    {
        ComponentManager componentManager;

        try
        {
            componentManager = container.getComponentManagerManager().createComponentManager( descriptor, container );
        }
        catch ( UndefinedComponentManagerException e )
        {
            String message = "Cannot create component manager for " + descriptor.getComponentKey() +
                ", so we cannot provide a component instance.";

            throw new ComponentLookupException( message, e );
        }
        catch ( UndefinedLifecycleHandlerException e )
        {
            String message = "Cannot create component manager for " + descriptor.getComponentKey() +
                ", so we cannot provide a component instance.";

            throw new ComponentLookupException( message, e );
        }

        return componentManager;
    }

    /**
     * //todo Change this to include components looked up from parents as well...
     */
    public Map lookupMap( String role )
        throws ComponentLookupException
    {
        Map components = new HashMap();

        Map componentDescriptors = container.getComponentDescriptorMap( role );

        if ( componentDescriptors != null )
        {
            // Now we have a map of component descriptors keyed by role hint.

            for ( Iterator i = componentDescriptors.keySet().iterator(); i.hasNext(); )
            {
                String roleHint = (String) i.next();

                Object component = lookup( role, roleHint );

                components.put( roleHint, component );
            }
        }

        return components;
    }

    /**
     * //todo Change this to include components looked up from parents as well...
     */
    public List lookupList( String role )
        throws ComponentLookupException
    {
        List components = new ArrayList();

        List componentDescriptors = container.getComponentDescriptorList( role );

        if ( componentDescriptors != null )
        {
            // Now we have a list of component descriptors.

            for ( Iterator i = componentDescriptors.iterator(); i.hasNext(); )
            {
                ComponentDescriptor descriptor = (ComponentDescriptor) i.next();

                String roleHint = descriptor.getRoleHint();

                Object component;

                if ( roleHint != null )
                {
                    component = lookup( role, roleHint );
                }
                else
                {
                    component = lookup( role );
                }

                components.add( component );
            }
        }

        return components;
    }

    public Object lookup( String role,
                          String roleHint )
        throws ComponentLookupException
    {
        return lookup( role + roleHint );
    }

    public void setContainer( MutablePlexusContainer container )
    {
        this.container = container;
    }
}
