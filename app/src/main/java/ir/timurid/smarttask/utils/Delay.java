package ir.timurid.smarttask.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Delay {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public static DelayExecution forTime(long millis) {
        Observable<Long> delayObservable = Observable.timer(millis, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return runnable -> delayObservable.subscribe(aLong -> runnable.run());
    }

    @FunctionalInterface
    public interface DelayExecution {

        void andThen(Runnable runnable);
    }
}
