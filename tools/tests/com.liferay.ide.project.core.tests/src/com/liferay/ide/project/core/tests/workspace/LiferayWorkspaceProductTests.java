package com.liferay.ide.project.core.tests.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.junit.Test;

import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.core.util.PropertiesUtil;
import com.liferay.ide.project.core.tests.ProjectCoreBase;
import com.liferay.ide.project.core.workspace.NewLiferayWorkspaceOp;
import com.liferay.ide.project.core.workspace.NewLiferayWorkspaceOpMethods;

public class LiferayWorkspaceProductTests  extends ProjectCoreBase {
	
	@Test
    public void testSwitchProductCategory() throws Exception
    {
        NewLiferayWorkspaceOp op = NewLiferayWorkspaceOp.TYPE.instantiate();
        
        waitForBuildAndValidation();
        
        op.setWorkspaceName( "test-gradle-liferay-workspace" );
        op.setUseDefaultLocation( true );
        
        assertTrue("portal".equals(op.getProductCategory().content()));
        assertTrue("portal-7.3-ga3".equals(op.getProductVersion().content()));
        
        op.setProductCategory("dxp");
        waitForBuildAndValidation();
        
        assertTrue("dxp".equals(op.getProductCategory().content()));
        assertTrue("dxp-7.2-sp1".equals(op.getProductVersion().content()));
        
        op.setProductCategory("commerce");
        waitForBuildAndValidation();
        
        assertTrue("commerce".equals(op.getProductCategory().content()));
        assertTrue("commerce-2.0.7-7.2".equals(op.getProductVersion().content()));

        if( op.validation().ok() )
        {
            NewLiferayWorkspaceOpMethods.execute( op, ProgressMonitorBridge.create( new NullProgressMonitor() ) );
        }

        waitForBuildAndValidation();
        
        IProject workspaceProject = CoreUtil.getProject( "test-gradle-liferay-workspace" );

        assertTrue(workspaceProject != null);
        assertTrue(workspaceProject.exists());
        
        String workspaceLocation = workspaceProject.getLocation().toPortableString();
        
        File propertiesFile = new File(workspaceLocation+"/gradle.properties");

        Properties prop = PropertiesUtil.loadProperties( propertiesFile);
        
        assertTrue("commerce-2.0.7-7.2".equals(prop.getProperty("")));
        
        workspaceProject.delete(true, true, new NullProgressMonitor());
    }
	
	@Test
    public void testSwitchProductVersion() throws Exception
    {
        NewLiferayWorkspaceOp op = NewLiferayWorkspaceOp.TYPE.instantiate();
        
        waitForBuildAndValidation();
        
        op.setWorkspaceName( "test-gradle-liferay-workspace" );
        op.setUseDefaultLocation( true );
        
        assertTrue("portal".equals(op.getProductCategory().content()));
        assertTrue("portal-7.3-ga3".equals(op.getProductVersion().content()));
        
        op.setProductVersion("portal-7.2-ga2");
        waitForBuildAndValidation();
        
        assertTrue("portal-7.2-ga2".equals(op.getProductVersion().content()));
        
        op.setProductCategory("dxp");
        
        waitForBuildAndValidation();
        
        assertTrue("dxp-7.2-sp1".equals(op.getProductVersion().content()));
        
        op.setProductVersion("dxp-7.0-sp13");
        
        waitForBuildAndValidation();
        
        assertTrue("dxp-7.0-sp13".equals(op.getProductVersion().content()));

        if( op.validation().ok() )
        {
            NewLiferayWorkspaceOpMethods.execute( op, ProgressMonitorBridge.create( new NullProgressMonitor() ) );
        }

        waitForBuildAndValidation();
        
        IProject workspaceProject = CoreUtil.getProject( "test-gradle-liferay-workspace" );

        assertTrue(workspaceProject != null);
        assertTrue(workspaceProject.exists());
        
        String workspaceLocation = workspaceProject.getLocation().toPortableString();
        
        File propertiesFile = new File(workspaceLocation+"/gradle.properties");

        Properties prop = PropertiesUtil.loadProperties( propertiesFile);
        
        assertTrue("dxp-7.0-sp13".equals(prop.getProperty("")));
        
        workspaceProject.delete(true, true, new NullProgressMonitor());
    }
	
	@Test
    public void testShowAllProductVersion() throws Exception
    {
        NewLiferayWorkspaceOp op = NewLiferayWorkspaceOp.TYPE.instantiate();
        
        waitForBuildAndValidation();
        
        op.setWorkspaceName( "test-gradle-liferay-workspace" );
        op.setUseDefaultLocation( true );
        
        op.setShowAllVersionProduct( true );
        
        waitForBuildAndValidation();
        
        op.setProductVersion("portal-7.1-ga3");
        
        String message = op.validation().message();

        assertNotNull( message );

        assertEquals( "Product Version can not be empty.", message );
        
        op.setProductCategory("dxp");
        
        waitForBuildAndValidation();
        
        op.setProductVersion("dxp-7.0-sp10");
        
        message = op.validation().message();

        assertNotNull( message );

        assertEquals( "Product Version can not be empty.", message );
        
        op.setProductCategory("commerce");
        
        waitForBuildAndValidation();
        
        op.setProductVersion("commerce-1.1.4");
        
        message = op.validation().message();

        assertNotNull( message );

        assertEquals( "Product Version can not be empty.", message );
        
        if( op.validation().ok() )
        {
            NewLiferayWorkspaceOpMethods.execute( op, ProgressMonitorBridge.create( new NullProgressMonitor() ) );
        }

        waitForBuildAndValidation();
        
        IProject workspaceProject = CoreUtil.getProject( "test-gradle-liferay-workspace" );

        assertTrue(workspaceProject != null);
        assertTrue(workspaceProject.exists());
        
        String workspaceLocation = workspaceProject.getLocation().toPortableString();
        
        File propertiesFile = new File(workspaceLocation+"/gradle.properties");

        Properties prop = PropertiesUtil.loadProperties( propertiesFile);
        
        assertTrue("commerce-1.1.4".equals(prop.getProperty("")));
        
        workspaceProject.delete(true, true, new NullProgressMonitor());
    }

}
