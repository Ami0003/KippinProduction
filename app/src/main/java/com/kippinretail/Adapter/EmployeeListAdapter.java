package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.R;

import java.util.ArrayList;

/**
 * Created by sandeep.singh on 7/15/2016.
 */
public class EmployeeListAdapter extends BaseAdapter {
    Context context;
    ArrayList<GiftCard> modalGiftCards;
    LayoutInflater layoutInflater ;
    @Override
    public int getCount() {
        return 0;
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
        if(convertView == null){
            convertView =layoutInflater.inflate(R.layout.item_friend_list,parent,false);
        }
        return null;
    }
}
