package com.example.whomatch.tables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.whomatch.R;
import com.example.whomatch.TableConstructor;
import com.example.whomatch.data.BufferDataMap;

import java.util.HashMap;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public final class StatisticTable extends AppCompatActivity {

    private int count = 1;
    private final String[] headers = new String[]{"№", "Name", "Sum", "%"};
    private final BufferDataMap bdm = BufferDataMap.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        TableView tablePersons = findViewById(R.id.table_2);

        String[][] data = createArray(bdm.getMap().size(), bdm.getMap());
        sortingArray(data);

        TableConstructor tableConstructor = new TableConstructor(
                tablePersons, new TableColumnWeightModel(4), headers, data);
        tableConstructor.initTable(new TableConstructor.ParametersOfWidth(0, 1),
                new TableConstructor.ParametersOfWidth(1, 2),
                new TableConstructor.ParametersOfWidth(2, 2),
                new TableConstructor.ParametersOfWidth(3, 1)
    );
    }

    private void sortingArray(String[][] array) {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < array.length - 1; i++) {
                if (Integer.parseInt(array[i][2]) < Integer.parseInt(array[i + 1][2])) {
                    swap(array, i);
                    isSorted = false;
                }
            }
        }
        for (String[] strings : array) {
            strings[0] = String.valueOf(count++);
        }
    }

    private void swap(String[][] array, int i) {
        String[] tmp = array[i];
        array[i] = array[i + 1];
        array[i + 1] = tmp;
    }

    private String[][] createArray(int size, HashMap<String, Integer> map) {
        String[][] array = new String[size][4];
        int row = 0;
        for (String s : map.keySet()) {
            array[row][0] = "";
            array[row][1] = s;
            array[row][2] = String.valueOf(map.get(s));
            array[row++][3] = String.valueOf((bdm.getMap().get(s) * 100) / bdm.getTotal());
        }
        return array;
    }
}