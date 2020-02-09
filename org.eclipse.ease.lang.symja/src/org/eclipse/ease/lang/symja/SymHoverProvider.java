package org.eclipse.ease.lang.symja;

import java.util.Collections;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.form.Documentation;

public class SymHoverProvider implements ITextHover {

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {

		String text = textViewer.getDocument().get();
		int offset = hoverRegion.getOffset();
		if (text.length() >= 1 && offset <= text.length()) {
			String searchStr;
			int startIndex = offset - 1;
			while (startIndex >= 0) {
				if (Character.isJavaIdentifierPart(text.charAt(startIndex))) {
					startIndex--;
				} else {
					break;
				}
			}
			startIndex++;
			int endIndex = offset;
			while (endIndex < text.length()) {
				if (Character.isJavaIdentifierPart(text.charAt(endIndex))) {
					endIndex++;
				} else {
					break;
				}
			}
			if (startIndex < endIndex && startIndex >= 0) {
				SymjaEnvironementBootStrapper.initialize();
				searchStr = text.substring(startIndex, endIndex);
				if (searchStr.length() > 1) {
					searchStr = searchStr.toLowerCase();
					String keyWord = AST2Expr.PREDEFINED_SYMBOLS_MAP.get(searchStr);
					if (keyWord != null) {
						searchStr = keyWord;
					}
				}
				StringBuilder buf = new StringBuilder();
				Documentation.printDocumentation(buf, searchStr);
				return generateHTMLString(buf.toString());
			}
		}
		return "";
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		String text = textViewer.getDocument().get();
		if (text.length() >= 1 && offset <= text.length()) {
			String searchStr;
			int startIndex = offset - 1;
			while (startIndex >= 0) {
				if (Character.isJavaIdentifierPart(text.charAt(startIndex))) {
					startIndex--;
				} else {
					break;
				}
			}
			startIndex++;
			int endIndex = offset;
			while (endIndex < text.length()) {
				if (Character.isJavaIdentifierPart(text.charAt(endIndex))) {
					endIndex++;
				} else {
					break;
				}
			}
			if (startIndex < endIndex && startIndex >= 0) {
				SymjaEnvironementBootStrapper.initialize();
				searchStr = text.substring(startIndex, endIndex).toLowerCase();
				String keyWord = AST2Expr.PREDEFINED_SYMBOLS_MAP.get(searchStr);
				if (keyWord != null) {
					return new Region(startIndex, keyWord.length());
				}
			}
		}
		return new Region(offset, 0);
	}

	public static String generateHTMLString(final String markdownStr) {
		Set<Extension> EXTENSIONS = Collections.singleton(TablesExtension.create());
		Parser parser = Parser.builder().extensions(EXTENSIONS).build();
		Node document = parser.parse(markdownStr);
		HtmlRenderer renderer = HtmlRenderer.builder().extensions(EXTENSIONS).build();
		return renderer.render(document);
	}
}