package in.vshukla.thed;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neha on 4/11/15.
 */
public class Api {

    public static final String TAG = "API";
    private static final String BASEURL = "http://example.com" + "/api";

    private RequestQueue requestQueue;

    public Api(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Get a JSONObject containing the list of articles in the given responseListener.
     * @param timestamp The timestamp of latest available article.
     * @param responseListener This callback function will be called on success with JSONObject containing response.
     * @param errorListener This callback function will be called in case of error.
     * @throws JSONException
     */
    public void getArticleList(long timestamp,
                               Response.Listener<JSONObject> responseListener,
                               Response.ErrorListener errorListener)
            throws JSONException {
        Log.d(TAG, "Getting articles after timestamp : " + String.valueOf(timestamp));
        String url = BASEURL + "/list";
        JSONObject params = new JSONObject();
        params.put("timestamp", timestamp);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Get a JSONObject containing list of articles in the given responseListener. Timestamp of 7 days past is used.
     * @param responseListener This callback function will be called on success with JSONObject containing response.
     * @param errorListener This callback function will be called in case of error.
     * @throws JSONException
     */
    public void getArticleList(Response.Listener<JSONObject> responseListener,
                               Response.ErrorListener errorListener) throws JSONException {
        Log.d(TAG, "Getting articles from past 7 days.");
        long tsLast = (System.currentTimeMillis() / 1000) - (7 * 24 * 60 * 60 );
        getArticleList(tsLast, responseListener, errorListener);
    }

    /**
     * Get a JSONObject containing article text in responseListener.
     * @param key Key corresponding to an article obtained from prev API call.
     * @param responseListener This callback function will be called on success with JSONObject containing response.
     * @param errorListener This callback function will be called in case of error.
     * @throws JSONException
     */
    public void getArticleText(String key,
                               Response.Listener<JSONObject> responseListener,
                               Response.ErrorListener errorListener) throws JSONException {
        Log.d(TAG, "Getting articles associated with key : " + key);
        String url = BASEURL + "/news";
        JSONObject params = new JSONObject();
        params.put("key", key);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
}
