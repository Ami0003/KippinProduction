package com.kippin.bankstatement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class StatusAdapter extends BaseAdapter {

    private String[] entries = null;
    private Context context = null;
    private LayoutInflater layoutInflater;


    public StatusAdapter(Context context, String[] entries) {

        this.context = context;
        this.entries = entries;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return entries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View view;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;


        if(convertView==null) {
            view = layoutInflater.inflate(R.layout.include_item_bank, parent, false);

            convertView =view;

            viewHolder = new ViewHolder();

            viewHolder.tvBankName = (TextView)convertView.findViewById(R.id.tvBankName);

            convertView.setTag(viewHolder);

        }else{
            view =convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBankName.setText(entries[position]);

        return convertView;


    }


    class ViewHolder{
        TextView tvBankName;
    }

}
