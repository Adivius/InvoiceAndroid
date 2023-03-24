package com.example.invoice;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class ChangeActivity extends AppCompatActivity {

    LocalDate selectedDate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        Intent intent = getIntent();
        String NAME = intent.getStringExtra("name");
        int ID = intent.getIntExtra("id", 0);

        CalendarView calendarView = findViewById(R.id.change_calendar);
        TextView textView = findViewById(R.id.change_text_view);
        SeekBar seekBar = findViewById(R.id.change_slider);
        Button button = findViewById(R.id.change_add);


        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> selectedDate = LocalDate.of(i, i1 + 1, i2));


        button.setOnClickListener(view -> {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = selectedDate.format(dateTimeFormatter);

            if (!Transmitter.addOrder(ID, (seekBar.getProgress() + 1), dateString).equals("False")) {
                finish();
                Toast.makeText(this, "Mahlzeiten wurden hinzugefügt!", Toast.LENGTH_SHORT).show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText("Mahlzeiten: " + (seekBar.getProgress() + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}