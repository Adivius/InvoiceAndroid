package com.example.invoice;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    LocalDate selectedDate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        String NAME = getIntent().getStringExtra("name");
        int ID = getIntent().getIntExtra("id", 0);

        getSupportActionBar().setTitle("Einkauf registrieren");

        CalendarView calendarView = findViewById(R.id.purchase_calendar);
        EditText editText = findViewById(R.id.purchase_bill);
        Button button = findViewById(R.id.purchase_add);

        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> selectedDate = LocalDate.of(i, i1 + 1, i2));


        button.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Bitte gebe einen Betrag an!", Toast.LENGTH_SHORT).show();
                return;
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = selectedDate.format(dateTimeFormatter);

            String bill = editText.getText().toString();

            if (!Transmitter.addPurchase(ID, Float.parseFloat(bill), dateString).equals("False")) {
                finish();
                Toast.makeText(this, "Einkauf über " + bill + " wurde hinzugefügt!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}