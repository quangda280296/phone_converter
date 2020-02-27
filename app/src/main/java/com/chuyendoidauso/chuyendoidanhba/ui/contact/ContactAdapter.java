package com.chuyendoidauso.chuyendoidanhba.ui.contact;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseRecyclerAdapter;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemContactBinding;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;


public class ContactAdapter extends BaseRecyclerAdapter<Contact> {

    public ContactAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactItemHolder((ItemContactBinding) DataBindingUtil.inflate(getInflater(),
                R.layout.item_contact, parent, false));
    }
}
