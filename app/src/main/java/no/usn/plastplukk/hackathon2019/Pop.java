package no.usn.plastplukk.hackathon2019;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Pop extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        TextView datoText = findViewById(R.id.datoText);
        TextView tittelText = findViewById(R.id.tittelText);
        TextView tekstText = findViewById(R.id.tekstText);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.9), (int) (height*.9));

        String tittel = getIntent().getStringExtra("Tittel");
        String tekst = getIntent().getStringExtra("Tekst");
        int mood = getIntent().getIntExtra("Mood", 0);
        String dato = getIntent().getStringExtra("Dato");

        datoText.setText(dato + " - Mood: " + mood);
        tittelText.setText(tittel);
        tekstText.setText(tekst);

    }
}
