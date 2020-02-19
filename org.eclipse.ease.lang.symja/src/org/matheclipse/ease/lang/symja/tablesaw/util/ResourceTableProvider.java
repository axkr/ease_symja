package org.matheclipse.ease.lang.symja.tablesaw.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.PartInitException;
import org.matheclipse.ease.lang.symja.SymjaPlugin;
import org.matheclipse.ease.lang.symja.tablesaw.SimpleTableProvider;
import org.matheclipse.ease.lang.symja.tablesaw.io.FileFormatSupport;

import tech.tablesaw.api.Table;

public class ResourceTableProvider extends SimpleTableProvider {

    private ResourceChangeHelper resourceChangeHelper;
    
    public ResourceTableProvider(final IFile file) {
        super(load(file));
        resourceChangeHelper = new ResourceChangeHelper(path -> setTable(load(file))) {
            @Override
            protected boolean isPath(IPath path) {
                return file.getFullPath().equals(path);
            }
        };
    }

    public void dispose() {
        resourceChangeHelper.dispose();
    }

    public static boolean supportsFile(IFile file) {
        String fileFormat = file.getFileExtension();
        FileFormatSupport ffs = SymjaPlugin.getDefault().getFileFormatSupport(fileFormat);
        return (ffs != null);
    }
    
    private static Table load(final IFile file) throws RuntimeException {
        try {
            String fileFormat = file.getFileExtension();
            FileFormatSupport ffs = SymjaPlugin.getDefault().getFileFormatSupport(fileFormat);
            if (ffs == null || Boolean.FALSE.equals(ffs.supportsFormat(fileFormat))) {
                throw new PartInitException("Unsupported file format: " + file.getName());
            }
            Table[] tables = ffs.read(file.getName(), () -> {
                try {
                    return file.getContents();
                } catch (CoreException e) {
                }
                return null;
            });
            if (tables == null || tables.length == 0) {
                throw new RuntimeException("Couldn't read table from: " + file.getName());
            }
            return tables[0];
        } catch (final Exception e) {
            return null;
        }
    }    
}
