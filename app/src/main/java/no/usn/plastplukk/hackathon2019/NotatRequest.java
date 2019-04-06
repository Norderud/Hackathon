package no.usn.plastplukk.hackathon2019;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
// Klasse for å håndtere ny notat-request som skal sendes til database
public class NotatRequest extends StringRequest {
    private static final String NOTAT_REQUEST_URL = "https://itfag.usn.no/~161741/hackathon2019/insertNotat.php";
    private Map<String, String> params;

    public NotatRequest(String brukerid, String tittel, String tekst, String mood, Response.Listener<String> listener) {
        super(Request.Method.POST, NOTAT_REQUEST_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", error.toString());
            }
        });

        params = new HashMap<>();
        params.put("brukerid", brukerid);
        params.put("tittel", tittel);
        params.put("tekst", tekst);
        params.put("mood", mood);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
