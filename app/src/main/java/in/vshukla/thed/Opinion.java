package in.vshukla.thed;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 *
 * Created by venkatesh on 30/11/16.
 */
public class Opinion extends Application {

    private static final String TAG = "Opinion";

    @Override
    public void onCreate() {
        Log.i(TAG, "Starting application.");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate();
    }
}
