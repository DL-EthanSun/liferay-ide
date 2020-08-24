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

package com.liferay.ide.upgrade.plan.core.internal;

import com.liferay.ide.core.ILiferayProjectProvider;
import com.liferay.ide.core.util.FileUtil;
import com.liferay.ide.core.util.ListUtil;
import com.liferay.ide.core.util.SapphireContentAccessor;
import com.liferay.ide.core.util.SapphireUtil;
import com.liferay.ide.core.util.ZipUtil;
import com.liferay.ide.upgrade.plan.core.IUpgradePlanOutline;
import com.liferay.ide.upgrade.plan.core.UpgradePlanCorePlugin;
import com.liferay.ide.upgrade.plan.core.UpgradePlanOutline;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.nio.file.Files;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.sapphire.PossibleValuesService;

import org.osgi.framework.Bundle;

/**
 * @author Terry Jia
 * @author Simon Jiang
 * @author Ethan Sun
 */
public class OutlinePossibleValuesService extends PossibleValuesService implements SapphireContentAccessor {

	public static final String CODE_UPGRADE = "code-upgrade";

	public static final String CODE_UPGRADE_ZIP_MD5 = "code-upgrade-zip-md5";

	public static final String DATABASE_UPGRADE = "database-upgrade";

	public static final String DATABASE_UPGRADE_ZIP_MD5 = "database-upgrade-zip-md5";

	public static final String OFFLINE_UNZIP_FOLDER = "offline-outline";

	@Override
	protected void compute(Set<String> values) {
		List<String> possibleValues = _offlineOutlines.stream(
		).map(
			IUpgradePlanOutline::getName
		).sorted(
		).collect(
			Collectors.toList()
		);

		values.addAll(possibleValues);
	}

	@Override
	protected void initPossibleValuesService() {
		Job releaseResource = new Job("Release doc resource") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				_releaseResource();
				
				refresh();

				return Status.OK_STATUS;
			}

		};

		releaseResource.setProperty(ILiferayProjectProvider.LIFERAY_PROJECT_JOB, new Object());

		releaseResource.setSystem(true);

		releaseResource.schedule();
	}

	private static String _computeMD5(File file) {
		if (Objects.nonNull(file)) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");

				md5.update(Files.readAllBytes(file.toPath()));

				return DatatypeConverter.printHexBinary(md5.digest());
			}
			catch (IOException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private void _releaseResource() {
		IPreferencesService preferencesService = Platform.getPreferencesService();

		IPath pluginStateLocation = _instance.getStateLocation();

		IPath offlineOutlinePath = pluginStateLocation.append(OFFLINE_UNZIP_FOLDER);

		Bundle bundle = Platform.getBundle(UpgradePlanCorePlugin.ID);

		Enumeration<URL> entryUrls = bundle.findEntries("resources/", "*.zip", true);

		if (ListUtil.isEmpty(entryUrls)) {
			return;
		}

		while (entryUrls.hasMoreElements()) {
			try {
				URL fileURL = FileLocator.toFileURL(entryUrls.nextElement());

				File outlineFile = new File(fileURL.getFile());

				String outline = "";

				String contentZipMD5 = "";

				switch (outlineFile.getName()) {
					case "code-upgrade.zip":
						outline = CODE_UPGRADE;
						contentZipMD5 = CODE_UPGRADE_ZIP_MD5;

						break;

					case "database-upgrade.zip":
						outline = DATABASE_UPGRADE;
						contentZipMD5 = DATABASE_UPGRADE_ZIP_MD5;

						break;
				}

				String outlineFilename = outlineFile.getName();

				String outlineFilenameWithoutEx = outlineFilename.split("\\.")[0];

				String storedMD5 = preferencesService.getString(UpgradePlanCorePlugin.ID, contentZipMD5, "", null);

				String updateMD5 = _computeMD5(outlineFile);

				IPath offlineDocDirPath = offlineOutlinePath.append(outlineFilenameWithoutEx);

				UpgradePlanOutline upgradePlanOutline = new UpgradePlanOutline(
					outlineFilenameWithoutEx, offlineDocDirPath.toOSString());
				
				_offlineOutlines.clear();

				if (!updateMD5.equals(storedMD5) || FileUtil.notExists(offlineDocDirPath)) {
					FileUtil.deleteDir(offlineDocDirPath.toFile(), true);

					ZipUtil.unzip(outlineFile, offlineOutlinePath.toFile());

					_prefstore.put(outline, upgradePlanOutline.toString());

					_prefstore.put(contentZipMD5, updateMD5);

					_prefstore.flush();
				}

				_offlineOutlines.add(upgradePlanOutline);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static UpgradePlanCorePlugin _instance = UpgradePlanCorePlugin.getInstance();

	private static final List<IUpgradePlanOutline> _offlineOutlines = new ArrayList<>();

	private IEclipsePreferences _prefstore = InstanceScope.INSTANCE.getNode(UpgradePlanCorePlugin.ID);

}