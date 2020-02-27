package com.chuyendoidauso.chuyendoidanhba.commons;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListener;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerDialog;

public class DialogUtils {

    public static void showDialogConfirm(Context context, String str, String str2, String str3, final OnClickListener onClickListener) {
        new Builder(context).setTitle(str).setMessage(str2).setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
                dialogInterface.dismiss();
            }
        }).setNegativeButton(context.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void showDialogConfirm(Context context,
                                         String title,
                                         String message,
                                         String positiveButton,
                                         String negativeButton,
                                         final OnClickListenerDialog onClickListener) {
        new Builder(context).setTitle(title).setMessage(message).setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onClickListener != null) {
                    onClickListener.onClickPositiveButton();
                }
                dialogInterface.dismiss();
            }
        }).setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onClickListener != null) {
                    onClickListener.onClickNegativeButton();
                }
                dialogInterface.dismiss();
            }
        }).show();
    }


    public static void showDialogConfirm(Context context, String str, String str2, String str3) {
        new Builder(context).setTitle(str).setMessage(str2).setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }


    public static void showDialogConfirm(Context context, int i, int i2, int i3, final OnClickListener onClickListener) {
        new Builder(context).setTitle(context.getString(i)).setMessage(context.getString(i2)).setPositiveButton(context.getString(i3), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
                dialogInterface.dismiss();
            }
        }).setNegativeButton(context.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void showDialog(Context context, String str, String str2, String str3) {
        new Builder(context).setTitle(str).setMessage(str2).setPositiveButton(str3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void showDialog(Context context, int i, int i2, int i3) {
        new Builder(context).setTitle(context.getString(i)).setMessage(context.getString(i2)).setPositiveButton(context.getString(i3), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
