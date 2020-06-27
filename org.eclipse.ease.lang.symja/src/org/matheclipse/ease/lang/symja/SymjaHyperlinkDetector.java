package org.matheclipse.ease.lang.symja;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.matheclipse.core.convert.AST2Expr;

/**
 * 
 * @deprecated
 */
public class SymjaHyperlinkDetector extends AbstractHyperlinkDetector implements IHyperlinkDetector {

//	private static final String PREFERENCES = "preferences";

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {

		IDocument document = textViewer.getDocument();
		int offset = region.getOffset();

		// extract relevant characters
		IRegion lineRegion;
		String candidate;
		try {
			lineRegion = document.getLineInformationOfOffset(offset);
			candidate = document.get(lineRegion.getOffset(), lineRegion.getLength());
		} catch (BadLocationException ex) {
			return null;
		}

		if (candidate.length() > 1) {
			String searchStr = candidate.toLowerCase();
			String keyWord = AST2Expr.PREDEFINED_SYMBOLS_MAP.get(searchStr);
			if (keyWord != null) {
				searchStr = keyWord;
				// detect region containing keyword
				IRegion targetRegion = new Region(lineRegion.getOffset(), candidate.length());
				if ((targetRegion.getOffset() <= offset)
						&& ((targetRegion.getOffset() + targetRegion.getLength()) > offset))
					// create link
					return new IHyperlink[] { new SymjaHyperlink(targetRegion, keyWord) };
			}
			return null;
		}
		// look for keyword
//		int index = candidate.indexOf(PREFERENCES);
//		if (index != -1) {
//
//			// detect region containing keyword
//			IRegion targetRegion = new Region(lineRegion.getOffset() + index, PREFERENCES.length());
//			if ((targetRegion.getOffset() <= offset)
//					&& ((targetRegion.getOffset() + targetRegion.getLength()) > offset))
//				// create link
//				return new IHyperlink[] { new SymjaHyperlink(targetRegion) };
//		}

		return null;
	}
}