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
package org.eclipse.ease.lang.symja.symjaeditor;

import org.eclipse.ease.lang.symja.SymHoverProvider;
import org.eclipse.ease.lang.symja.SymjaPlugin;
import org.eclipse.ease.lang.symja.symjaeditor.symja.SymjaAutoIndentStrategy;
import org.eclipse.ease.lang.symja.symjaeditor.symja.SymjaCompletionProcessor;
import org.eclipse.ease.lang.symja.symjaeditor.symja.SymjaDoubleClickSelector;
import org.eclipse.ease.lang.symja.symjaeditor.symjadoc.SymjaDocCompletionProcessor;
import org.eclipse.ease.lang.symja.symjaeditor.util.SymjaColorProvider;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;

/**
 * Example configuration for an <code>SourceViewer</code> which shows Java code.
 */
public class SymjaSourceViewerConfiguration extends SourceViewerConfiguration {

	/**
	 * Single token scanner.
	 */
	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}

	/**
	 * Default constructor.
	 */
	public SymjaSourceViewerConfiguration() {
	}

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new SymjaAnnotationHover();
	}

	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy strategy = (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new SymjaAutoIndentStrategy()
				: new DefaultIndentLineAutoEditStrategy());
		return new IAutoEditStrategy[] { strategy };
	}

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return SymjaPlugin.JAVA_PARTITIONING;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, SymjaPartitionScanner.JAVA_DOC,
				SymjaPartitionScanner.JAVA_MULTILINE_COMMENT };
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant = new ContentAssistant();
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		assistant.setContentAssistProcessor(new SymjaCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new SymjaDocCompletionProcessor(), SymjaPartitionScanner.JAVA_DOC);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setContextInformationPopupBackground(
				SymjaPlugin.getDefault().getJavaColorProvider().getColor(new RGB(150, 150, 0)));

		return assistant;
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		return new SymjaDoubleClickSelector();
	}

	@Override
	public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
		return new String[] { "\t", "    " }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

		SymjaColorProvider provider = SymjaPlugin.getDefault().getJavaColorProvider();
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(SymjaPlugin.getDefault().getJavaCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(SymjaPlugin.getDefault().getJavaDocScanner());
		reconciler.setDamager(dr, SymjaPartitionScanner.JAVA_DOC);
		reconciler.setRepairer(dr, SymjaPartitionScanner.JAVA_DOC);

		dr = new DefaultDamagerRepairer(
				new SingleTokenScanner(new TextAttribute(provider.getColor(SymjaColorProvider.MULTI_LINE_COMMENT))));
		reconciler.setDamager(dr, SymjaPartitionScanner.JAVA_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, SymjaPartitionScanner.JAVA_MULTILINE_COMMENT);

		return reconciler;
	}

	@Override
	public int getTabWidth(ISourceViewer sourceViewer) {
		return 4;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		return new SymHoverProvider();
	}
}
