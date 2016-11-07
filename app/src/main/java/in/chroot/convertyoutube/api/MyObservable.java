package in.chroot.convertyoutube.api;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yogi on 07/11/16.
 */
public  abstract class MyObservable<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage());
    }

    @Override
    public void onCompleted() {
        
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onError(String message);
}