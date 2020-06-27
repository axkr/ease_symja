package org.matheclipse.ease.lang.symja;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * 
 * 
 * @deprecated
 */
public class SymjaHyperlink implements IHyperlink {

	private final IRegion fUrlRegion;
	String function;

	public SymjaHyperlink(IRegion urlRegion, String function) {
		fUrlRegion = urlRegion;
		this.function= function;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return fUrlRegion;
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public String getHyperlinkText() {
		return null;
	}

	@Override
	public void open() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(
						"https://github.com/axkr/symja_android_library/tree/master/symja_android_library/doc/functions/"
								+ function));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(), null, null, null).open();
	}
}