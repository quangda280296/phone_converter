package com.chuyendoidauso.chuyendoidanhba.ui.recover;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseRecyclerAdapter;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemRecoveryBinding;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;


public class RecoveryAdapter extends BaseRecyclerAdapter<DataContact> {

    public RecoveryAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecoveryItemHolder((ItemRecoveryBinding) DataBindingUtil.inflate(getInflater(),
                R.layout.item_recovery, parent, false));
    }
}
