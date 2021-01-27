package com.liferay.ide.upgrade.problems.core.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.liferay.ide.upgrade.plan.core.UpgradeProblem;
import com.liferay.ide.upgrade.problems.core.FileMigration;

/**
 * @author Ethan Sun
 */
public class LiferayUIFlashTagsTest {

	@Test
	public void findUpgradeProblems() throws Exception {
		ServiceReference<FileMigration> sr = _context.getServiceReference(FileMigration.class);

		FileMigration m = _context.getService(sr);

		List<String> versions = Arrays.asList("7.0", "7.1", "7.2", "7.3", "7.4");

		List<UpgradeProblem> problems = m.findUpgradeProblems(new File("jsptests/liferayui-flash/"), versions, new NullProgressMonitor());

		Assert.assertEquals("", 1, problems.size());

		boolean found = false;

		for (UpgradeProblem problem : problems) {
			if (problem.getResource().getName().endsWith("LiferayUIFlashTagTest.jsp")) {
				if (problem.getLineNumber() == 29) {
					Assert.assertEquals("", 1708, problem.getStartOffset());

					Assert.assertEquals("", 2205, problem.getEndOffset());

					found = true;
				}
			}
		}

		if (!found) {
			Assert.fail();
		}
	}

	private final BundleContext _context = FrameworkUtil.getBundle(getClass()).getBundleContext();

}
