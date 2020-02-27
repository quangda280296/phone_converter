package com.chuyendoidauso.chuyendoidanhba.ui.recover;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemRecoveryBinding;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;

public class RecoveryItemHolder extends CommonHolder<DataContact, ItemRecoveryBinding> {

    public RecoveryItemHolder(ItemRecoveryBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(final DataContact dataContact, int position) {
        getBinding().txtNameLetter.setText(String.valueOf(position + 1));
        getBinding().tvDate.setText(dataContact.date);
        getBinding().tvTotal.setText(getContext().getString(R.string.title_total_recovery_number,dataContact.getContactList().size()));
    }
}
