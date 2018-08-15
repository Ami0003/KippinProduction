package com.kippin.connectedbankacc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.connectedbankacc.ConnectedBankAccount;
import com.kippin.kippin.R;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.kippin.webclient.model.ModalMobileDropdownListing;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class BankAdapter extends BaseAdapter {

    private ArrayList<ModalMobileDropdownListing> entries = null;
    private Context context = null;
    private LayoutInflater layoutInflater;


    public BankAdapter(Context context  ) {

        this.context = context;
        entries = Utility.banks;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;

        if(position == entries.size()-1){
            return  new View(context) ;
        }
        else
        if(true) {
            convertView = layoutInflater.inflate(R.layout.include_item_bank, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tvBankName = (TextView)convertView.findViewById(R.id.tvBankName);

            convertView.setTag(viewHolder);

        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if(position == 0 && ((ConnectedBankAccount)context).isItemSelected){
////            convertView.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
//            return  new View(context);
//        }else
        {
            viewHolder.tvBankName.setText(entries.get(position).getBankName());
        }

        return convertView;


    }


    class ViewHolder{
        TextView tvBankName;
    }

}
