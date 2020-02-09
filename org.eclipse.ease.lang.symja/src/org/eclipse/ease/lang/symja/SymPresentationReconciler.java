package org.eclipse.ease.lang.symja;

import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.matheclipse.core.convert.AST2Expr;

public class SymPresentationReconciler extends PresentationReconciler {

	private final TextAttribute tagAttribute = new TextAttribute(new Color(Display.getCurrent(), new RGB(0, 0, 255)));
	private final TextAttribute headerAttribute = new TextAttribute(
			new Color(Display.getCurrent(), new RGB(128, 128, 128)));

	private static final class IdentifierDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c) && c != '_';
		}

		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c) && c != '_';
		}
	}

	public SymPresentationReconciler() {
		RuleBasedScanner scanner = new RuleBasedScanner();
		IRule[] rules = new IRule[2];
//		rules[1] = new SingleLineRule("<", ">", new Token(tagAttribute));
//		rules[0] = new SingleLineRule("<?", "?>", new Token(headerAttribute));

		Token wordToken = new Token(tagAttribute);
		WordRule wordRule = new WordRule(new IdentifierDetector(), Token.UNDEFINED, true);
//		wordRule.addWord("C", wordToken);
//		wordRule.addWord("D", wordToken);
//		wordRule.addWord("E", wordToken);
//		wordRule.addWord("I", wordToken);
		SymjaEnvironementBootStrapper.initialize();
		for (Map.Entry<String, String> elem : AST2Expr.PREDEFINED_SYMBOLS_MAP.entrySet()) {
			String key = elem.getKey();
			if (key.length() > 1) {
				wordRule.addWord(key, wordToken);
			}
		}
		rules[0] = wordRule;
		WordRule shortWordRule = new WordRule(new IdentifierDetector());
		shortWordRule.addWord("C", wordToken);
		shortWordRule.addWord("D", wordToken);
		shortWordRule.addWord("E", wordToken);
		shortWordRule.addWord("I", wordToken);
		shortWordRule.addWord("N", wordToken);
		rules[1] = shortWordRule;
		scanner.setRules(rules);
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}
}