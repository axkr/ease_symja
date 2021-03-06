package org.matheclipse.ease.lang.symja.tablesaw.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.WorkbenchPart;
import org.matheclipse.ease.lang.symja.tablesaw.NatTablesawEditor;

public abstract class AbstractNatTablesawEditorHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
        if (activeEditor instanceof NatTablesawEditor) {
            return execute((NatTablesawEditor) activeEditor);
        }
        return null;
    }

    protected IStatus execute(NatTablesawEditor editor, IUndoableOperation operation, IProgressMonitor monitor, IAdaptable adaptable) throws ExecutionException {
        operation.addContext(editor.getUndoContext());
        return AbstractNatTablesawEditorHandler.execute((WorkbenchPart) editor, operation, monitor, adaptable);
    }

    public static IStatus execute(WorkbenchPart part, IUndoableOperation operation, IProgressMonitor monitor, IAdaptable adaptable) throws ExecutionException {
        IWorkbench workbench = part.getSite().getWorkbenchWindow().getWorkbench();
        IOperationHistory operationHistory = workbench.getOperationSupport().getOperationHistory();
        return operationHistory.execute(operation, monitor, adaptable);
    }
    
    protected abstract IStatus execute(NatTablesawEditor activeEditor) throws ExecutionException;
}
