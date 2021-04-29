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

package com.liferay.ide.gradle.action;

import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.core.util.ListUtil;
import com.liferay.ide.gradle.core.GradleUtil;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Lovett Li
 * @author Terry Jia
 * @author Andy Wu
 * @author Ethan Sun
 */
public class BuildServiceTaskAction extends GradleTaskAction {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		if (fSelection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)fSelection;

			Object[] elems = structuredSelection.toArray();

			if (ListUtil.isNotEmpty(elems)) {
				Object elem = elems[0];

				if (elem instanceof IProject) {
					IContainer container = (IContainer)elem;

					action.setEnabled(container.findMember("service.xml") != null);
				}
			}
		}
	}

	protected void afterAction() {
		boolean refresh = false;

		IProject[] projects = CoreUtil.getClasspathProjects(project);

		for (IProject project : projects) {
			List<IFolder> folders = CoreUtil.getSourceFolders(JavaCore.create(project));

			if (ListUtil.isEmpty(folders)) {
				refresh = true;
			}
			else {
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
				catch (CoreException ce) {
				}
			}
		}

		List<IFolder> folders = CoreUtil.getSourceFolders(JavaCore.create(project));

		if (ListUtil.isEmpty(folders) || refresh) {
			GradleUtil.refreshProject(project);
		}
		else {
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
			catch (CoreException ce) {
			}
		}
	}

	@Override
	protected String getGradleTaskName() {
		return "buildService";
	}

}