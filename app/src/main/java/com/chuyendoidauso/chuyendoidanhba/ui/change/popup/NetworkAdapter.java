package com.chuyendoidauso.chuyendoidanhba.ui.change.popup;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.models.Network;

import java.util.List;

public class NetworkAdapter extends Adapter<NetworkAdapter.NetworkAdapterViewHolder> {

    private NetworkItemListener listener;
    private List<Network> values;

    public interface NetworkItemListener {
        void onClick(List<Network> list);
    }

    public class NetworkAdapterViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvName;

        public NetworkAdapterViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            checkBox = view.findViewById(R.id.checkboxSelect);
        }
    }

    public NetworkAdapter(List<Network> list, NetworkItemListener networkItemListener) {
        this.values = list;
        this.listener = networkItemListener;
    }

    public NetworkAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NetworkAdapterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_network, viewGroup, false));
    }

    public void onBindViewHolder(final NetworkAdapterViewHolder viewHolder, int position) {
        if (this.values == null) return;
        final Network network = this.values.get(position);
        if (network != null) {
            viewHolder.tvName.setText(network.getName());
            viewHolder.checkBox.setChecked(network.isChecked());
            viewHolder.checkBox.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    network.setChecked(viewHolder.checkBox.isChecked());
                    if (listener != null) {
                        listener.onClick(values);
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.values == null ? 0 : this.values.size();
    }

    public void checkBoxAll(boolean isCheck) {
        for (Network checked : this.values) {
            checked.setChecked(isCheck);
        }
        notifyDataSetChanged();
    }

    public List<Network> getNetworkList() {
        return this.values;
    }
}
