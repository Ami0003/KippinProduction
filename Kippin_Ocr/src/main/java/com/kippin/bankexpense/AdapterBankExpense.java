package com.kippin.bankexpense;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.webclient.model.ModelExpense;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/29/2016.
 */
public class AdapterBankExpense extends BaseAdapter{

    Activity activity;
    ArrayList<ModelExpense>  expenses;

    public AdapterBankExpense(Activity activity, ArrayList<ModelExpense> expenses) {
        this.activity=activity;
        this.expenses=expenses;
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class  ViewHolder{
        TextView textView;
    }

    ViewHolder holder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         if(convertView==null){
             convertView = ((LayoutInflater)(activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.include_item_bank,parent,false) ;
             holder = new ViewHolder();
             holder.textView = (TextView) convertView.findViewById(R.id.tvBankName);
             convertView.setTag(holder);
         }else{
             holder  = (ViewHolder) convertView.getTag();
         }

        holder.textView.setText(expenses.get(position).getExpenseName());
        return convertView;
    }
}
