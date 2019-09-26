/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ide.functional.modules.ext.files.wizard.tests;

import com.liferay.ide.functional.liferay.SwtbotBase;
import com.liferay.ide.functional.liferay.support.project.ProjectSupport;
import com.liferay.ide.functional.liferay.support.workspace.LiferayWorkspaceGradle72Support;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Rui Wang
 * @author Ashley Yuan
 */
public class NewModulesExtFilesTests extends SwtbotBase {

	@ClassRule
	public static LiferayWorkspaceGradle72Support liferayWorkspace = new LiferayWorkspaceGradle72Support(bot);

	@Test
	public void addEmptyMoudlesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.newModulesExtFiles.openFileMenuModulesExtFilesWizard();

		wizardAction.newModulesExtFiles.selectModuleExtProjectName(project.getName());

		wizardAction.finish();

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addJavaModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.newModulesExt.selectLaunchModulesExtFiles();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.newModulesExtFiles.selectModuleExtProjectName(project.getName());

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile(
			"com", "liferay", "login", "web", "internal", "constants", "LoginPortletKeys.java");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/java",
				"com.liferay.login.web.internal.constants", "LoginPortletKeys.java"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addJSPModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.newModulesExtFiles.openFileMenuModulesExtFilesWizard();

		wizardAction.newModulesExtFiles.selectModuleExtProjectName(project.getName());

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile("META-INF", "resources", "configuration.jsp");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/resources", "META-INF", "resources",
				"configuration.jsp"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addLanguagePropertiesModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		viewAction.project.openProjectNewModuleExtFilesWizard(liferayWorkspace.getName(), project.getName());

		wizardAction.newModulesExtFiles.selectModuleExtProjectName(project.getName());

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile("content", "Language.properties");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/resources", "content",
				"Language.properties"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addManifestModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.newModulesExt.selectLaunchModulesExtFiles();

		wizardAction.finish();

		jobAction.waitForNoRunningJobs();

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile("META-INF", "MANIFEST.MF");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/resources", "META-INF", "MANIFEST.MF"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addPortletPropertiesModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.newModulesExt.selectLaunchModulesExtFiles();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile("portlet.properties");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/resources", "portlet.properties"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Test
	public void addResourceActionModulesExtFiles() {
		wizardAction.openNewLiferayModulesExtWizard();

		wizardAction.newModulesExt.prepare(project.getName());

		wizardAction.newModulesExt.openSelectBrowseDialog();

		dialogAction.prepareText("com.liferay:com.liferay.login.web");

		dialogAction.confirm();

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.newModulesExtFiles.openFileMenuModulesExtFilesWizard();

		wizardAction.newModulesExtFiles.selectModuleExtProjectName(project.getName());

		wizardAction.newModulesExt.openAddOriginMoudleDialog();

		dialogAction.selectOverrideFile("resource-actions", "default.xml");

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		Assert.assertTrue(
			viewAction.project.visibleFileTry(
				liferayWorkspace.getName(), "ext", project.getName(), "src/main/resources", "resource-actions",
				"default.xml"));

		viewAction.project.closeAndDeleteFromDisk(liferayWorkspace.getName(), "ext");
	}

	@Rule
	public ProjectSupport project = new ProjectSupport(bot);

}