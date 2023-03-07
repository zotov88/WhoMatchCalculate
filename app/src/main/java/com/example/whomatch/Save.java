package com.example.whomatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.whomatch.data.BufferDataMap;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Save extends AppCompatActivity {

    private final BufferDataMap bdm = BufferDataMap.getInstance();
    private EditText et_fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        et_fileName = findViewById(R.id.et_save);
        ImageButton bt_save = findViewById(R.id.bt_save_name);


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = et_fileName.getText().toString();
                if (!fileName.isEmpty()) {
                    DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
                    Date today = Calendar.getInstance().getTime();
                    File file = new File(getFilesDir(), fileName + " (" + df.format(today) + ")");
                    addItemOnFile(file);
                    Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                    onMainActivity(v);
                } else {
                    Toast.makeText(getApplicationContext(), "Введите имя файла", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addItemOnFile(File file) {
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            for (String s : bdm.getList()) {
                fos.write((s + "\n").getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMainActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}