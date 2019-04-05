package no.usn.plastplukk.hackathon2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton menu, arkiv, newNote, logout;
    boolean isFABOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateButtons();
    }

    private void initiateButtons() {
        menu = findViewById(R.id.fab);
        newNote = findViewById(R.id.fab1);
        arkiv = findViewById(R.id.fab2);
        logout = findViewById(R.id.fab3);
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

    private void showFABMenu() {
        isFABOpen = true;
        logout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        arkiv.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        newNote.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        menu.setScaleY(1);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        menu.animate().translationY(0);
        logout.animate().translationY(0);
        arkiv.animate().translationY(0);
        newNote.animate().translationY(0);
    }

    public void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

    }
}
