package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ServerResponseForLOcationAnalysis;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
public class Analytics_LocationAdapter extends BaseAdapter {
    List<ServerResponseForLOcationAnalysis> listData;
    Activity mContext ;
    LayoutInflater inflater;
    public Analytics_LocationAdapter(List<ServerResponseForLOcationAnalysis> listData , Activity activity){
        this.listData = listData;
        this.mContext = activity;
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
            holder.txt3.setVisibility(View.GONE);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.txt1.setText("");
            holder.txt2.setText("");
            holder.txt3.setVisibility(View.GONE);
        }
        ServerResponseForLOcationAnalysis response = listData.get(position);
        holder.txt1.setText(response.getCountry());
        holder.txt2.setText(response.getAmount());


        return convertView;
    }
    class ViewHolder{
        TextView txt1,txt2,txt3;
    }
}
