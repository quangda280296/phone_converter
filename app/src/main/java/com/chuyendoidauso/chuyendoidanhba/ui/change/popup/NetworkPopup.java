package com.chuyendoidauso.chuyendoidanhba.ui.change.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.models.Network;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class NetworkPopup {

    private NetworkAdapter networkAdapter;
    private List<Network> networkListSelect;
    private OnNetworkPopupListener onNetworkPopupListener;
    public boolean isChangeNetwork;
    private boolean isChange;

    public NetworkPopup(List<Network> networkListSelect, OnNetworkPopupListener onNetworkPopupListener) {
        this.networkListSelect = networkListSelect;
        this.onNetworkPopupListener = onNetworkPopupListener;
    }


    public void show(Context context, View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_select_network, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final RecyclerView recyclerView = popupView.findViewById(R.id.recyclerView);
        final CheckBox checkboxAll = popupView.findViewById(R.id.checkboxAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        networkAdapter = new NetworkAdapter(networkListSelect, new NetworkAdapter.NetworkItemListener() {
            @Override
            public void onClick(List<Network> list) {
                checkBoxAll(networkAdapter.getNetworkList());
                isChange = true;
                if (onNetworkPopupListener != null) onNetworkPopupListener.refreshFilter();
            }
        });
        recyclerView.setAdapter(this.networkAdapter);
        checkboxAll.setChecked(checkBoxAll(this.networkListSelect));
        popupWindow.setAnimationStyle(R.anim.fade_out);
        popupWindow.showAtLocation(view, 17, -2, -2);

        popupView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChangeNetwork = isChange;
                isChange = false;
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.checkboxAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkAdapter.checkBoxAll(checkboxAll.isChecked());
                if (onNetworkPopupListener != null) onNetworkPopupListener.refreshFilter();
                isChange = true;
            }
        });
    }

    private boolean checkBoxAll(List<Network> list) {
        for (Network isChecked : list) {
            if (!isChecked.isChecked()) {
                return false;
            }
        }
        return true;
    }

    public NetworkAdapter getNetworkAdapter() {
        return networkAdapter;
    }

    public interface OnNetworkPopupListener {
        void refreshFilter();
    }
}
