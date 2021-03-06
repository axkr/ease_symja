package org.matheclipse.ease.lang.symja.tablesaw;

import org.eclipse.swt.widgets.Composite;
import org.matheclipse.ease.lang.symja.tablesaw.views.AbstractTablesawView;

import tech.tablesaw.api.Table;

public class NatTablesawView extends AbstractTablesawView implements TableProvider {

    public NatTablesawView() {
        super(false);
    }

//    @Override
//    protected void createConfigControls(final Composite configParent) {
//        createTableRegistrySelector("Source: ", configParent, this);
//    }

    private NatTablesawViewer natTablesawViewer;

    @Override
    protected void createTableDataControls(final Composite parent) {
        super.createTableDataControls(parent);
        natTablesawViewer = new NatTablesawViewer() {
            @Override
            protected TableProvider getTableProvider() {
                return NatTablesawView.this;
            }
        };
        natTablesawViewer.createPartControl(parent);
    }

    protected Table getTableViewerInput() {
        return getViewTable();
    }

    @Override
    protected void updateTableControls() {
        natTablesawViewer.setInput(getTableViewerInput());
    }

    // TableProvider

    @Override
    public Table getTable() {
        return natTablesawViewer.getTable();
    }

    @Override
    public void addTableDataProviderListener(final TableProvider.Listener listener) {
        natTablesawViewer.addTableDataProviderListener(listener);
    }

    @Override
    public void removeTableDataProviderListener(final TableProvider.Listener listener) {
        natTablesawViewer.removeTableDataProviderListener(listener);
    }
}
