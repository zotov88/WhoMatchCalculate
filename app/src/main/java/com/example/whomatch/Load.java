package com.example.whomatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whomatch.data.BufferDataMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class Load extends AppCompatActivity {

    private final BufferDataMap bdm = BufferDataMap.getInstance();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listOfFileNames;
    private static final String FILE_NAME = "user_data.txt";
    private EditText ed_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        listOfFileNames = getListFileNames(getListFiles());
        ListView listView = findViewById(R.id.list_saved);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, listOfFileNames);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(Load.this).setIcon(R.drawable.ic_delete)
                        .setTitle("Удалить?").setMessage(listOfFileNames.get(position))
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeFile(position);
                                listOfFileNames.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Нет", null).show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(Load.this).setIcon(R.drawable.ic_download)
                        .setTitle("Загрузить?").setMessage(listOfFileNames.get(position))
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                overwriting(position);
                                onMainActivity(view);
                            }
                        }).setNegativeButton("Нет", null).show();
            }
        });
    }

    private void overwriting(final int position) {
        File fileFrom = new File(getApplicationInfo().dataDir + File.separator + "files"
                + File.separator + listOfFileNames.get(position));

        try (BufferedReader br = new BufferedReader(new FileReader(fileFrom));
             BufferedWriter bw = new BufferedWriter(new FileWriter(new File(getFilesDir(), FILE_NAME)))) {
            String line;
            bdm.resetList();
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
                bdm.addToList(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFile(final int position) {
        File file = new File(getApplicationInfo().dataDir + File.separator + "files"
                + File.separator + listOfFileNames.get(position));
        file.delete();
    }

    private ArrayList<String> getListFileNames(File[] file) {
        ArrayList<String> list = new ArrayList<>();
        for (File one : file) {
            list.add(one.getName());
        }
        list.remove("user_data.txt");
        return list;
    }

    private File[] getListFiles() {
        File file = new File(getApplicationInfo().dataDir + File.separator + "files");
        File[] dirFiles = null;
        if (file.isDirectory()) {
            dirFiles = file.listFiles();
        }
        return dirFiles;
    }

    private String getFromFile() {
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("eName");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void onMainActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}