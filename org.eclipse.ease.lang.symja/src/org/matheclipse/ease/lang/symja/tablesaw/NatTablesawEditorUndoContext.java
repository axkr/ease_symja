package org.matheclipse.ease.lang.symja.tablesaw;

import org.eclipse.core.commands.operations.IUndoContext;

public class NatTablesawEditorUndoContext implements IUndoContext {

    @Override
    public String getLabel() {
        return "NatTablesawEditor";
    }

    @Override
    public boolean matches(IUndoContext context) {
        return context instanceof NatTablesawEditorUndoContext;
    }
}
