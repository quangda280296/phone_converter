package com.chuyendoidauso.chuyendoidanhba.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public abstract class CommonHolder<T, V extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private V binding;

    public CommonHolder(V binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public abstract void bindData(T t, int position);

    public void bindData(BaseRecyclerAdapter adapter, ArrayList<T> list, int position){

    }

    public V getBinding() {
        return binding;
    }

}