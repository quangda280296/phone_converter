package com.chuyendoidauso.chuyendoidanhba.ui.info;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseRecyclerAdapter;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemContentBinding;
import com.chuyendoidauso.chuyendoidanhba.databinding.ItemTitleBinding;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;

import static com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork.TYPE_CONTENT;
import static com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork.TYPE_TITLE;

public class InfoAdapter extends BaseRecyclerAdapter<HomeNetwork> {

    public InfoAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == InfoAdapterViewType.VIEW_ITEM_TITLE.value()) {
            return new TitleItemHolder((ItemTitleBinding) DataBindingUtil.inflate(getInflater(),
                    R.layout.item_title, parent, false));
        }
        return new ContentItemHolder((ItemContentBinding) DataBindingUtil.inflate(getInflater(),
                R.layout.item_content, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).TYPE == TYPE_TITLE) {
            return InfoAdapterViewType.VIEW_ITEM_TITLE.value();
        }
        if (dataList.get(position).TYPE == TYPE_CONTENT) {
            return InfoAdapterViewType.VIEW_ITEM_CONTENT.value();
        }
        return super.getItemViewType(position);
    }

    public enum InfoAdapterViewType {
        VIEW_ITEM_TITLE(1),
        VIEW_ITEM_CONTENT(2);
        int value;

        InfoAdapterViewType(int va) {
            value = va;
        }

        public int value() {
            return value;
        }
    }
}
