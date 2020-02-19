/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
package org.matheclipse.ease.lang.symja.symjaeditor.symja;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.ease.lang.symja.SymjaPlugin;
import org.matheclipse.ease.lang.symja.symjaeditor.util.SymjaColorProvider;
import org.matheclipse.ease.lang.symja.symjaeditor.util.SymjaWhitespaceDetector;
import org.matheclipse.ease.lang.symja.symjaeditor.util.SymjaWordDetector;

/**
 * A Java code scanner.
 */
public class SymjaCodeScanner extends RuleBasedScanner {

//	private static String[] fgKeywords = { "abstract", "break", "case", "catch", "class", "continue", "default", "do", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
//			"else", "extends", "final", "finally", "for", "if", "implements", "import", "instanceof", "interface", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
//			"native", "new", "package", "private", "protected", "public", "return", "static", "super", "switch", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
//			"synchronized", "this", "throw", "throws", "transient", "try", "volatile", "while" }; //$NON-NLS-8$ //$NON-NLS-7$ //$NON-NLS-6$ //$NON-NLS-5$ //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
//
//	private static String[] fgTypes = { "void", "boolean", "char", "byte", "short", "int", "long", "float", "double" }; //$NON-NLS-1$ //$NON-NLS-5$ //$NON-NLS-7$ //$NON-NLS-6$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-2$
//
//	private static String[] fgConstants = { "false", "null", "true" }; //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$

	/**
	 * Creates a Java code scanner with the given color provider.
	 *
	 * @param provider the color provider
	 */
	public SymjaCodeScanner(SymjaColorProvider provider) {

		IToken keyword = new Token(new TextAttribute(provider.getColor(SymjaColorProvider.KEYWORD)));
//		IToken type= new Token(new TextAttribute(provider.getColor(SymjaColorProvider.TYPE)));
		IToken string = new Token(new TextAttribute(provider.getColor(SymjaColorProvider.STRING)));
//		IToken comment= new Token(new TextAttribute(provider.getColor(SymjaColorProvider.SINGLE_LINE_COMMENT)));
		IToken other= new Token(new TextAttribute(provider.getColor(SymjaColorProvider.DEFAULT)));

		List<IRule> rules = new ArrayList<>();

		// Add rule for single line comments.
//		rules.add(new EndOfLineRule("//", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		rules.add(new MultiLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
//		rules.add(new SingleLineRule("'", "'", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new SymjaWhitespaceDetector()));

		// Add word rule for keywords, types, and constants.
		WordRule wordRule = new WordRule(new SymjaWordDetector(), other, true);
//		for (String fgKeyword : fgKeywords) {
//			wordRule.addWord(fgKeyword, keyword);
//		}
//		for (String fgType : fgTypes) {
//			wordRule.addWord(fgType, type);
//		}
//		for (String fgConstant : fgConstants) {
//			wordRule.addWord(fgConstant, type);
//		}

		SymjaPlugin.initialize();
		for (Map.Entry<String, String> elem : AST2Expr.PREDEFINED_SYMBOLS_MAP.entrySet()) {
			String key = elem.getKey();
			if (key.length() > 1) {
				wordRule.addWord(key, keyword);
			}
		}
		rules.add(wordRule);

		WordRule shortWordRule = new WordRule(new SymjaWordDetector(), other, true);
		shortWordRule.addWord("C", keyword);
		shortWordRule.addWord("D", keyword);
		shortWordRule.addWord("E", keyword);
		shortWordRule.addWord("I", keyword);
		shortWordRule.addWord("N", keyword);
		rules.add(shortWordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
