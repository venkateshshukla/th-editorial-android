package in.vshukla.thed;

import android.app.Application;
import android.util.Log;

/**
 *
 * Created by venkatesh on 30/11/16.
 */
public class Opinion extends Application {

    private static final String TAG = "Opinion";

    @Override
    public void onCreate() {
        Log.i(TAG, "Starting application.");
        super.onCreate();
    }
}
