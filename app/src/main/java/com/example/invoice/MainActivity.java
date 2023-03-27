package com.example.invoice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupieAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static MainActivity main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.main = this;

        TransmitterConnection transmitterConnection = loadConnection();
        Transmitter.IP = transmitterConnection.IP;
        Transmitter.PORT = transmitterConnection.PORT;


        FloatingActionButton main_button_add = findViewById(R.id.main_button_add);
        main_button_add.setOnClickListener(view -> showAddUserDialog());

        FloatingActionButton main_save = findViewById(R.id.main_save);
        main_save.setOnClickListener(view -> showPrintDialog());

        RecyclerView main_rec_user = findViewById(R.id.main_rec_user);
        main_rec_user.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        GroupieAdapter adapter = new GroupieAdapter();
        main_rec_user.setAdapter(adapter);

        refreshUserList();

        adapter.setOnItemClickListener((item, view) -> {
            Intent myIntent = new Intent(MainActivity.this, UserActivity.class);
            UserItem userItem = (UserItem) item;
            myIntent.putExtra("name", userItem.NAME);
            startActivity(myIntent);
            refreshUserList();
        });
    }

    private void showPrintDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Speichern")
                .setMessage("Der aktuelle Stand wird auf dem Server gespeichert!")
                .setIcon(R.drawable.ic_save)
                .setPositiveButton("Okay", (dialog, whichButton) -> Toast.makeText(main, Transmitter.save(), Toast.LENGTH_SHORT).show())
                .setNegativeButton("Abbrechen", (dialog, whichButton) -> dialog.cancel()).show();
    }

    public void refreshUserList() {
        RecyclerView main_rec_user = findViewById(R.id.main_rec_user);
        GroupieAdapter adapter = (GroupieAdapter) main_rec_user.getAdapter();
        adapter.clear();
        for (String string : Transmitter.getUser()) {
            if (!Objects.equals(string, "False")) {
                adapter.add(new UserItem(string));
            }
        }
    }

    public void addUser(String name) {
        if (Transmitter.addUser(name).equals("False")) {
            Toast.makeText(this, "\"" + name + "\"" + " couldn't be created!", Toast.LENGTH_SHORT).show();
        }
        refreshUserList();
    }

    public void showAddUserDialog() {
        final EditText input = new EditText(this);
        input.setHint(" Name des Mitarbeiters");
        new AlertDialog.Builder(this)
                .setTitle("Hinzufügen:")
                .setMessage("Welchen Mitarbeiter willst du hinzufügen?")
                .setIcon(R.drawable.ic_group_add)
                .setView(input)
                .setPositiveButton("Hinzufügen", (dialog, whichButton) -> addUser(input.getText().toString()))
                .setNegativeButton("Abbrechen", (dialog, whichButton) -> dialog.cancel()).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_settings:
                showSettingsDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showSettingsDialog() {
        final EditText input = new EditText(this);
        final TransmitterConnection transmitterConnection = loadConnection();
        input.setText(transmitterConnection.IP + " " + transmitterConnection.PORT);
        input.setHint("Ip-adresse & Port");
        new AlertDialog.Builder(this)
                .setTitle("Verbindung:")
                .setMessage("Verbinde dich mit Ip-adresse und Port")
                .setIcon(R.drawable.ic_cell_wifi)
                .setView(input)
                .setPositiveButton("Speichern", (dialog, whichButton) -> {
                    saveConnection(new TransmitterConnection(input.getText().toString()));
                    refreshUserList();
                })
                .setNegativeButton("Abbrechen", (dialog, whichButton) -> dialog.cancel()).show();
    }


    public void saveConnection(TransmitterConnection transmitterConnection){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ip", transmitterConnection.IP);
        editor.putInt("port", transmitterConnection.PORT);
        editor.apply();

        Transmitter.IP = transmitterConnection.IP;
        Transmitter.PORT = transmitterConnection.PORT;
    }

    public TransmitterConnection loadConnection(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String ip = sharedPref.getString("ip", "0.0.0.0");
        int port = sharedPref.getInt("port", 8080);

        Transmitter.IP = ip;
        Transmitter.PORT = port;

        return new TransmitterConnection(ip, port);
    }
}