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

package com.liferay.ide.upgrade.commands.ui.internal.code;

import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.core.util.SapphireContentAccessor;
import com.liferay.ide.project.core.workspace.ImportLiferayWorkspaceOp;
import com.liferay.ide.project.ui.workspace.ImportLiferayWorkspaceWizard;
import com.liferay.ide.ui.util.UIUtil;
import com.liferay.ide.upgrade.commands.core.code.ImportExistingLiferayWorkspaceCommandKeys;
import com.liferay.ide.upgrade.commands.ui.internal.UpgradeCommandsUIPlugin;
import com.liferay.ide.upgrade.plan.core.MessagePrompt;
import com.liferay.ide.upgrade.plan.core.UpgradeCommand;
import com.liferay.ide.upgrade.plan.core.UpgradeCommandPerformedEvent;
import com.liferay.ide.upgrade.plan.core.UpgradePlan;
import com.liferay.ide.upgrade.plan.core.UpgradePlanner;

import java.nio.file.Paths;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Gregory Amerson
 * @author Terry Jia
 * @author Ethan Sun
 * @author Simon Jiang
 */
@Component(
	property = "id=" + ImportExistingLiferayWorkspaceCommandKeys.ID, scope = ServiceScope.PROTOTYPE,
	service = UpgradeCommand.class
)
public class ImportExistingLiferayWorkspaceCommand implements SapphireContentAccessor, UpgradeCommand {

	@Override
	public IStatus perform(IProgressMonitor progressMonitor) {
		UpgradePlan upgradePlan = _upgradePlanner.getCurrentUpgradePlan();

		Map<String, String> upgradeContext = upgradePlan.getUpgradeContext();

		String targetProjectLocation = upgradeContext.get("targetProjectLocation");

		if (targetProjectLocation != null) {
			boolean result = _messagePrompt.promptQuestion(
				"Target Project Location Exists",
				"The path " + targetProjectLocation +
					" already is used as target project location, do you want to override?");

			if (!result) {
				return Status.CANCEL_STATUS;
			}
		}

		ImportLiferayWorkspaceOp importLiferayWorkspaceOp = ImportLiferayWorkspaceOp.TYPE.instantiate();

		importLiferayWorkspaceOp.setShowDownloadBundle(false);

		final AtomicInteger returnCode = new AtomicInteger();

		UIUtil.sync(
			() -> {
				ImportLiferayWorkspaceWizard importLiferayWorkspaceWizard = new ImportLiferayWorkspaceWizard(
					importLiferayWorkspaceOp) {

					@Override
					protected void openLiferayPerspective(IProject newProject) {
					}

				};

				IWorkbench workbench = PlatformUI.getWorkbench();

				IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

				WizardDialog wizardDialog = new WizardDialog(workbenchWindow.getShell(), importLiferayWorkspaceWizard);

				returnCode.set(wizardDialog.open());
			});

		if (returnCode.get() == Window.OK) {
			Path workspacePath = get(importLiferayWorkspaceOp.getWorkspaceLocation());

			java.nio.file.Path path = Paths.get(workspacePath.toOSString());

			java.nio.file.Path workspaceName = path.getName(path.getNameCount() - 1);

			upgradeContext.put("targetProjectLocation", workspacePath.toString());

			_upgradePlanner.dispatch(
				new UpgradeCommandPerformedEvent(
					this, Collections.singletonList(CoreUtil.getProject(workspaceName.toString()))));

			return Status.OK_STATUS;
		}

		return UpgradeCommandsUIPlugin.createErrorStatus("Liferay Workspace was not imported.");
	}

	@Reference
	private MessagePrompt _messagePrompt;

	@Reference
	private UpgradePlanner _upgradePlanner;

}