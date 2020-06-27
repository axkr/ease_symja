package org.matheclipse.ease.lang.symja.tablesaw;

import tech.tablesaw.api.ColumnType;

public interface ColumnTypeProvider {

	public ColumnType getColumnType(int columnIndex);
}
