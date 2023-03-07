package com.example.whomatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whomatch.data.BufferDataMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public final class MainActivity extends AppCompatActivity {

    private EditText input_name;
    private TextView input_sum;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private File file;
    private final BufferDataMap bdm = BufferDataMap.getInstance();
    private static final String FILE_NAME = "user_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton bt_calc = findViewById(R.id.bt_calculate);
        ImageButton bt_clear = findViewById(R.id.bt_clear);
        ImageButton bt_add = findViewById(R.id.bt_add);
        ImageButton bt_save = findViewById(R.id.bt_save);
        ImageButton bt_download = findViewById(R.id.bt_download);
        input_name = findViewById(R.id.et_input_name);
        input_sum = findViewById(R.id.et_input_sum);
        listView = findViewById(R.id.list_view);
        file = new File(getFilesDir(), FILE_NAME);

        initArrayFromFile();
        updateAdapter();

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveView(v);
            }
        });

        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadView(v);
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_name.getText().toString().length() != 0 && input_sum.getText().toString().length() != 0) {
                    updateAdapter();
                    addItemOnList();
                    addItemOnFile();
                } else {
                    Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdm.resetAllParameters();
                bdm.setAllParameters();
                if (bdm.getMap().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Нет данных для подсчета", Toast.LENGTH_SHORT).show();
                } else {
                    onAggregatorView(v);
                }
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this list?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateFile(false);
                                listView.setAdapter(null);
                                bdm.resetList();
                                Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_alert_red)
                        .show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Долгое нажатие для удаления", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_delete)
                        .setTitle("Удалить?").setMessage(bdm.getList().get(position))
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bdm.removeItemFromList(position);
                                adapter.notifyDataSetChanged();
                                updateFile(true);
                            }
                        }).setNegativeButton("Нет", null).show();
                return true;
            }
        });
    }

    public void addItemOnFile() {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write((input_name.getText().toString() + " " + input_sum.getText().toString() + "\n").getBytes());
            input_name.setText("");
            input_sum.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFile(boolean isUpdating) {
        try (PrintWriter writer = new PrintWriter(file)) {
            if (isUpdating) {
                for (String s : bdm.getList()) {
                    writer.print(s + "\n");
                }
            } else {
                writer.print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initArrayFromFile() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (!bdm.getList().contains(line)) {
                    bdm.addToList(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, bdm.getList());
        listView.setAdapter(adapter);
    }

    private void addItemOnList() {
        String text = input_name.getText().toString() + " " + input_sum.getText().toString();
        bdm.addToList(text);
        adapter.notifyDataSetChanged();
    }

    public void onAggregatorView(View v) {
        Intent intent = new Intent(this, Aggregator.class);
        startActivity(intent);
    }

    public void onSaveView(View v) {
        Intent intent = new Intent(this, Save.class);
        startActivity(intent);
    }

    public void onLoadView(View v) {
        Intent intent = new Intent(this, Load.class);
        startActivity(intent);
    }
}