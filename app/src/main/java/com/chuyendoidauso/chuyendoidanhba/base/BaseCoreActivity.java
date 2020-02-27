package com.chuyendoidauso.chuyendoidanhba.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseCoreActivity extends AppCompatActivity {

    public void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }

    public void openActivity(Class<? extends Activity> pClass, boolean isFinish) {
        openActivity(pClass);
        if (isFinish) {
            finish();
        }
    }

    public void openActivity(Class<? extends Activity> pClass, Bundle bundle) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
