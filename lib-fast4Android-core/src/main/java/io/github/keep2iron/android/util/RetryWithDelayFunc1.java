package io.github.keep2iron.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by 薛世君
 * Date : 2016/11/11
 * Email : 497881309@qq.com
 */
public class RetryWithDelayFunc1 implements Function<Observable<? extends Throwable>, Observable<?>> {
    private int time;

    private int mRetryTime;
    private long mDelayMillions;

    private static List<Class<? extends Throwable>> mIgnoreThrowable = new ArrayList<>();

    public RetryWithDelayFunc1(int retryTime, long delayMillions) {
        this.mRetryTime = retryTime;
        this.mDelayMillions = delayMillions;
    }

    public static void init(Class<? extends Throwable>... clazz) {
        if (clazz != null) {
            for (Class<? extends Throwable> cls : clazz) {
                mIgnoreThrowable.add(cls);
            }
        }
    }

    public RetryWithDelayFunc1() {
        this(3, 1000);
    }

    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                for (Class<? extends Throwable> cls : mIgnoreThrowable) {
                    if (throwable.getClass() == cls) {
                        return Observable.error(throwable);
                    }
                }

                if (++time < mRetryTime) {
                    return Observable.timer(mDelayMillions, TimeUnit.MILLISECONDS);
                } else {
                    return Observable.error(throwable);
                }
            }
        });
    }
}