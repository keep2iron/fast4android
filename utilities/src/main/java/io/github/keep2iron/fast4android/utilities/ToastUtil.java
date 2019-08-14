package io.github.keep2iron.fast4android.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2017/08/21 10:07
 */

public class ToastUtil {
  private static Context mContext;
  private static WeakHandler mHandler;

  static class MyRunnable implements Runnable {
    private static MyRunnable myRunnable = new MyRunnable();
    private String mMsg;
    private int mShowLength;
    private int mGravity;
    private int dx;
    private int dy;

    private MyRunnable() {
    }

    public static MyRunnable create(String msg) {
      return create(msg, Toast.LENGTH_SHORT);
    }

    public static MyRunnable create(String msg, int showLength) {
      myRunnable.mMsg = msg;
      myRunnable.mShowLength = showLength;
      return myRunnable;
    }

    @Override
    public void run() {
      Toast toast = Toast.makeText(mContext, mMsg, mShowLength);
      if (mGravity != -1) {
        toast.setGravity(mGravity, dx, dy);
      }
      toast.show();
    }
  }

  public static void init(Context context) {
    mContext = context.getApplicationContext();

    mHandler = new WeakHandler();
  }

  public static void S(String msg) {
    S(-1, -1, -1, msg);
  }

  public static void S(int gravity, int dx, int dy, String msg) {
    MyRunnable runnable = MyRunnable.create(msg);
    runnable.mGravity = gravity;
    runnable.dx = dx;
    runnable.dx = dy;
    mHandler.removeCallbacks(runnable);
    mHandler.postDelayed(runnable, 300);
  }

  public static void L(String msg) {
    S(-1, -1, -1, msg);
  }

  public static void L(int gravity, int dx, int dy, String msg) {
    MyRunnable runnable = MyRunnable.create(msg, Toast.LENGTH_LONG);
    runnable.mGravity = gravity;
    runnable.dx = dx;
    runnable.dx = dy;
    mHandler.removeCallbacks(runnable);
    mHandler.postDelayed(runnable, 300);
  }
}
