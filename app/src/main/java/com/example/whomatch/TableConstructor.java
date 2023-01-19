package com.example.whomatch;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public final class TableConstructor {

    private final TableView tableView;
    private final TableColumnWeightModel columnWeightModel;
    private final String[] headers;
    private final String[][] data;

    public TableConstructor(TableView tableView, TableColumnWeightModel columnWeightModel, String[] headers, String[][] data) {
        this.tableView = tableView;
        this.columnWeightModel = columnWeightModel;
        this.headers = headers;
        this.data = data;
    }

    public void initTable(ParametersOfWidth... parametersOfWidths) {
        for (ParametersOfWidth parameter : parametersOfWidths) {
            columnWeightModel.setColumnWeight(parameter.columIndex, parameter.columnAbsoluteWidth);
        }
        tableView.setColumnModel(columnWeightModel);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(tableView.getContext(), headers));
        tableView.setDataAdapter(new SimpleTableDataAdapter(tableView.getContext(), data));
    }

    public static class ParametersOfWidth {
        private final int columIndex;
        private final int columnAbsoluteWidth;

        public ParametersOfWidth(int columIndex, int columnAbsoluteWidth) {
            this.columIndex = columIndex;
            this.columnAbsoluteWidth = columnAbsoluteWidth;
        }
    }
}
