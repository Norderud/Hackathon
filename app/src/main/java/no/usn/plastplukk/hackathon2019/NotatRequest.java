package no.usn.plastplukk.hackathon2019;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NotatRequest extends StringRequest {
    private static final String NOTAT_REQUEST_URL = "https://itfag.usn.no/~161741/hackathon2019/insertNotat.php";
    private Map<String, String> params;

    public NotatRequest(String brukerid, String tittel, String tekst, String mood, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Request.Method.POST, NOTAT_REQUEST_URL, listener, null);

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
