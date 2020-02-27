package com.chuyendoidauso.chuyendoidanhba.api;
import io.reactivex.disposables.Disposable;

public interface OnLoadingDataFromServer {
    void onShowLoading(Disposable disposable);
    void onDismissLoading(boolean isError);
}
