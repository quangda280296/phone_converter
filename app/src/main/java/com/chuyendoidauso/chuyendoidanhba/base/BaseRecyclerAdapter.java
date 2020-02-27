package com.chuyendoidauso.chuyendoidanhba.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerItem;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    public ArrayList<T> dataList = new ArrayList<>();
    public OnClickListenerItem<T> onRecyclerViewItemClickListener;
    public boolean isListener = true;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (context == null) return;

        if (holder instanceof CommonHolder) {
            CommonHolder<T, ?> commonHolder = (CommonHolder<T, ?>) holder;

            if (dataList != null && dataList.size() > position && dataList.get(position) != null) {
                commonHolder.bindData(this, dataList, position);
                commonHolder.bindData(dataList.get(position), position);
            }

            // TODO: Move listener into holder to avoid create additional object
            if (onRecyclerViewItemClickListener != null && isListener) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataList != null) {
                            if (position < dataList.size()) {
                                int positionItem = holder.getAdapterPosition();
                                onRecyclerViewItemClickListener.onClick(dataList.get(positionItem));
                            }
                        }
                    }
                });
            }
        }
    }

    protected LayoutInflater getInflater() {
        return LayoutInflater.from(context);
    }

    public void setDataList(List<T> items) {
        dataList.clear();
        if (items != null) {
            dataList.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void addItemsAtFront(final List<T> items) {
        if (items == null) {
            return;
        }
        dataList.addAll(dataList.size(), items);
        notifyItemRangeChanged(dataList.size(), dataList.size() + items.size());

    }

    public void addItems(List<T> items, boolean isRefresh) {
        if (items == null) {
            return;
        }

        if (isRefresh && dataList != null) dataList.clear();
        dataList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (item == null) {
            return;
        }
        dataList.add(item);
        notifyDataSetChanged();
    }

    public void addItem(T item, int position) {
        if (item == null) {
            return;
        }
        dataList.add(position, item);
        notifyDataSetChanged();
    }

    public void deleteItem(T item) {
        if (item == null) {
            return;
        }
        dataList.remove(item);
        notifyDataSetChanged();
    }

    public void notifyRemoveListData(ArrayList<T> list) {
        getDataList().removeAll(list);
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return dataList;
    }

    public T getDataItem(int position) {
        if (dataList == null || dataList.isEmpty()) {
            return null;
        }
        return dataList.get(position);
    }

    public void setOnRecyclerViewItemClickListener(OnClickListenerItem<T> mOnRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
    }
}
