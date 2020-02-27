package com.chuyendoidauso.chuyendoidanhba.ui.change.popup.number;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerItem;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.models.Network;
import com.chuyendoidauso.chuyendoidanhba.ui.change.ChangeIsdnActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.change.popup.NetworkAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork.TYPE_TITLE;

public class NumberPhonePopup {

    private NumberPhoneAdapter numberPhoneAdapter;
    private ArrayList<HomeNetwork> listData = new ArrayList<>();
    private OnNumberPhoneListener onNumberPhoneListener;
    private boolean isChangeNetwork;
    private boolean isAddData;

    public NumberPhonePopup(OnNumberPhoneListener onNumberPhoneListener) {
        this.onNumberPhoneListener = onNumberPhoneListener;
    }

    @SuppressLint("StaticFieldLeak")
    public void show(Context context, final List<Network> networkList, final boolean isChange, View view) {
        this.isChangeNetwork = isChange;
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_select_phone_number, null);

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

        numberPhoneAdapter = new NumberPhoneAdapter(context);
        numberPhoneAdapter.setOnRecyclerViewItemClickListener(new OnClickListenerItem<HomeNetwork>() {
            @Override
            public void onClick(HomeNetwork homeNetwork) {
                homeNetwork.isCheckNumber = !homeNetwork.isCheckNumber;
                numberPhoneAdapter.notifyDataSetChanged();
                checkboxAll.setChecked(checkBoxAll(listData));
                if (onNumberPhoneListener != null) onNumberPhoneListener.refreshFilter();
            }
        });
        recyclerView.setAdapter(numberPhoneAdapter);

        popupWindow.setAnimationStyle(R.anim.fade_out);
        popupWindow.showAtLocation(view, 17, -2, -2);

        popupView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.checkboxAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (HomeNetwork network : numberPhoneAdapter.dataList) {
                    network.isCheckNumber = checkboxAll.isChecked();
                }
                if (onNumberPhoneListener != null) onNumberPhoneListener.refreshFilter();
                numberPhoneAdapter.notifyDataSetChanged();
            }
        });

        final InfoNumberChange responseBody = SharedReferenceUtils.get(context, InfoNumberChange.class.getName(), InfoNumberChange.class);
        new AsyncTask<ArrayList<HomeNetwork>, Void, ArrayList<HomeNetwork>>() {
            @Override
            protected ArrayList<HomeNetwork> doInBackground(ArrayList<HomeNetwork>... voids) {
                if (isChangeNetwork) {
                    listData.clear();
                }
                isAddData = ((!isChangeNetwork && listData.size() == 0) || (isChangeNetwork && listData.size() == 0));
                for (int i = 0; i < networkList.size(); i++) {
                    if (networkList.get(i).isChecked()) {
                        switch (i) {
                            case 0:
                                addData(responseBody.vietTel, TYPE_TITLE, "Nhà mạng Viettel");
                                break;
                            case 1:
                                addData(responseBody.mobilePhone, TYPE_TITLE, "Nhà mạng MobilePhone");
                                break;
                            case 2:
                                addData(responseBody.vinaPhone, TYPE_TITLE, "Nhà mạng VinaPhone");
                                break;
                            case 3:
                                addData(responseBody.vietnamobile, TYPE_TITLE, "Nhà mạng VietNamMobile");
                                break;
                            case 4:
                                addData(responseBody.gmobile, TYPE_TITLE, "Nhà mạng Gmobile");
                                break;
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HomeNetwork> list) {
                super.onPostExecute(list);
                numberPhoneAdapter.setDataList(listData);
                checkboxAll.setChecked(checkBoxAll(listData));
            }
        }.execute();
    }

    private void addData(ArrayList<HomeNetwork> list, int type, String title) {
        if (isAddData) {
            listData.add(new HomeNetwork(type, title));
            for (HomeNetwork homeNetwork : list) {
                homeNetwork.isCheckNumber = true;
                listData.add(homeNetwork);
            }
        }
    }

    private boolean checkBoxAll(List<HomeNetwork> list) {
        for (HomeNetwork homeNetwork : list) {
            if (homeNetwork.TYPE != TYPE_TITLE && !homeNetwork.isCheckNumber) {
                return false;
            }
        }
        return true;
    }

    public boolean isContactPhoneSelect(Contact contact) {
        for (HomeNetwork homeNetwork : listData) {
            if (!TextUtils.isEmpty(homeNetwork.phoneOld) && homeNetwork.isCheckNumber) {
                if (homeNetwork.phoneOld.equals(contact.getPrefixOld())) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface OnNumberPhoneListener {
        void refreshFilter();
    }
}
