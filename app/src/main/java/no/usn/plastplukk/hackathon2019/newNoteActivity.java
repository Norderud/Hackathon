package no.usn.plastplukk.hackathon2019;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class newNoteActivity extends AppCompatActivity {
    TextView titleView, editTextView;
    boolean isBold;
    SpannableStringBuilder spanString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        titleView = findViewById(R.id.titleET);
        editTextView = findViewById(R.id.text);
        spanString = new SpannableStringBuilder();
        editTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editTextView.append("dåse");
                return true;
            }
        });

    }

    public void boldText(View view) {
        isBold = !isBold;

    }
}
