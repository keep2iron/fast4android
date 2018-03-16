package io.github.keep2iron.android.net.interceptor;

import android.util.Log;

import com.orhanobut.logger.Logger;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2017/10/31 15:20
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger{
    @Override
    public void log(String message) {
        if (message.startsWith("{")) {
            Logger.json(message);
        } else {
            Log.e("keep2iron", message);
        }
    }
}
