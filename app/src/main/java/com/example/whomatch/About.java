package com.example.whomatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.whomatch.data.BufferDataMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public final class About extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BufferDataMap bdm = BufferDataMap.getInstance();
        setContentView(R.layout.activity_about);
        textView = findViewById(R.id.tv_about);
        getOn();
        textView.append(String.valueOf(bdm.getList().size()));

    }

    public void getOn() {
        try {
            FileInputStream fis = openFileInput("user_data.txt");
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lines = "";
            StringBuilder sb = new StringBuilder();
            while ((lines = bufferedReader.readLine()) != null) {
                sb.append(lines);
                sb.append("\n");
            }
            textView.setText(sb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String about() {
        return "\tПриложение помогает определить одинаковый денежный вклад для каждого члена компании.\n" +
                "\tВ поле вводится через пробел имя и сумма. Каждый человек с новой строки." +
                "\n\tЕсли вы получаете сообщение \"Некорректный ввод\", значит допустили ошибку.\n" +
                "\tПосле подсчета приложениеи оповестит, чей взнос больше, меньше или равен средней сумме.\n" +
                "\tЕсли окажется, что чьи-то вклады выше средней суммы, то будет предложен вариант" +
                " распределения денег от людей, чьи вклады оказались меньше в формате:" +
                "\n\n\"\tнедоплативший -> переплативший -> сумма\"" +
                "\n\n\tВ силу того, что программа не работает с копейками, могут возникать погрешности в +/- несколько рублей";
    }
}