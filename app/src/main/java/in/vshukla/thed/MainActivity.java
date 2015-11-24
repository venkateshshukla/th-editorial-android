package in.vshukla.thed;

import android.app.Activity;
import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final TextView tv =(TextView) findViewById(R.id.textView);

        String url = "http://th-opinion.appspot.com/api/list";
        Calendar c=Calendar.getInstance();
        long timestamp = c.get(Calendar.SECOND);
        HashMap<String,String> params= new HashMap<String,String>();
        params.put("Timestamp", "1444463177");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url,new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ar = response.getJSONArray("entries");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject p = ar.getJSONObject(i);
                                String key = p.getString("key");
                                String author = p.getString("author");
                                String title = p.getString("title");
                                String kind = p.getString("kind");
                                String body = p.getString("body");
                                String date = p.getString("print_date");
                                ContentValues values = new ContentValues();

                                jsonResponse = "";
                                jsonResponse += "Title" + title + "\n\n";
                                jsonResponse += "Author" + author + "\n\n";
                                jsonResponse += "Date" + date + "\n\n";

                                tv.setText(jsonResponse);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener()

                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv.setText("Error");
                    }


                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
