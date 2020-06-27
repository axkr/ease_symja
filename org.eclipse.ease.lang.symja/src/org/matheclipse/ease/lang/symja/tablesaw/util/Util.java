package org.matheclipse.ease.lang.symja.tablesaw.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

public class Util {
    public static ImageDescriptor imageFromPlugin(String pluginId, String path) {
        try {
            return ImageDescriptor.createFromURL(new URL("platform:/plugin/" + pluginId + path));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }   
    }
}
