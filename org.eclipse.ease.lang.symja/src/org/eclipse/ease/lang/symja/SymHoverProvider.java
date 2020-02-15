package org.eclipse.ease.lang.symja;

import java.util.Collections;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.form.Documentation;

public class SymHoverProvider implements ITextHover, ITextHoverExtension {

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
//				return buf.toString();
				// TODO <- display as HTML
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
		Set<Extension> markdownExtensions = Collections.singleton(TablesExtension.create());
		Parser parser = Parser.builder().extensions(markdownExtensions).build();
		Node document = parser.parse(markdownStr);
		HtmlRenderer renderer = HtmlRenderer.builder().extensions(markdownExtensions).build();
		String html = renderer.render(document);
//		System.out.println(html);
		// TODO improve this hack
		html = html.replace("<blockquote>", "<pre>");
		html = html.replace("</blockquote>", "</pre>");
		return html;
	}

	/**
	 * See: <a href="https://stackoverflow.com/a/42977366/24819">StackOverflow -
	 * HTML tags in Eclipse editor hover</a>
	 */
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
			}
		};
	}
}