package com.example.whomatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.whomatch.data.BufferDataMap;
import com.example.whomatch.tables.MainTable;
import com.example.whomatch.tables.StatisticTable;

public final class Aggregator extends AppCompatActivity {

    private final BufferDataMap bdm = BufferDataMap.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregator);

        EditText output = findViewById(R.id.et_output);
        ImageButton bt_stat = findViewById(R.id.bt_statistic);
        ImageButton bt_table = findViewById(R.id.bt_to_table);

        output.setText(Html.fromHtml("<b>" + "Всего: " + "</b> "));
        output.append(bdm.getTotal() + "\n");
        output.append(Html.fromHtml("<b>" + "Средняя: " + "</b> "));
        output.append(bdm.getAvg() + "");

        bt_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStatisticView(v);
            }
        });

        bt_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTableView(v);
            }
        });
    }

    public void onTableView(View v) {
        Intent intent = new Intent(this, MainTable.class);
        startActivity(intent);
    }

    public void onStatisticView(View v) {
        Intent intent = new Intent(this, StatisticTable.class);
        startActivity(intent);
    }
}