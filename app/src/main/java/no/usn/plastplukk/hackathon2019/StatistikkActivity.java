package no.usn.plastplukk.hackathon2019;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Integer.parseInt;
//Viser hvor mange ganger brukeren har postet med ulike moods
//Enten siste uke eller nåværende måned
public class StatistikkActivity extends AppCompatActivity {

    TextView mood1, mood2, mood3, mood4, mood5;
    int antallMood1, antallMood2, antallMood3, antallMood4, antallMood5;
    boolean uke, måned;
    Button ukeKnapp, månedKnapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistikk);

        mood1 = findViewById(R.id.mood1);
        mood2 = findViewById(R.id.mood2);
        mood3 = findViewById(R.id.mood3);
        mood4 = findViewById(R.id.mood4);
        mood5 = findViewById(R.id.mood5);

        ukeKnapp = findViewById(R.id.ukeKnapp);
        månedKnapp = findViewById(R.id.månedKnapp);

        getArkiv();

    }
    //Henter notater fra databasen
    //Legger til antall verdier til ulike moods
    private void getArkiv() {
        antallMood1 = 0;
        antallMood2 = 0;
        antallMood3 = 0;
        antallMood4 = 0;
        antallMood5 = 0;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String[] datoTabell;
                    int[] moodTabell;
                    Log.e("Response", response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        JSONObject jsonObject = new JSONObject(jsonResponse.getString("array"));
                        moodTabell = new int[jsonObject.length()];
                        datoTabell = new String[jsonObject.length()];
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(Calendar.getInstance().getTime());

                        for (int i=0; i<jsonObject.length(); i++){
                            JSONArray arkivArray = jsonObject.getJSONArray(""+(i+1));
                            moodTabell[i] = arkivArray.getInt(2);
                            datoTabell[i] = arkivArray.getString(3).substring(0, 10);
                            if (måned){
                                if (!datoTabell[i].substring(0,7).equals(timeStamp.substring(0, 7))){
                                    moodTabell[i]=0;
                                }
                            } else if (uke){
                                if (parseInt(timeStamp.substring(9, 10)) - parseInt(datoTabell[i].substring(9, 10)) > 7){
                                    moodTabell[i] =0;
                                }
                            }

                            switch(moodTabell[i]){
                                case 1: antallMood1++; break;
                                case 2: antallMood2++; break;
                                case 3: antallMood3++; break;
                                case 4: antallMood4++; break;
                                case 5: antallMood5++; break;
                            }
                        }
                        mood1.setText("Veldig dårlig humør: " + antallMood1);
                        mood2.setText("Dårlig humør: " + antallMood2);
                        mood3.setText("Middels humør: " + antallMood3);
                        mood4.setText("Godt humør: " + antallMood4);
                        mood5.setText("Veldig godt humør: " + antallMood5);
                    } else {
                        String error = jsonResponse.getString("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ArkivRequest arkivRequest = new ArkivRequest(""+ getSharedPreferences("MyPrefsName", MODE_PRIVATE)
                .getInt("UserID", 0),
                responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(arkivRequest);
    }
    // Sorterer etter måned/uke
    public void Sorter(View view) {
        String tag = view.getTag().toString();
        if (tag.equals("Uke")) {
            uke = true;
            ukeKnapp.setBackground(getResources().getDrawable(R.color.colorGreen));
            månedKnapp.setBackground(getResources().getDrawable(R.color.colorGray));

        }
        if (tag.equals("Måned")) {
            måned = true;
            ukeKnapp.setBackground(getResources().getDrawable(R.color.colorGray));
            månedKnapp.setBackground(getResources().getDrawable(R.color.colorGreen));
        }
        getArkiv();
    }
}
