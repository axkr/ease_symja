package org.matheclipse.ease.lang.symja.tablesaw.commands;

import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.nebula.widgets.nattable.selection.ISelectionModel;
import org.eclipse.swt.graphics.Rectangle;
import org.matheclipse.ease.lang.symja.tablesaw.NatTablesawEditor;
import org.matheclipse.ease.lang.symja.tablesaw.NatTablesawViewer;
import org.matheclipse.ease.lang.symja.tablesaw.TablesawDataProvider;

public abstract class AbstractNatTablesawEditorOperation extends AbstractOperation {

    private final NatTablesawEditor natTablesawEditor;
    
    public AbstractNatTablesawEditorOperation(String name, NatTablesawEditor natTablesawEditor) {
        super(name);
        this.natTablesawEditor = natTablesawEditor;
    }
    
    protected NatTablesawEditor getNatTablesawEditor() {
        return natTablesawEditor;
    }
    
    protected NatTablesawViewer getNatTablesawViewer() {
        return getNatTablesawEditor().getNatTablesawViewer();
    }
    
    protected TablesawDataProvider getTablesawDataProvider() {
        return getNatTablesawViewer().getTablesawDataProvider();
    }

    //

    protected void selectRows(int... rowNums) {
        ISelectionModel selectionModel = getNatTablesawEditor().getNatTablesawViewer().getSelectionLayer().getSelectionModel();
        int columnCount = getNatTablesawEditor().getModelTable().columnCount();
        for (int i = 0; i < rowNums.length; i++) {                
            selectionModel.addSelection(new Rectangle(0, rowNums[i], columnCount, 1));
        }
    }

    protected void selectColumns(int... columnNums) {
        ISelectionModel selectionModel = getNatTablesawEditor().getNatTablesawViewer().getSelectionLayer().getSelectionModel();
        int rowCount = getNatTablesawEditor().getModelTable().rowCount();
        for (int i = 0; i < columnNums.length; i++) {                
            selectionModel.addSelection(new Rectangle(columnNums[i], 0, 1, rowCount));
        }
    }
}
