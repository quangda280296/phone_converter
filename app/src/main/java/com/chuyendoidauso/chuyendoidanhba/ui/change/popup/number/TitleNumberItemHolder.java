package com.chuyendoidauso.chuyendoidanhba.ui.change.popup.number;

import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemNumberTitleBinding;

class TitleNumberItemHolder extends CommonHolder<HomeNetwork, ItemNumberTitleBinding> {

    public TitleNumberItemHolder(ItemNumberTitleBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(final HomeNetwork homeNetwork, int position) {
        getBinding().title.setText(homeNetwork.title);
    }
}