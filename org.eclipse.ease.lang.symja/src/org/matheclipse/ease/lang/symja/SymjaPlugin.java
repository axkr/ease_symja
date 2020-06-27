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
package org.matheclipse.ease.lang.symja;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.expression.F;
import org.matheclipse.ease.lang.symja.symjaeditor.SymjaPartitionScanner;
import org.matheclipse.ease.lang.symja.symjaeditor.symja.SymjaCodeScanner;
import org.matheclipse.ease.lang.symja.symjaeditor.symjadoc.SymjaDocScanner;
import org.matheclipse.ease.lang.symja.symjaeditor.util.SymjaColorProvider;
import org.matheclipse.ease.lang.symja.tablesaw.TableProviderRegistry;
import org.matheclipse.ease.lang.symja.tablesaw.io.FileFormatSupport;
import org.matheclipse.parser.client.FEConfig;
import org.osgi.framework.BundleContext;

import tech.tablesaw.api.Table;


/**
 * The Symja editor plug-in class.
 *
 */
public class SymjaPlugin extends AbstractUIPlugin {

	public final static String JAVA_PARTITIONING= "__java_example_partitioning"; //$NON-NLS-1$

	public static final String PLUGIN_ID= "org.matheclipse.ease.lang.symja"; //$NON-NLS-1$

	private static SymjaPlugin fgInstance;

	private SymjaPartitionScanner fPartitionScanner;

	private SymjaColorProvider fColorProvider;

	private SymjaCodeScanner fCodeScanner;

	private SymjaDocScanner fDocScanner;

	/**
	 * Creates a new plug-in instance.
	 */
	public SymjaPlugin() {
		fgInstance= this;
		initialize();
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
	public SymjaPartitionScanner getJavaPartitionScanner() {
		if (fPartitionScanner == null)
			fPartitionScanner= new SymjaPartitionScanner();
		return fPartitionScanner;
	}

	/**
	 * Returns the singleton Java code scanner.
	 *
	 * @return the singleton Java code scanner
	 */
	public RuleBasedScanner getJavaCodeScanner() {
		if (fCodeScanner == null)
			fCodeScanner= new SymjaCodeScanner(getJavaColorProvider());
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

	public static void initialize() {
		FEConfig.PARSER_USE_LOWERCASE_SYMBOLS = true;
		Config.USE_VISJS = true;
		Config.FILESYSTEM_ENABLED = true;
		F.initSymbols(null, null, true);
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, 0, "Java editor example: internal error", e)); //$NON-NLS-1$
	}
	

	@Override
	public void start(final BundleContext context) throws Exception {
		fgInstance = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		fgInstance = null;
		tableProviderRegistry.clear();
	}

	private final TableProviderRegistry tableProviderRegistry = new TableProviderRegistry();

	public TableProviderRegistry getTableProviderRegistry() {
		return tableProviderRegistry;
	}

	//
	
//	private Collection<ExprSupport> exprSupports = null;
//
//	public ExprSupport[] getExprSupports() {
//		if (exprSupports == null) {
//			exprSupports = new ArrayList<ExprSupport>();
//			processExprSupportExtensions();
//		}
//		return exprSupports.toArray(new ExprSupport[exprSupports.size()]);
//	}
//
//	public ExprSupport getExprSupport(String... names) {
//	    for (String name : names) {
//	        ExprSupport exprSupport = getExprSupport(name);
//	        if (exprSupport != null) {
//	            return exprSupport;
//	        }
//	    }
//	    return null;
//	}
//
//	public ExprSupport getExprSupport(String name) {
//	    for (ExprSupport exprSupport : getExprSupports()) {
//	        if ("*".equals(name) || exprSupport.getLang().equals(name)) {
//	            return exprSupport;
//	        }
//	    }
//	    return null;
//	}

//	private void processExprSupportExtensions() {
//		final IExtensionPoint ep = Platform.getExtensionRegistry().getExtensionPoint("org.matheclipse.ease.lang.symja.tablesaw.ui.exprSupport");
//		for (final IExtension extension : ep.getExtensions()) {
//			for (final IConfigurationElement ces : extension.getConfigurationElements()) {
//				if ("exprSupport".equals(ces.getName())) {
//					try {
//						final ExprSupport es = (ExprSupport) ces.createExecutableExtension("supportClass");
//						es.setLang(ces.getAttribute("langName"));
//						exprSupports.add(es);
//					} catch (final CoreException e) {
//					}
//				}
//			}
//		}
//	}
	
	//

	private final static FileFormatSupport unsupportedFileFormat = new FileFormatSupport() {
	    @Override
	    public Boolean supportsFormat(String format) {
	        return false;
	    }
	    @Override
	    public Table[] read(String name, Supplier<InputStream> input) throws IOException {
	        throw new UnsupportedOperationException("Write is not supported");
	    }
	    @Override
	    public void write(Table[] tables, String name, OutputStream output) throws IOException {
	        throw new UnsupportedOperationException("Write is not supported");
	    }
	};

	private boolean readAllAtOnce = false;
	private Map<String, FileFormatSupport> fileFormatSupports = null;
	
	public FileFormatSupport getFileFormatSupport(String key) {
	    if (fileFormatSupports == null) {
	        fileFormatSupports = new HashMap<String, FileFormatSupport>();
	        if (readAllAtOnce) {
	            processFileFormatSupportExtensions(null);	            
	        }
	    }
	    if ((! readAllAtOnce) && (! fileFormatSupports.containsKey(key))) {
    	    if (! processFileFormatSupportExtensions(key)) {
    	        fileFormatSupports.put(key, unsupportedFileFormat);
    	    }
    	}
	    return fileFormatSupports.get(key);
	}

	private boolean processFileFormatSupportExtensions(String key) {
	    final IExtensionPoint ep = Platform.getExtensionRegistry().getExtensionPoint("org.matheclipse.ease.lang.symja.fileFormatSupport");
	    for (final IExtension extension : ep.getExtensions()) {
	        for (final IConfigurationElement ces : extension.getConfigurationElements()) {
	            if ("fileFormatSupport".equals(ces.getName())) {
	                try {
	                    FileFormatSupport ffs = null;
	                    for (String fileFormat : ces.getAttribute("fileFormats").split(",\\s*")) {
	                        if (key == null || key.equals(fileFormat)) {
	                            if (ffs == null) {
	                                ffs = (FileFormatSupport) ces.createExecutableExtension("supportClass");
	                            }
	                            fileFormatSupports.put(fileFormat, ffs);
	                            if (key != null) {
	                                return true;
	                            }
	                        }
	                    }
	                } catch (final Exception e) {
	                }
	            }
	        }
	    }
	    return false;
	}

}
