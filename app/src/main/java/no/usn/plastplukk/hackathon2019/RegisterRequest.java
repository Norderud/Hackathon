package no.usn.plastplukk.hackathon2019;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
// Sender forespørsel til php-scriptet som tar av seg registrering
public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = "https://itfag.usn.no/~161741/hackathon2019/register.php";
    private Map<String, String> params;

    public RegisterRequest(String user, String password, String imageFileName, String encodedImage, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });
        params = new HashMap<>();
        params.put("user", user);
        params.put("password", password);
        params.put("name", imageFileName);
        params.put("image", encodedImage);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
