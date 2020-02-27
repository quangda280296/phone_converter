package com.chuyendoidauso.chuyendoidanhba.ui.contact;

import com.chuyendoidauso.chuyendoidanhba.base.CommonHolder;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemContactBinding;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;

public class ContactItemHolder extends CommonHolder<Contact, ItemContactBinding> {

    public ContactItemHolder(ItemContactBinding binding) {
        super(binding);
    }

    @Override
    public void bindData(final Contact contact, int position) {
        getBinding().tvName.setText(contact.getName());
        getBinding().tvPhone.setText(contact.getNewIsdn());
        getBinding().txtNameLetter.setText(String.valueOf(position + 1));
    }
}
