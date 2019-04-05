package no.usn.plastplukk.hackathon2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("MyPrefsName", MODE_PRIVATE);

        if (prefs.getString("Username", "").length() == 0){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
