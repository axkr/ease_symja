package org.eclipse.ease.lang.symja;

import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.matheclipse.core.builtin.IOFunctions;
import org.matheclipse.core.convert.AST2Expr;

public class SymContentAssistProcessor implements IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		String text = viewer.getDocument().get();
		if (text.length() >= 1 && offset <= text.length()) {
			String searchStr;
			int indx = offset - 1;
			while (indx >= 0) {
				if (Character.isJavaIdentifierPart(text.charAt(indx))) {
					indx--;
				} else {
					break;
				}
			}
			indx++;
			if (indx < offset && indx >= 0) {
				searchStr = text.substring(indx, offset).toLowerCase();
				// System.out.println(searchStr);
				List<String> list = IOFunctions.getAutoCompletionList(searchStr);
				if (list.size() > 0) {
					SymjaReplScriptEngine.initialize();
					ICompletionProposal[] proposals = new ICompletionProposal[list.size()];
					for (int i = 0; i < list.size(); i++) {
						String str = list.get(i);
						String symbol = AST2Expr.PREDEFINED_SYMBOLS_MAP.get(str);
						if (symbol != null) {
							str = symbol;
						}
						proposals[i] = new CompletionProposal(str, indx, offset - indx, str.length());
					}
					return proposals;
				}
			}
		}
		return new ICompletionProposal[0];
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '"' }; // NON-NLS-1
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}