package com.example.whomatch.tables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.whomatch.Person;
import com.example.whomatch.R;
import com.example.whomatch.TableConstructor;
import com.example.whomatch.data.BufferDataMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public final class MainTable extends AppCompatActivity {

    private final String[] headers = {"№", "Кто", "Кому", "Сколько"};
    private final BufferDataMap bdm = BufferDataMap.getInstance();
    private final ArrayList<String[]> listOfRowsForTable = new ArrayList<>();
    private final Stack<Person> getStack = new Stack<>();
    private final Stack<Person> giveStack = new Stack<>();
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        TableView table = findViewById(R.id.table_1);

        getStacks(bdm.getMap(), bdm.getAvg());
        distributionMoney(giveStack, getStack, listOfRowsForTable, bdm.getAvg());

        TableConstructor tableConstructor = new TableConstructor(
                table, new TableColumnWeightModel(4), headers, listToMatrix());
        tableConstructor.initTable(new TableConstructor.ParametersOfWidth(0, 1),
                new TableConstructor.ParametersOfWidth(1, 2),
                new TableConstructor.ParametersOfWidth(2, 2),
                new TableConstructor.ParametersOfWidth(3, 2)
        );

        if (listOfRowsForTable.isEmpty()) {
            table.setVisibility(View.INVISIBLE);
        }
    }

    private String[][] listToMatrix() {
        String[][] result = new String[listOfRowsForTable.size()][];
        int index = 0;
        for (String[] array : listOfRowsForTable) {
            result[index++] = array;
        }
        return result;
    }

    private void getStacks(HashMap<String, Integer> persons, int avg) {
        for (String personName : persons.keySet()) {
            if (avg - persons.get(personName) > 0) {
                giveStack.push(new Person(personName, persons.get(personName)));
            } else if (avg - persons.get(personName) < 0) {
                getStack.push(new Person(personName, persons.get(personName)));
            }
        }
    }

    private void distributionMoney(Stack<Person> giveStack, Stack<Person> getStack, ArrayList<String[]> list, int avg) {
        while (getStack.size() > 0 && giveStack.size() > 0) {
            Person give = giveStack.pop();
            Person get = getStack.pop();
            int moneyGet = avg - get.getMoney();
            int moneyGive = avg - give.getMoney();
            int sum = 0;
            while (moneyGet < 0 && moneyGive > 0) {
                moneyGet++;
                moneyGive--;
                sum++;
            }
            give.setMoney(give.getMoney() + sum);
            get.setMoney(get.getMoney() - sum);
            list.add(new String[]{String.valueOf(count++), give.getName(), get.getName(), String.valueOf(sum)});
            if (moneyGet != 0) {
                getStack.push(get);
            }
            if (moneyGive != 0) {
                giveStack.push(give);
            }
        }
    }
}