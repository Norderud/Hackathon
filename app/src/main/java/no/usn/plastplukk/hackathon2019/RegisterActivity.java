package no.usn.plastplukk.hackathon2019;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText etUser;
    EditText etPassword1;
    EditText etPassword2;

    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etUser);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);

        bRegister = findViewById(R.id.bRegister);

    }

    public void registerUser(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetwork() == null){
            Toast.makeText(getApplicationContext(), "Du er ikke koblet til internett.", Toast.LENGTH_SHORT).show();
            return;
        }
        final String user = etUser.getText().toString();
        final String password1 = etPassword1.getText().toString();
        final String password2 = etPassword2.getText().toString();

        if(user.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            alertDialog("Alle feltene må fylles ut");
            return;
        }
        if(!isValidPassword(password1)){
            alertDialog("Passordet må inneholde minst en bokstav og 6 tall.");
            return;
        }
        if(!password1.equals(password2)){
            alertDialog("Passordene må være like");
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String error = jsonResponse.getString("error");
                    if(success){
                        Toast.makeText(RegisterActivity.this, "Registrering vellykket", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    } else{
                        alertDialog("Registrering feilet" + "\n"+ error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest(user, password1, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    private boolean isValidPassword(String password1) {
        String regex = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$";
        if(Pattern.matches(regex, password1)){
            return true;
        }
        return false;
    }

    private void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton("Prøv igjen", null)
                .create()
                .show();
    }

}