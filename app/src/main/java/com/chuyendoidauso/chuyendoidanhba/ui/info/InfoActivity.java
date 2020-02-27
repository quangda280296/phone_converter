package com.chuyendoidauso.chuyendoidanhba.ui.info;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.DialogUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.NetworkUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityInfoBinding;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerDialog;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.notify.NotifyUtils;

import java.util.ArrayList;

public class InfoActivity extends BaseActivity<ActivityInfoBinding> {

    private ArrayList<HomeNetwork> listData = new ArrayList<>();
    private boolean isError;
    private WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    public void initView() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //this.infoAdapter = new InfoAdapter(this);
        //binding.recyclerView.setAdapter(this.infoAdapter);
        AdsUtils.getInstance().getIdAds(new AdsUtils.OnCallBackData() {
            @Override
            public void onData(AdsModel adsModel) {
                if (adsModel != null) {
                    NotifyUtils.addNotify(InfoActivity.this, adsModel.timeNotify);
                    binding.message.setText(adsModel.messageLCD);
                }
            }
        });

        this.webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        //webView.setWebViewClient(webViewClient);

        loadWebView();

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                binding.loading.setVisibility(View.GONE);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                findViewById(R.id.loading).setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                isError = true;
                showDialogError();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.loading).setVisibility(View.GONE);
                if (!isError) {
                    webView.setVisibility(View.VISIBLE);
                }

            }
        });
        AdsUtils.getInstance().initAds(this);
    }

    private void showDialogError() {
        DialogUtils.showDialogConfirm(InfoActivity.this,
                getString(R.string.title_info),
                getString(R.string.msg_confirm_calendar_connect_internet),
                getString(R.string.btn_try),
                getString(R.string.btn_cancel),
                new OnClickListenerDialog() {

                    @Override
                    public void onClickPositiveButton() {
                        loadWebView();
                    }

                    @Override
                    public void onClickNegativeButton() {
                        finish();
                    }
                });
    }

    private void loadWebView() {
        final AdsModel adsModel = SharedReferenceUtils.get(InfoActivity.this, AdsModel.class.getName(), AdsModel.class);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsModel != null && !TextUtils.isEmpty(adsModel.linkLichChuyenDoi) && NetworkUtils.isNetworkAvailable(InfoActivity.this)) {
                    isError = false;
                    webView.loadUrl(adsModel.linkLichChuyenDoi);
                } else {
                    showDialogError();
                }
            }
        }, 333L);
    }

    @Override
    public void initData() {
        getInfo();
    }

    private void getInfo() {
//        AppApi.getInstance().getInfo(new BaseObserver<InfoNumberChange>() {
//
//            @Override
//            protected void onResponse(final InfoNumberChange responseBody) {
//                SharedReferenceUtils.put(InfoActivity.this, InfoNumberChange.class.getName(), responseBody);
//                showData(responseBody);
//            }
//
//            @Override
//            protected void onFailure() {
//                InfoNumberChange infoNumberChange = SharedReferenceUtils.get(InfoActivity.this, InfoNumberChange.class.getName(), InfoNumberChange.class);
//                if (infoNumberChange == null) {
//                    new AlertDialog.Builder(InfoActivity.this)
//                            .setTitle("Thông báo")
//                            .setMessage("Xin vui lòng kiểm tra kết nối internet của bạn !")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    finish();
//                                }
//                            }).show();
//                } else {
//                    showData(infoNumberChange);
//                }
//            }
//        });
    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void showData(final InfoNumberChange responseBody) {
//        new AsyncTask<ArrayList<HomeNetwork>, Void, ArrayList<HomeNetwork>>() {
//            @Override
//            protected ArrayList<HomeNetwork> doInBackground(ArrayList<HomeNetwork>... voids) {
//                addData(responseBody.vietTel, TYPE_TITLE, "Nhà mạng Viettel");
//                addData(responseBody.mobilePhone, TYPE_TITLE, "Nhà mạng MobilePhone");
//                addData(responseBody.vinaPhone, TYPE_TITLE, "Nhà mạng VinaPhone");
//                addData(responseBody.vietnamobile, TYPE_TITLE, "Nhà mạng VietNamMobile");
//                addData(responseBody.gmobile, TYPE_TITLE, "Nhà mạng Gmobile");
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<HomeNetwork> list) {
//                super.onPostExecute(list);
//                //System.out.println("listData" + listData.size());
//                infoAdapter.setDataList(listData);
//                binding.loading.setVisibility(View.GONE);
//            }
//        }.execute();
//    }

    private void addData(ArrayList<HomeNetwork> list, int type, String title) {
        listData.add(new HomeNetwork(type, title));
        for (HomeNetwork homeNetwork : list) {
            listData.add(homeNetwork);
        }
    }

    @Override
    protected void onDestroy() {
        AdsUtils.getInstance().displayInterstitial();
        super.onDestroy();
    }
}
