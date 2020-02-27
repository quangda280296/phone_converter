package com.chuyendoidauso.chuyendoidanhba.ui.change.popup.number;

import android.view.View;

import com.chuyendoidauso.chuyendoidanhba.base.BaseRecyclerAdapter;
import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemNumberContentBinding;

import java.util.ArrayList;

class ContentNumberItemHolder extends CommonHolder<HomeNetwork, ItemNumberContentBinding> {

    public ContentNumberItemHolder(ItemNumberContentBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(HomeNetwork homeNetwork, int position) {

    }

    @Override
    public void bindData(final BaseRecyclerAdapter adapter,final  ArrayList<HomeNetwork> list, final int position) {
        super.bindData(adapter, list, position);
        getBinding().checkboxSelect.setChecked(list.get(position).isCheckNumber);
        getBinding().tvName.setText(list.get(position).phoneOld);
        getBinding().checkboxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.onRecyclerViewItemClickListener.onClick(list.get(position));
            }
        });
    }
}
