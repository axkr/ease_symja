package org.eclipse.ease.lang.symja;

import org.eclipse.ease.lang.symja.SymReconcilerStrategy;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.reconciler.Reconciler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

public class SymReconciler extends Reconciler {

    private SymReconcilerStrategy fStrategy;

    public SymReconciler() {
        // TODO this is logic for .project file to fold tags. Replace with your language logic!
        fStrategy = new SymReconcilerStrategy();
        this.setReconcilingStrategy(fStrategy, IDocument.DEFAULT_CONTENT_TYPE);
    }

    @Override
    public void install(ITextViewer textViewer) {
        super.install(textViewer);
        ProjectionViewer pViewer =(ProjectionViewer)textViewer;
        fStrategy.setProjectionViewer(pViewer);
    }
}