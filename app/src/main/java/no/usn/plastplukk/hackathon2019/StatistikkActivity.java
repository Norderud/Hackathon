package no.usn.plastplukk.hackathon2019;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatistikkActivity extends AppCompatActivity {

    TextView mood1, mood2, mood3, mood4, mood5;
    int antallMood1, antallMood2, antallMood3, antallMood4, antallMood5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistikk);

        mood1 = findViewById(R.id.mood1);
        mood2 = findViewById(R.id.mood2);
        mood3 = findViewById(R.id.mood3);
        mood4 = findViewById(R.id.mood4);
        mood5 = findViewById(R.id.mood5);

        getArkiv();

    }

    private void getArkiv() {
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
                        for (int i=0; i<jsonObject.length(); i++){
                            JSONArray arkivArray = jsonObject.getJSONArray(""+(i+1));
                            moodTabell[i] = arkivArray.getInt(2);
                            datoTabell[i] = arkivArray.getString(3).substring(0, 10);
                            Log.e("Mood", ""+i+": "+moodTabell[i]);
                            switch(moodTabell[i]){
                                case 1: antallMood1++; break;
                                case 2: antallMood2++; break;
                                case 3: antallMood3++; break;
                                case 4: antallMood4++; break;
                                case 5: antallMood5++; break;
                            }
                        }
                        mood1.setText("Mood 1: " + antallMood1);
                        mood2.setText("Mood 2: " + antallMood2);
                        mood3.setText("Mood 3: " + antallMood3);
                        mood4.setText("Mood 4: " + antallMood4);
                        mood5.setText("Mood 5: " + antallMood5);
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

    public void Uke(View view) {
    }
}
