package in.vshukla.thed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        key = intent.getExtras().getString(getString(R.string.extras_key));
        Log.d(TAG, "Received key : " + key);
    }
}
