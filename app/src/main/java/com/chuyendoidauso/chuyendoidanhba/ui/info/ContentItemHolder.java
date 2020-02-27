package com.chuyendoidauso.chuyendoidanhba.ui.info;

import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemContentBinding;

class ContentItemHolder extends CommonHolder<HomeNetwork, ItemContentBinding> {

    public ContentItemHolder(ItemContentBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(final HomeNetwork homeNetwork, int position) {
        getBinding().tvPrefixNew.setText(homeNetwork.phoneNew);
        getBinding().tvPrefixOld.setText(homeNetwork.phoneOld);
        getBinding().tvDate.setText(homeNetwork.date);
    }
}
