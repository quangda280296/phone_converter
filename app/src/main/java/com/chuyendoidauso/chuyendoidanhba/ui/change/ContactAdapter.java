package com.chuyendoidauso.chuyendoidanhba.ui.change;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.commons.DialogUtils;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends Adapter<ContactAdapter.ViewHolder> {

    private final List<Contact> filter;
    private final ContactItemListener listener;
    private final List<Contact> values;
    private Context context;
    private DecimalFormat formatter = new DecimalFormat("#,####,###");
    boolean isRecovery;

    public interface ContactItemListener {
        void onClick(List<Contact> list);
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvName;
        TextView tvNetwork;
        TextView tvPhone;
        TextView tvPrefixNew;
        TextView tvPrefixOld;
        TextView txtNameLetter;
        ImageView typeImage;
        TextView tvDate;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvPrefixOld = view.findViewById(R.id.tvPrefixOld);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvPrefixNew = view.findViewById(R.id.tvPrefixNew);
            tvNetwork = view.findViewById(R.id.tvNetwork);
            checkBox = view.findViewById(R.id.cbSelect);
            txtNameLetter = view.findViewById(R.id.txtNameLetter);
            typeImage = view.findViewById(R.id.typeImage);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }

    public ContactAdapter(Context context, List<Contact> list, ContactItemListener contactItemListener) {
        this.context = context;
        this.values = list;
        this.listener = contactItemListener;
        this.filter = initFilter(list);
    }

    public ContactAdapter(Context context, List<Contact> list, ContactItemListener contactItemListener, boolean isRecovery) {
        this.context = context;
        this.values = list;
        this.listener = contactItemListener;
        this.filter = initFilter(list);
        this.isRecovery = isRecovery;
    }

    private List<Contact> initFilter(List<Contact> list) {
        List<Contact> arrayList = new ArrayList();
        for (Contact add : list) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_change, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (this.filter == null) return;
        final Contact contact = this.filter.get(position);
        if (contact != null && contact.isShow) {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText(contact.getName());
            if (!TextUtils.isEmpty(contact.date) && contact.date.length() > 5) {
                // viewHolder.txtNameLetter.setText(String.valueOf(contact.date.substring(0, 5)));//contact.getName().trim().substring(0, 1)
                viewHolder.txtNameLetter.setText(String.valueOf(position + 1));
                viewHolder.txtNameLetter.setBackgroundResource(contact.isValidate ? R.drawable.bg_circle : R.drawable.bg_circle_red);
            } else {
                viewHolder.txtNameLetter.setText(String.valueOf(contact.date));
            }
            //StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder.append("(");

            //stringBuilder.append(numberOld);


            //stringBuilder = new StringBuilder();
            //stringBuilder.append(contact.getPrefixNew());
            //viewHolder.tvPhone.setText(contact.getSuffixNumber());
            //stringBuilder.append(")");
            //String numberOld = contact.getPrefixOld() + formatter.format(Double.parseDouble(contact.getSuffixNumber()));
            // 11 -> 4.4.3
            // 0162.3123.456

            if (!isRecovery) {
                viewHolder.tvPrefixOld.setText(Html.fromHtml(String.format(context.getString(R.string.test),
                        contact.getPrefixOld(),//+ contact.getSuffixNumber().substring(0, 1)+ "."
                        "." + formatter.format(Double.parseDouble(contact.getSuffixNumber())))));//
                viewHolder.tvPrefixNew.setText(Html.fromHtml(String.format(context.getString(R.string.test),
                        contact.getPrefixNew(),// + contact.getSuffixNumber().substring(0, 1) + "."
                        "." + formatter.format(Double.parseDouble(contact.getSuffixNumber())))));
            } else {
                viewHolder.tvPrefixNew.setText(Html.fromHtml(String.format(context.getString(R.string.test),
                        contact.getPrefixOld(),//+ contact.getSuffixNumber().substring(0, 1)+ "."
                        "." + formatter.format(Double.parseDouble(contact.getSuffixNumber())))));//
                viewHolder.tvPrefixOld.setText(Html.fromHtml(String.format(context.getString(R.string.test),
                        contact.getPrefixNew(),// + contact.getSuffixNumber().substring(0, 1) + "."
                        "." + formatter.format(Double.parseDouble(contact.getSuffixNumber())))));
            }
            viewHolder.tvNetwork.setText(contact.getNetwork());
            viewHolder.checkBox.setChecked(contact.isChecked());
            viewHolder.checkBox.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (contact.isValidate) {
                        contact.setChecked(viewHolder.checkBox.isChecked());
                        if (listener != null) {
                            listener.onClick(getContactList());
                        }
                    } else {
                        viewHolder.checkBox.setChecked(false);
                        //ToastUtils.showToastShort(String.format(AppNumberChanger.newInstance().getString(R.string.title_confirm_number_validate), contact.getPrefixOld(), contact.date));
                        DialogUtils.showDialogConfirm(context, AppNumberChanger.newInstance().getString(R.string.title_info),
                                String.format(AppNumberChanger.newInstance().getString(R.string.title_confirm_number_validate),
                                        contact.getPrefixOld(), contact.date, contact.date),
                                AppNumberChanger.newInstance().getString(R.string.btn_accept));
                        contact.setChecked(false);
                        notifyItemChanged(position);
                    }
                }
            });
            viewHolder.tvDate.setText(contact.date);
            //viewHolder.typeImage.setBackgroundResource(contact.contactType.equals(SimUtil.CONTACT_TYPE_SIM) ? R.drawable.ic_sim : R.drawable.ic_phone);
        } else {
            viewHolder.itemView.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.filter == null ? 0 : this.filter.size();
    }

    public List<Contact> getContactList() {
        return this.filter;
    }

    public List<Contact> getOriginContactList() {
        return this.values;
    }

    public void filter(List<Contact> list) {
        this.filter.clear();
        this.filter.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.filter.clear();
        notifyDataSetChanged();
    }
}
