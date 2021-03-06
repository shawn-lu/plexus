package org.codehaus.plexus.component.repository;

/*
 * Copyright 2001-2006 Codehaus Foundation.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ComponentSetDescriptor
{
    // ComponentDescriptor list
    private List components;

    private List dependencies;

    private boolean isolatedRealm;

    private String id;

    public List getComponents()
    {
        return components;
    }

    public void addComponentDescriptor( ComponentDescriptor cd )
    {
        if ( components == null )
        {
            components = new ArrayList();
        }

        components.add( cd );
    }

    public void setComponents( List components )
    {
        this.components = components;
    }

    public List getDependencies()
    {
        return dependencies;
    }

    public void addDependency( ComponentDependency cd )
    {
        if ( dependencies == null )
        {
            dependencies = new ArrayList();
        }

        dependencies.add( cd );
    }

    public void setDependencies( List dependencies )
    {
        this.dependencies = dependencies;
    }

    public void setIsolatedRealm( boolean isolatedRealm )
    {
        this.isolatedRealm = isolatedRealm;
    }

    public boolean isIsolatedRealm()
    {
        return isolatedRealm;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "Component Descriptor: " );

        for ( Iterator i = components.iterator(); i.hasNext(); )
        {
            ComponentDescriptor cd = (ComponentDescriptor) i.next();

            sb.append( cd.getHumanReadableKey() ).append( "\n" );
        }

        sb.append( "---" );

        return sb.toString();
    }
}
