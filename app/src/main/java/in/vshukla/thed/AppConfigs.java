package in.vshukla.thed;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by venkatesh on 24/11/16.
 */

public class AppConfigs {

    private static final String TAG  = "AppConfigs";
    private static final String CONFIG_FILE = "build-configs.properties";

    private static Properties properties;

    public static String getProperty(String key, Context context) {
        if (properties == null) {
            try {
                refreshProperties(context);
            } catch (IOException e) {
                Log.e(TAG, "Error reading the properties {}", e);
            }
        }
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    private static void refreshProperties(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(CONFIG_FILE);
        properties = new Properties();
        properties.load(inputStream);
    }
}
