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
package org.eclipse.ease.lang.symja.symjaeditor.symja;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.ease.lang.symja.SymjaPlugin;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.matheclipse.core.builtin.IOFunctions;
import org.matheclipse.core.convert.AST2Expr;

/**
 * Example Java completion processor.
 */
public class SymjaCompletionProcessor implements IContentAssistProcessor {

	/**
	 * Simple content assist tip closer. The tip is valid in a range of 5 characters
	 * around its popup location.
	 */
	protected static class Validator implements IContextInformationValidator, IContextInformationPresenter {

		protected int fInstallOffset;

		@Override
		public boolean isContextInformationValid(int offset) {
			return Math.abs(fInstallOffset - offset) < 5;
		}

		@Override
		public void install(IContextInformation info, ITextViewer viewer, int offset) {
			fInstallOffset = offset;
		}

		@Override
		public boolean updatePresentation(int documentPosition, TextPresentation presentation) {
			return false;
		}
	}

//	protected final static String[] fgProposals = { "abstract", "boolean", "break", "byte", "case", "catch", "char", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
//			"class", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
//			"for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
//			"package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
//			"this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while" }; //$NON-NLS-9$ //$NON-NLS-8$ //$NON-NLS-7$ //$NON-NLS-6$ //$NON-NLS-5$ //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$

	protected IContextInformationValidator fValidator = new Validator();

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		String text = viewer.getDocument().get();
		if (text.length() >= 1 && documentOffset <= text.length()) {
			String searchStr;
			int indx = documentOffset - 1;
			while (indx >= 0) {
				if (Character.isJavaIdentifierPart(text.charAt(indx))) {
					indx--;
				} else {
					break;
				}
			}
			indx++;

			searchStr = text.substring(indx, documentOffset).toLowerCase();
			// System.out.println(searchStr);
			List<String> list = IOFunctions.getAutoCompletionList(searchStr);
			if (list.size() > 0) {
				SymjaPlugin.initialize();
				ICompletionProposal[] proposals = new ICompletionProposal[list.size()];
				for (int i = 0; i < list.size(); i++) {
					String str = list.get(i);
					String symbol = AST2Expr.PREDEFINED_SYMBOLS_MAP.get(str);
					if (symbol != null) {
						str = symbol;
					}
					proposals[i] = new CompletionProposal(str, indx, documentOffset - indx, str.length());
				}
				return proposals;
			}
		}
		return new ICompletionProposal[0];
//		ICompletionProposal[] result = new ICompletionProposal[fgProposals.length];
//		for (int i = 0; i < fgProposals.length; i++) {
//			IContextInformation info = new ContextInformation(fgProposals[i],
//					MessageFormat.format(
//							SymjaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), //$NON-NLS-1$
//							new Object[] { fgProposals[i] }));
//			result[i] = new CompletionProposal(fgProposals[i], documentOffset, 0, fgProposals[i].length(), null,
//					fgProposals[i], info,
//					MessageFormat.format(SymjaEditorMessages.getString("CompletionProcessor.Proposal.hoverinfo.pattern"), //$NON-NLS-1$
//							new Object[] { fgProposals[i] }));
//		}
//		return result;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		IContextInformation[] result = new IContextInformation[5];
		for (int i = 0; i < result.length; i++)
			result[i] = new ContextInformation(
					MessageFormat.format(
							SymjaEditorMessages.getString("CompletionProcessor.ContextInfo.display.pattern"), //$NON-NLS-1$
							new Object[] { Integer.valueOf(i), Integer.valueOf(documentOffset) }),
					MessageFormat.format(SymjaEditorMessages.getString("CompletionProcessor.ContextInfo.value.pattern"), //$NON-NLS-1$
							new Object[] { Integer.valueOf(i), Integer.valueOf(documentOffset - 5),
									Integer.valueOf(documentOffset + 5) }));
		return result;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.', '(' };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return new char[] { '#' };
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return fValidator;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}
}
