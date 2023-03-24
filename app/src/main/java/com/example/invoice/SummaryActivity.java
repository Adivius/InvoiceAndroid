package com.example.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    String MONTH = "";
    int ID;
    String NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        NAME = intent.getStringExtra("name");
        ID = intent.getIntExtra("id", 0);


        ArrayList<String> month = Transmitter.getAll();

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_month, month);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            MONTH = autoCompleteTextView.getText().toString();
            refresh();
        });
    }

    void refresh() {

        String[] total = Transmitter.getUserTotal(ID, MONTH);

        TextView textView0 = findViewById(R.id.summary_orders_count);
        textView0.setText("Mahlzeiten: " + total[0]);

        TextView textView1 = findViewById(R.id.summary_bill);
        textView1.setText("Einkäufe: " + total[1] + "€");

        TextView textView2 = findViewById(R.id.summary_price);
        textView2.setText("Pro Mahlzeit: " + total[2] + "€");

        TextView textView3 = findViewById(R.id.summary_price_total);
        textView3.setText("Insgesamt: " + total[3] + "€");

        TextView textView4 = findViewById(R.id.summary_start);
        textView4.setText("Startkapital: : " + total[4] + "€");

        float end = Float.parseFloat(total[3]) + Float.parseFloat(total[4]);

        end = Math.round(end * 100.0f) / 100.0f;

        TextView textView5 = findViewById(R.id.summary_end);
        textView5.setText("Endkapital: " + end + "€");
    }
}