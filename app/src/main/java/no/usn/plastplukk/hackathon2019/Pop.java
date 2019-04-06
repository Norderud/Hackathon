package no.usn.plastplukk.hackathon2019;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import android.widget.TextView;


public class Pop extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        TextView tittelText = findViewById(R.id.tittelText);
        WebView tekstText = findViewById(R.id.tekstText);
        TextView bunnText = findViewById(R.id.bunnText);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.9), (int) (height*.65));

        String tittel = getIntent().getStringExtra("Tittel");
        String tekst = getIntent().getStringExtra("Tekst");
        int mood = getIntent().getIntExtra("Mood", 0);
        String dato = getIntent().getStringExtra("Dato");

        tittelText.setText(tittel);
        tekstText.loadData(tekst, "text/html; charset=utf-8", "utf-8");
        bunnText.setText("Dato: " + dato +"          Mood: " + mood);

    }
}
