package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.CharityData;
import com.kippinretail.MoneyDonatedActivity;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by kamaljeet.singh on 10/10/2016.
 */

public class CharityDataAdapter extends BaseAdapter {


    private final List<CharityData> charityData;
    private final MoneyDonatedActivity context;
    private final LayoutInflater inflater;
    ViewHolder holder;

    public CharityDataAdapter(MoneyDonatedActivity moneyDonatedActivity, List<CharityData> charityData) {

        this.context = moneyDonatedActivity;
        this.charityData = charityData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("SIZE:",""+charityData.size());
    }

    @Override
    public int getCount() {
        return charityData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
Log.e("getUserName: ",""+charityData.get(position).getUserName());

        if (view == null) {
            view = inflater.inflate(R.layout.row_donated_item, parent, false);
            holder = new ViewHolder();
            holder.tvForKippinUsers = (TextView) view.findViewById(R.id.tvForKippinUsers);
            holder.tvForDate = (TextView) view.findViewById(R.id.tvForDate);
            holder.tvForAmount = (TextView) view.findViewById(R.id.tvForAmount);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvForKippinUsers.setText(charityData.get(position).getUserName());
        holder.tvForDate.setText(charityData.get(position).getDateCreated());
        holder.tvForAmount.setText("" + charityData.get(position).getAmount());

        return view;
    }

    class ViewHolder {
        TextView tvForKippinUsers, tvForDate, tvForAmount;
    }
}
