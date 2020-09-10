package ir.timurid.smarttask.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Delay {

    public static Observable<Long> getObservableForTime(long millis) {
        return Observable.timer(millis, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public static DelayExecution forTime(long millis) {
        Observable<Long> delayObservable = getObservableForTime(millis);

        return runnable -> delayObservable.subscribe(aLong -> runnable.run());
    }


    @FunctionalInterface
    public interface DelayExecution {

        void andThen(Runnable runnable);
    }
}
