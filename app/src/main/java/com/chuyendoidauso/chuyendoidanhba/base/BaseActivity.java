package com.chuyendoidauso.chuyendoidanhba.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseActivity<T extends ViewDataBinding> extends BaseCoreActivity {

    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        initView();
        initData();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    protected T getBinding() {
        return binding;
    }
}
