package no.usn.plastplukk.hackathon2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    ListView arkivListe;
    private LinearLayout layout;

    FloatingActionButton menu, arkiv, newNote, logout;
    boolean isFABOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateButtons();
        menu.setScaleY(-1); 
        prefs = getSharedPreferences("MyPrefsName", MODE_PRIVATE);

        layout = findViewById(R.id.mainLayout);
        arkivListe = findViewById(R.id.arkivListID);

        // Hvis du ikke er logget inn, send til logg inn aktivitet
        if (prefs.getString("Username", "").length() == 0){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        getArkiv();

    }

    // Oppretter arkive som en liste som skal vises
    private void defineList(final String[] tittelTabell, final String[] tekstTabell, final int[] moodTabell, final String[] datoTabell){
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, datoTabell);
        arkivListe.setAdapter(arrayAdapter);

        arkivListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent popupIntent = new Intent(MainActivity.this, Pop.class);
                layout.setAlpha((float) .5);
                popupIntent.putExtra("Tittel", tittelTabell[position]);
                popupIntent.putExtra("Tekst", tekstTabell[position]);
                popupIntent.putExtra("Mood", moodTabell[position]);
                popupIntent.putExtra("Dato", datoTabell[position]);
                startActivity(popupIntent);
            }
        });
    }

    // Henter arkivet med notater fra databasen
    private void getArkiv() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String[] tittelTabell, tekstTabell, datoTabell;
                    int[] moodTabell;
                    Log.e("Response", response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        JSONObject jsonObject = new JSONObject(jsonResponse.getString("array"));
                        tittelTabell = new String[jsonObject.length()];
                        tekstTabell = new String[jsonObject.length()];
                        datoTabell = new String[jsonObject.length()];
                        moodTabell = new int[jsonObject.length()];
                        for (int i=0; i<jsonObject.length(); i++){
                            JSONArray arkivArray = jsonObject.getJSONArray(""+(i+1));
                            tittelTabell[i] = arkivArray.getString(0);
                            tekstTabell[i] = arkivArray.getString(1);
                            moodTabell[i] = arkivArray.getInt(2);
                            datoTabell[i] = arkivArray.getString(3).substring(0,10);
                        }
                        defineList(tittelTabell, tekstTabell, moodTabell, datoTabell);
                    } else {
                        String error = jsonResponse.getString("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ArkivRequest arkivRequest = new ArkivRequest(""+prefs.getInt("UserID", 0),
                responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(arkivRequest);
    }


    @Override
    public void onResume() {
        layout.setAlpha(1);
        super.onResume();
    }

    // Oppretter knapper
    private void initiateButtons() {
        menu = findViewById(R.id.menu);
        newNote = findViewById(R.id.newNote);
        arkiv = findViewById(R.id.statistic);
        logout = findViewById(R.id.logoutFBA);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
    }

    // Viser menyen av floating action bars
    private void showFABMenu() {
        isFABOpen = true;
        logout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        arkiv.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        newNote.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        menu.setScaleY(1);
    }

    // Lukker menyen
    private void closeFABMenu(){
        isFABOpen=false;
        menu.animate().translationY(0);
        logout.animate().translationY(0);
        arkiv.animate().translationY(0);
        newNote.animate().translationY(0);
        menu.setScaleY(-1);
    }


    // Logger ut fra brukeren
    public void logout(View view) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Username", "");
        editor.apply();

        startActivity(new Intent(this, MainActivity.class));
    }

    // Gå til aktivitet for å opprette nytt innlegg
    public void newNote(View view) {
        Intent newNoteIntent = new Intent(this, NewNoteActivity.class);
        startActivity(newNoteIntent);
    }

    // Gå til statistikk aktiviteten
    public void statstikk(View view) {
        Intent statistikkIntent = new Intent(this, StatistikkActivity.class);
        startActivity(statistikkIntent);
    }
}
