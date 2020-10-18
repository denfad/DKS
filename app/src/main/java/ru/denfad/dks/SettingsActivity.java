package ru.denfad.dks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferencesLectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        ImageButton b = findViewById(R.id.button);

        sharedPreferencesLectures = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        final RadioGroup groupChangeView = findViewById(R.id.radio_group);

        final EditText text = findViewById(R.id.date);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferencesLectures.edit();
                editor.putInt("schedule", groupChangeView.getCheckedRadioButtonId());
                editor.putInt("date", Integer.valueOf(text.getText().toString()));
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
