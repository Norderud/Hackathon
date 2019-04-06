package no.usn.plastplukk.hackathon2019;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//Klasse for å sende forespørsel til php-scriptet om å hente notater fra databasen
public class ArkivRequest extends StringRequest {
    private static final String ARKIV_REQUEST_URL = "https://itfag.usn.no/~161741/hackathon2019/getArkiv.php";
    private Map<String, String> params;

    public ArkivRequest(String brukerid, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Request.Method.POST, ARKIV_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("brukerid", brukerid);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
