package no.usn.plastplukk.hackathon2019;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.dankito.richtexteditor.android.RichTextEditor;

public class newNoteActivity extends AppCompatActivity {

    private RichTextEditor editor;
    private boolean isBold, isItalic;
    private Button boldButton, italicButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        editor = findViewById(R.id.editor);
        editor.setEditorFontSize(20);
        editor.setPadding((int) (4 * getResources().getDisplayMetrics().density));
        editor.focusEditorAndShowKeyboardDelayed();
    }

    public void sjekk(View view) {
        Toast.makeText(this, editor.getHtml(),Toast.LENGTH_LONG).show();
    }

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
    public void addImage(){
        StringBuilder html = new StringBuilder();
        html.append(editor.getHtml());
        html.append("<img src=\"https://itfag.usn.no/grupper/v19gr2/plast/itfag/uploadedFiles/JPEG_20190320_150316_1553090596743_.jpg\" width=\"70%\">");
        editor.setHtml(html.toString());
    }

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

    public void addPhoto(View view) {

    }
}

