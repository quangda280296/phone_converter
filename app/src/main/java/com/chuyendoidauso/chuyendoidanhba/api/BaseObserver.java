package com.chuyendoidauso.chuyendoidanhba.api;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jacky on 1/12/18.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (t != null) {
            onResponse(t);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        //System.out.println("onError::::" +throwable);
        onFailure();
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onResponse(T t);

    protected abstract void onFailure();
}
