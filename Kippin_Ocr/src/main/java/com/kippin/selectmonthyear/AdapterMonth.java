package com.kippin.selectmonthyear;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.performbook.adapter.CurrencyAdapter;
import com.kippin.utils.Utility;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.kippin.kippin.R.layout.include_select_month_year;

/**
 * Created by gaganpreet.singh on 2/16/2016.
 */
public class AdapterMonth extends BaseAdapter {

    ActivitySelectMonthYear activitySelectMonthYear;
    ArrayList<TemplateMonth> datas;
    LayoutInflater layoutInflater;

    public AdapterMonth(ActivitySelectMonthYear activitySelectMonthYear, ArrayList<TemplateMonth> datas ){
        this.activitySelectMonthYear = activitySelectMonthYear;
        this.datas = datas;
        layoutInflater = activitySelectMonthYear.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


//    class ViewHolder {
//        TextView textView;
//    }


    TextView viewHolder;

    String concater = "<font color=#cc0029>*</font>";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = ((View)layoutInflater.inflate(R.layout.include_select_month_year,parent,false));

            viewHolder = (TextView) convertView;

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (TextView) convertView.getTag ();
        }


        String data = datas.get(position).getFolderName();

        if(Integer.parseInt(data)<=12){
            data = Utility.getMonth(data);
        }

        if(!datas.get(position).isAssociated())
        {

            data = concater+data;
        }


        viewHolder.setText(Html.fromHtml(data));



        return convertView;
    }
}
