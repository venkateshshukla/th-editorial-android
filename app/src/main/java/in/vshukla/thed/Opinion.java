package in.vshukla.thed;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import in.vshukla.thed.utils.AppConstants;
import in.vshukla.thed.utils.AppUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *
 * Created by venkatesh on 30/11/16.
 */
public class Opinion extends Application {

    private static final String TAG = "Opinion";

    @Override
    public void onCreate() {
        Log.i(TAG, "Starting application.");
        Realm.init(this);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .name("opinion.realm")
                .schemaVersion(1);
        Realm.setDefaultConfiguration(builder.build());
        super.onCreate();
    }
}
