package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ServerResponseForUserAnalytics;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by Amit on 7/20/2016.
 */
public class Analytics_UserPoint extends BaseAdapter {
    List<ServerResponseForUserAnalytics> listData;
    Activity mContext ;
    LayoutInflater inflater;

    public Analytics_UserPoint(List<ServerResponseForUserAnalytics> listData , Activity activity){
        this.listData = listData;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(listData!=null){
            return listData.size();
        }
        else{
            return  0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.subitem_point_analytics,parent,false);
            holder.txt1 =(TextView) convertView.findViewById(R.id.txt_col1);
            holder.txt2 =(TextView) convertView.findViewById(R.id.txt_col2);
            holder.txt3 =(TextView) convertView.findViewById(R.id.txt_col3);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.txt1.setText("");
            holder.txt2.setText("");
            holder.txt3.setText("");
        }
        ServerResponseForUserAnalytics response = listData.get(position);
        String point=response.getPointUsed();
        String point_allocate=response.getPointAllocated();
        int val1 = Integer.parseInt(point_allocate);
        int val2 = Integer.parseInt(point);
        String finalResult=String.valueOf(val1 - val2);
        holder.txt1.setText(response.getLoyalityCardName());
        holder.txt2.setText(finalResult);
        holder.txt3.setText(response.getPointUsed());

        return convertView;
    }
    class ViewHolder{
        TextView txt1,txt2,txt3;
    }
}
