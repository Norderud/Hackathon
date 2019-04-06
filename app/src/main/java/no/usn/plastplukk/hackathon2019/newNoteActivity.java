package no.usn.plastplukk.hackathon2019;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.dankito.richtexteditor.android.RichTextEditor;

import org.json.JSONException;
import org.json.JSONObject;

public class NewNoteActivity extends AppCompatActivity {
    //Deklarerer GUI-elementer
    private RichTextEditor editor;
    private EditText titleText;
    private SeekBar moodbar;
    private Button boldButton, italicButton;
    private boolean isBold, isItalic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleText = findViewById(R.id.titleText);
        moodbar = findViewById(R.id.moodbar);

        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        editor = findViewById(R.id.editor);
        editor.setEditorFontSize(20);
        editor.setPadding((int) (4 * getResources().getDisplayMetrics().density));
        editor.focusEditorAndShowKeyboardDelayed();
    }

    //Metode for å gjøre tekst bold
    public void boldText(View view) {
        if(!isBold){
            boldButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            editor.setBold();
            isBold = true;
        } else {
            boldButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
            editor.setBold();
            isBold = false;
        }
    }
    //Metode for å gjøre tekst italic
    public void italicText(View view) {
        if(!isItalic){
            italicButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            editor.setItalic();
            isItalic = true;
        } else {
            italicButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
            editor.setItalic();
            isItalic = false;
        }
    }
    // Metode for å legge til bilde med URL
    public void addImage(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Last opp bilde");
        alert.setMessage("Oppgi URL");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputText = input.getText().toString();
                String html = editor.getHtml();
                html += "<p><img src=\"" + inputText +"\" width=\"80%\"</p>";
                editor.setHtml(html);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();


    }
    // Metode for å lagre notat i database og sende deg tilbake til MainActivity
    public void lagreNotat(View view) {
        String title = titleText.getText().toString();
        String mood = moodbar.getProgress() +"";
        String htmlText = editor.getHtml();
        NotatRequest notatRequest = new NotatRequest(getSharedPreferences("MyPrefsName", MODE_PRIVATE).getInt("UserID", 0)+"", title, htmlText, mood, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(NewNoteActivity.this, MainActivity.class);
                        NewNoteActivity.this.startActivity(intent);

                    } else {
                        Toast.makeText(NewNoteActivity.this, "Noe gikk galt", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(NewNoteActivity.this);
        queue.add(notatRequest);
    }
}

