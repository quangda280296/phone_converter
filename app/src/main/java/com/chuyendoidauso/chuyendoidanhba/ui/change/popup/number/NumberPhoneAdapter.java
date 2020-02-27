package com.chuyendoidauso.chuyendoidanhba.ui.change.popup.number;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseRecyclerAdapter;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemContentBinding;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemNumberContentBinding;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemNumberTitleBinding;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemTitleBinding;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.ui.info.InfoAdapter;

import java.util.List;

import static com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork.TYPE_CONTENT;
import static com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork.TYPE_TITLE;

public class NumberPhoneAdapter extends BaseRecyclerAdapter<HomeNetwork> {

    public NumberPhoneAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == InfoAdapter.InfoAdapterViewType.VIEW_ITEM_TITLE.value()) {
            return new TitleNumberItemHolder((ItemNumberTitleBinding) DataBindingUtil.inflate(getInflater(),
                    R.layout.item_number_title, parent, false));
        }
        return new ContentNumberItemHolder((ItemNumberContentBinding) DataBindingUtil.inflate(getInflater(),
                R.layout.item_number_content, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).TYPE == TYPE_TITLE) {
            return NumberAdapterViewType.VIEW_ITEM_TITLE.value();
        }
        if (dataList.get(position).TYPE == TYPE_CONTENT) {
            return NumberAdapterViewType.VIEW_ITEM_CONTENT.value();
        }
        return super.getItemViewType(position);
    }

    public enum NumberAdapterViewType {
        VIEW_ITEM_TITLE(1),
        VIEW_ITEM_CONTENT(2);
        int value;

        NumberAdapterViewType(int va) {
            value = va;
        }

        public int value() {
            return value;
        }
    }
}