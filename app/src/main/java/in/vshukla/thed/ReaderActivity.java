package in.vshukla.thed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.e(TAG, "No Bundle received. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        key = extras.getString(getString(R.string.extras_key));
        if (key == null) {
            Log.e(TAG, "Received null key. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Received key : " + key);
    }
}
