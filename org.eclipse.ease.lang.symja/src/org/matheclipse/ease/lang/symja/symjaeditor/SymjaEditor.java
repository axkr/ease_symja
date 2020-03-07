/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
package org.matheclipse.ease.lang.symja.symjaeditor;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.matheclipse.core.eval.EvalControlledCallable;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IStringX;

/**
 * Java specific text editor.
 */
public class SymjaEditor extends TextEditor {

	private class ConvertJavaAction extends TextEditorAction {

		public ConvertJavaAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}

		private IAnnotationModel getAnnotationModel(ITextEditor editor) {
			return editor.getAdapter(ProjectionAnnotationModel.class);
		}

		@Override
		public void run() {
			ITextEditor editor = getTextEditor();
			ISelection selection = editor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				if (!textSelection.isEmpty()) {
					IAnnotationModel model = getAnnotationModel(editor);
					if (model != null) {
						String text = textSelection.getText().trim();
						if (text.length() > 0) {
							ExprEvaluator fEvaluator = new ExprEvaluator(false, 100);
//							OutputFormFactory fOutputFactory = OutputFormFactory.get(true, false, 5, 7);
							EvalEngine evalEngine = fEvaluator.getEvalEngine();
							evalEngine.setFileSystemEnabled(true);
							text = convertText(text);
							IExpr result = fEvaluator.evaluateWithTimeout(text, 10, TimeUnit.SECONDS, true,
									new EvalControlledCallable(fEvaluator.getEvalEngine()));
							if (result != null && result.isString()) {
								IStringX strX = (IStringX) result;
								StringSelection stringSelection = new StringSelection(strX.toString());
								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
								clipboard.setContents(stringSelection, null);
							}
						}
					}
				}
			}
		}

		public String convertText(String text) {
			text = "JavaForm(" + text + ", Prefix->True)";
			return text;
		}
	}
	
	private class ConvertJavaFormFloatAction extends ConvertJavaAction {
		public ConvertJavaFormFloatAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		public String convertText(String text) {
			text = "JavaForm(" + text + ", Float->True)";
			return text;
		}
	}
	
	private class ConvertJSFormAction extends ConvertJavaAction {
		public ConvertJSFormAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		public String convertText(String text) {
			text = "JSForm(" + text + ")";
			return text;
		}
	}
	
	private class ConvertJSFormFloatAction extends ConvertJavaAction {
		public ConvertJSFormFloatAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		public String convertText(String text) {
			text = "JSForm(" + text + ", Float)";
			return text;
		}
	}

	private class ConvertTeXFormAction extends ConvertJavaAction {
		public ConvertTeXFormAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		public String convertText(String text) {
			text = "TeXForm(" + text + ")";
			return text;
		}
	}
	
	private class ConvertMathMLFormAction extends ConvertJavaAction {
		public ConvertMathMLFormAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		public String convertText(String text) {
			text = "MathMLForm(" + text + ")";
			return text;
		}
	}
	
	private class DefineFoldingRegionAction extends TextEditorAction {

		public DefineFoldingRegionAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}

		private IAnnotationModel getAnnotationModel(ITextEditor editor) {
			return editor.getAdapter(ProjectionAnnotationModel.class);
		}

		@Override
		public void run() {
			ITextEditor editor = getTextEditor();
			ISelection selection = editor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				if (!textSelection.isEmpty()) {
					IAnnotationModel model = getAnnotationModel(editor);
					if (model != null) {

						int start = textSelection.getStartLine();
						int end = textSelection.getEndLine();

						try {
							IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
							int offset = document.getLineOffset(start);
							int endOffset = document.getLineOffset(end + 1);
							Position position = new Position(offset, endOffset - offset);
							model.addAnnotation(new ProjectionAnnotation(), position);
						} catch (BadLocationException x) {
							// ignore
						}
					}
				}
			}
		}
	}

	/** The outline page */
	private SymjaContentOutlinePage fOutlinePage;
	/** The projection support */
	private ProjectionSupport fProjectionSupport;

	/**
	 * Default constructor.
	 */
	public SymjaEditor() {
		super();
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method extend the actions to add those
	 * specific to the receiver
	 */
	@Override
	protected void createActions() {
		super.createActions();

		IAction action = new DefineFoldingRegionAction(SymjaEditorMessages.getResourceBundle(), "DefineFoldingRegion.", //$NON-NLS-1$
				this);
		setAction("DefineFoldingRegion", action); //$NON-NLS-1$
		action = new ConvertJavaAction(SymjaEditorMessages.getResourceBundle(), "ConvertJavaAction.", this); //$NON-NLS-1$
		setAction("ConvertJava", action); //$NON-NLS-1$
		action = new ConvertJavaFormFloatAction(SymjaEditorMessages.getResourceBundle(), "ConvertJavaFormFloatAction.", this); //$NON-NLS-1$
		setAction("ConvertJavaFormFloat", action); //$NON-NLS-1$
		action = new ConvertJSFormAction(SymjaEditorMessages.getResourceBundle(), "ConvertJSFormAction.", this); //$NON-NLS-1$
		setAction("ConvertJSForm", action); //$NON-NLS-1$
		action = new ConvertJSFormFloatAction(SymjaEditorMessages.getResourceBundle(), "ConvertJSFormFloatAction.", this); //$NON-NLS-1$
		setAction("ConvertJSFormFloat", action); //$NON-NLS-1$
		action = new ConvertTeXFormAction(SymjaEditorMessages.getResourceBundle(), "ConvertTeXFormAction.", this); //$NON-NLS-1$
		setAction("ConvertTeXForm", action); //$NON-NLS-1$
		action = new ConvertMathMLFormAction(SymjaEditorMessages.getResourceBundle(), "ConvertMathMLFormAction.", this); //$NON-NLS-1$
		setAction("ConvertMathMLForm", action); //$NON-NLS-1$
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra disposal actions
	 * required by the java editor.
	 */
	@Override
	public void dispose() {
		if (fOutlinePage != null)
			fOutlinePage.setInput(null);
		super.dispose();
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra revert behavior
	 * required by the java editor.
	 */
	@Override
	public void doRevertToSaved() {
		super.doRevertToSaved();
		if (fOutlinePage != null)
			fOutlinePage.update();
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra save behavior
	 * required by the java editor.
	 *
	 * @param monitor the progress monitor
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (fOutlinePage != null)
			fOutlinePage.update();
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra save as behavior
	 * required by the java editor.
	 */
	@Override
	public void doSaveAs() {
		super.doSaveAs();
		if (fOutlinePage != null)
			fOutlinePage.update();
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs sets the input of the outline
	 * page after AbstractTextEditor has set input.
	 *
	 * @param input the editor input
	 * @throws CoreException in case the input can not be set
	 */
	@Override
	public void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		if (fOutlinePage != null)
			fOutlinePage.setInput(input);
	}

	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		
		addAction(menu, "ConvertJava"); //$NON-NLS-1$
		addAction(menu, "ConvertJavaFormFloat"); //$NON-NLS-1$
		addAction(menu, "ConvertJSForm"); //$NON-NLS-1$
		addAction(menu, "ConvertJSFormFloat"); //$NON-NLS-1$
		addAction(menu, "ConvertTeXForm"); //$NON-NLS-1$
		addAction(menu, "ConvertMathMLForm"); //$NON-NLS-1$ 
		
		addAction(menu, "ContentAssistProposal"); //$NON-NLS-1$
		addAction(menu, "ContentAssistTip"); //$NON-NLS-1$
		addAction(menu, "DefineFoldingRegion"); //$NON-NLS-1$
	}

	/**
	 * The <code>SymjaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs gets the java content outline
	 * page if request is for a an outline page.
	 *
	 * @param required the required type
	 * @return an adapter for the required type or <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) {
				fOutlinePage = new SymjaContentOutlinePage(getDocumentProvider(), this);
				if (getEditorInput() != null)
					fOutlinePage.setInput(getEditorInput());
			}
			return (T) fOutlinePage;
		}

		if (fProjectionSupport != null) {
			T adapter = fProjectionSupport.getAdapter(getSourceViewer(), required);
			if (adapter != null)
				return adapter;
		}

		return super.getAdapter(required);
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new SymjaSourceViewerConfiguration());
	}

	@Override
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		fAnnotationAccess = createAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(),
				styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
		fProjectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
		fProjectionSupport.install();
		viewer.doOperation(ProjectionViewer.TOGGLE);
	}

	@Override
	protected void adjustHighlightRange(int offset, int length) {
		ISourceViewer viewer = getSourceViewer();
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
			extension.exposeModelRange(new Region(offset, length));
		}
	}
}
