package com.example.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    public String NAME;
    public int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        NAME = intent.getStringExtra("name");
        String id_result = Transmitter.getIdByName(NAME);

        if (id_result.equals("False")) {
            Toast.makeText(this, "\"" + NAME + "\" konnte nicht geladen werden!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ID = Integer.parseInt(id_result);
        }

        getSupportActionBar().setTitle(NAME);

        Button user_remove = findViewById(R.id.user_remove);
        user_remove.setOnClickListener(view -> showDeleteDialog());

        Button user_add_order = findViewById(R.id.user_add_order);
        user_add_order.setOnClickListener(view -> {
            Intent changeActivityIntent = new Intent(this, ChangeActivity.class);
            changeActivityIntent.putExtra("name", NAME);
            changeActivityIntent.putExtra("id", ID);
            startActivity(changeActivityIntent);
        });

        Button user_add_purchase = findViewById(R.id.user_add_purchase);
        user_add_purchase.setOnClickListener(view -> {
            Intent changeActivityIntent = new Intent(this, PurchaseActivity.class);
            changeActivityIntent.putExtra("name", NAME);
            changeActivityIntent.putExtra("id", ID);
            startActivity(changeActivityIntent);
        });

        Button user_summary = findViewById(R.id.user_summary);
        user_summary.setOnClickListener(view -> {
            Intent changeActivityIntent = new Intent(this, SummaryActivity.class);
            changeActivityIntent.putExtra("name", NAME);
            changeActivityIntent.putExtra("id", ID);
            startActivity(changeActivityIntent);
        });

        Button user_overview = findViewById(R.id.user_overview);
        user_overview.setOnClickListener(view -> showOverview());

    }

    public void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Warnung")
                .setMessage("Möchtest du \"" + NAME + "\" wirklich löschen?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Löschen", (dialog, whichButton) -> deleteUser())
                .setNegativeButton("Abbrechen", (dialog, whichButton) -> dialog.cancel()).show();
    }

    public void showOverview() {

        int count = Transmitter.getOrderCountById(ID, Utils.getCurrentMonth());
        float sumBill = Transmitter.getPurchaseSumById(ID, Utils.getCurrentMonth());
        String message = "\nMahlzeiten: " + count + "x\n\nEingekauft für: " + sumBill + "€";
        new AlertDialog.Builder(this)
                .setTitle("Übersicht von \"" + NAME + "\"")
                .setMessage(message)
                .setIcon(R.drawable.ic_info)
                .setPositiveButton("Schließen", null).show();
    }

    public void deleteUser() {
        if (Transmitter.removeUser(NAME).equals("False")) {
            Toast.makeText(UserActivity.this, "\"" + NAME + "\" konnte nicht entfernt werden!", Toast.LENGTH_SHORT).show();
        }
        finish();
        MainActivity.main.refreshUserList();
    }
}