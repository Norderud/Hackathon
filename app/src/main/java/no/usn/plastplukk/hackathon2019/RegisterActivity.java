package no.usn.plastplukk.hackathon2019;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText etUser;
    EditText etPassword1;
    EditText etPassword2;

    Button bRegister;

    final static int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    String imageFileName;
    static String currentPhotoPath;
    public static String PHOTOPATH = "photoPath", IMAGEFILENAME = "imageFileName",
            IMAGE_WIDTH = "imageWidth", IMAGE_HEIGHT = "imageHeight";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    Button confirmPictureButton;
    ImageButton takePictureButton;
    private String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etUser);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        imageView = findViewById(R.id.photoDisplay);
        prefs = getSharedPreferences("MyPrefsName", MODE_PRIVATE);
        editor = prefs.edit();

        takePictureButton = findViewById(R.id.bTakePicture);
        confirmPictureButton = findViewById(R.id.videreFraKamera);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
            }
        });
        confirmPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });
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
            alertDialog("Alle feltene må fylles ut", "OK", null, null);
            return;
        }
        if(!isValidPassword(password1)){
            alertDialog("Passordet må inneholde minst en bokstav og 6 tall.", "OK", null, null);
            return;
        }
        if(!password1.equals(password2)){
            alertDialog("Passordene må være like", "OK", null, null);
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("Response", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String error = jsonResponse.getString("error");
                    if(success){
                        Toast.makeText(RegisterActivity.this, "Registrering vellykket", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    } else{
                        alertDialog("Registrering feilet" + "\n"+ error, "Prøv igjen", null, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest(user, password1, imageFileName, encodedImage, responseListener);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();

                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "no.usn.plastplukk.hackathon2019.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) { // Only create file if external storage exists
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                    + "_" + String.valueOf(Calendar.getInstance().getTimeInMillis());
            imageFileName = String.format("JPEG_%s_.jpg", timeStamp);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = new File(storageDir, imageFileName);
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            editor.putString("ImageFileName", imageFileName);
            editor.apply();
            return image;
        }
        throw new IOException();
    }

    private void requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "Alle rettigheter er godtatt.", Toast.LENGTH_SHORT).show();
                            dispatchTakePictureIntent();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            alertDialog("Manglende Rettighet! Aktiver lokasjonstjenester og lagring",
                                    "Instillinger", Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Feilet", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            confirmPictureButton.setVisibility(View.VISIBLE);
            Bitmap bitmap = HelpFunctions.loadImageFromFile(imageView, currentPhotoPath, imageView.getWidth(), imageView.getHeight());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            editor.putString("currentPhotoPath", currentPhotoPath);
            editor.putString("EncodedImage", encodedImage);
            editor.apply();
        } else {
            recreate();
        }
    }




    private void alertDialog(String message, String buttonName, final String settings, final Uri uri){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(buttonName, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (settings == null)
                            return;
                        Intent intent = new Intent(settings);
                        if (uri != null)
                            intent.setData(uri);
                        startActivityForResult(intent, 233);
                    }
                })
                .create()
                .show();
    }

}
