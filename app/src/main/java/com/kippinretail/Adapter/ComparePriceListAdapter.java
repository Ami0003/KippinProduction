package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.ComparePriceList.PriceDetail;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by sandeep.saini on 3/15/2016.
 */
public class ComparePriceListAdapter extends BaseAdapter {
    private Context mcontext;
    private LayoutInflater inflater;
    private List<PriceDetail> priceDetails;

    public ComparePriceListAdapter(Context mcontext, List<PriceDetail> priceDetails)
    {
        this.mcontext = mcontext;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.priceDetails = priceDetails;
    }

    @Override
    public int getCount()
    {
        return priceDetails.size();

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
    public View getView(int position, View view, ViewGroup parent)
    {
        ViewHolder holder = null;
        if(view == null)
        {
            view = inflater.inflate(R.layout.subitem_compare_price_list,parent,false);
            holder = new ViewHolder();
            holder.txtItemName = (TextView)view.findViewById(R.id.txtItemName);
            holder.txtMerchantName = (TextView)view.findViewById(R.id.txtMerchantName);
            holder.txtPrice = (TextView)view.findViewById(R.id.txtPrice);
            holder.txtsize = (TextView)view.findViewById(R.id.txtsize);
            holder.txtLocation=(TextView)view.findViewById(R.id.txtLocation);
            holder.txtCity=(TextView)view.findViewById(R.id.txtCity);
            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }
        if(priceDetails.get(position)!=null && priceDetails.get(position).getItemName()!=null)
        {
            holder.txtItemName.setText(priceDetails.get(position).getItemName());
        }
        if(priceDetails.get(position)!=null && priceDetails.get(position).getPrice()!=null)
        {
            double price = Double.parseDouble(priceDetails.get(position).getPrice());
            int _price = (int)price;
            holder.txtPrice.setText("$"+String.valueOf(_price));
        }
        if(priceDetails.get(position)!=null && priceDetails.get(position).getBusinessName()!=null)
        {
            holder.txtMerchantName.setText( priceDetails.get(position).getBusinessName());
        }
        if(priceDetails.get(position)!=null && priceDetails.get(position).getLocation()!=null)
        {
            holder.txtLocation.setText(""+priceDetails.get(position).getLocation());
        }
        if(priceDetails.get(position)!=null && priceDetails.get(position).getCity()!=null)
        {
            holder.txtCity.setText(""+priceDetails.get(position).getCity());
        }
            holder.txtsize.setText( priceDetails.get(position).getQuantity());

        return view;
    }

    private class ViewHolder
    {
        TextView txtItemName,txtMerchantName,txtPrice,txtsize,txtLocation,txtCity;
    }
}
