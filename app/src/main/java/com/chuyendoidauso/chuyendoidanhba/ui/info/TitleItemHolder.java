package com.chuyendoidauso.chuyendoidanhba.ui.info;

import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemTitleBinding;

import java.util.ArrayList;

class TitleItemHolder extends CommonHolder<HomeNetwork, ItemTitleBinding> {

    public TitleItemHolder(ItemTitleBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(final HomeNetwork homeNetwork, int position) {
        getBinding().title.setText(homeNetwork.title);
    }
}