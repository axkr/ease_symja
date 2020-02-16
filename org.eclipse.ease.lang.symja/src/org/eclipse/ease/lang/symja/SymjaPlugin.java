/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ease.lang.symja;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ease.lang.symja.symjaeditor.JavaPartitionScanner;
import org.eclipse.ease.lang.symja.symjaeditor.java.JavaCodeScanner;
import org.eclipse.ease.lang.symja.symjaeditor.javadoc.SymjaDocScanner;
import org.eclipse.ease.lang.symja.symjaeditor.util.SymjaColorProvider;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * The example java editor plug-in class.
 *
 * @since 3.0
 */
public class SymjaPlugin extends AbstractUIPlugin {

	public final static String JAVA_PARTITIONING= "__java_example_partitioning"; //$NON-NLS-1$

	public static final String PLUGIN_ID= "org.eclipse.ease.lang.symja"; //$NON-NLS-1$

	private static SymjaPlugin fgInstance;

	private JavaPartitionScanner fPartitionScanner;

	private SymjaColorProvider fColorProvider;

	private JavaCodeScanner fCodeScanner;

	private SymjaDocScanner fDocScanner;

	/**
	 * Creates a new plug-in instance.
	 */
	public SymjaPlugin() {
		fgInstance= this;
	}

	/**
	 * Returns the default plug-in instance.
	 *
	 * @return the default plug-in instance
	 */
	public static SymjaPlugin getDefault() {
		return fgInstance;
	}

	/**
	 * Return a scanner for creating Java partitions.
	 *
	 * @return a scanner for creating Java partitions
	 */
	public JavaPartitionScanner getJavaPartitionScanner() {
		if (fPartitionScanner == null)
			fPartitionScanner= new JavaPartitionScanner();
		return fPartitionScanner;
	}

	/**
	 * Returns the singleton Java code scanner.
	 *
	 * @return the singleton Java code scanner
	 */
	public RuleBasedScanner getJavaCodeScanner() {
		if (fCodeScanner == null)
			fCodeScanner= new JavaCodeScanner(getJavaColorProvider());
		return fCodeScanner;
	}

	/**
	 * Returns the singleton Java color provider.
	 *
	 * @return the singleton Java color provider
	 */
	public SymjaColorProvider getJavaColorProvider() {
		if (fColorProvider == null)
			fColorProvider= new SymjaColorProvider();
		return fColorProvider;
	}

	/**
	 * Returns the singleton Javadoc scanner.
	 *
	 * @return the singleton Javadoc scanner
	 */
	public RuleBasedScanner getJavaDocScanner() {
		if (fDocScanner == null)
			fDocScanner= new SymjaDocScanner(fColorProvider);
		return fDocScanner;
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, 0, "Java editor example: internal error", e)); //$NON-NLS-1$
	}

}
