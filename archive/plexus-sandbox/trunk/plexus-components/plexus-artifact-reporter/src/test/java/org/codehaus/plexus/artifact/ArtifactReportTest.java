/**
 *
 * Copyright 2006
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.codehaus.plexus.artifact;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.velocity.DefaultVelocityComponent;
import org.codehaus.plexus.velocity.VelocityComponent;

import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Scm;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Site;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;

/**
 * @author John Tolentino
 */
public class ArtifactReportTest
    extends PlexusTestCase
{
    public void testSimple()
        throws Exception
    {
        ArtifactReport report = (DefaultArtifactReport) lookup( ArtifactReport.ROLE );
        assertNotNull( report );

        VelocityComponent velocityComponent = (DefaultVelocityComponent) lookup( DefaultVelocityComponent.ROLE );
        assertNotNull( velocityComponent );
    }

    public void testVelocityContextPlexusConfiguration()
        throws Exception
    {
        ArtifactReport report = (DefaultArtifactReport) lookup( ArtifactReport.ROLE );
        assertNotNull( report );

        MavenProject project = new MavenProject();

        project.setGroupId( "org.codehaus.plexus.artifact" );

        project.setArtifactId( "ArtifactReporter" );

        project.setVersion( "1.0" );

        project.setUrl( "" );

        Scm scm = new Scm();

        scm.setUrl( "http://svn.codehaus.org/swizzle/trunk" );

        project.setScm( scm );

        DistributionManagement distributionManagement = new DistributionManagement();

        distributionManagement.setDownloadUrl( "http://download-it-here.org/repo/swizzle-1.0.jar" );

        Site site = new Site();
        site.setUrl( "http://people.apache.org/~jtolentino/release-reports" );

        distributionManagement.setSite( site );

        project.setDistributionManagement( distributionManagement );

        ArtifactReportConfiguration config = new ArtifactReportConfiguration( project, "107",
           true, "target/test-classes/org/codehaus/plexus/artifact/docck-successful.txt",
           false, "target/test-classes/org/codehaus/plexus/artifact/license-failed.txt" );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream result = new PrintStream( baos );

        report.generate( config, result );

        result.close();

        String actual = new String( baos.toByteArray() );

        ClassLoader classLoader = this.getClass().getClassLoader();
        String expectedFile = "org/codehaus/plexus/artifact/ExpectedResult.txt";
        URL resource = classLoader.getResource( expectedFile );
        String expected = Utils.streamToString( resource.openStream() );

        assertEquals( expected, actual );

    }

}
