package com.kippin.performbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.webclient.model.ModelOwnership;
import com.kippin.webclient.model.ProvinceModel;

import java.util.ArrayList;

/**
 * Created by dilip.singh on 2/4/2016.
 */
public class OwnerShipAdapter extends ArrayAdapter<ModelOwnership> {

    private LayoutInflater mInflater;
    Context mContext;
    ArrayList<ModelOwnership> arrayList;
    public OwnerShipAdapter(Context context, int resource, ArrayList<ModelOwnership> objects) {
        super(context, resource, objects);
        mContext = context;
        arrayList = objects;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView( position, convertView, parent);
    }

    @Override
    public int getCount() {
        return arrayList.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_spinner_textview, null);
            holder = new ViewHolder();
            holder.textViewName = (TextView)convertView.findViewById(R.id.spinner_text);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
            convertView.setTag(holder);
        }

        holder.textViewName.setText(arrayList.get(position).getOwnershipType());

        return convertView;
    }

    /**
     * View holder class
     */
    public static class ViewHolder{
        TextView textViewName;

    }
}

