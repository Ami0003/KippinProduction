package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Interface.OnDonateToCharity;
import com.kippinretail.Modal.donateToCharity.ModalDonateToCharity;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gaganpreet.singh on 3/31/2016.
 */

/**
 * Created by sandeep.saini on 3/15/2016.
 */
public class AdapterDonateToCharity extends BaseAdapter  {
    private Activity mcontext;
    private LayoutInflater inflater;

    private ItemFilter mfilter = new ItemFilter();
    private int hei,wei;
    private ArrayList<ModalDonateToCharity> donateToCharities;
    private OnDonateToCharity onDonateToCharity ;
    private List<ModalDonateToCharity> filtereddata;

    public AdapterDonateToCharity(Activity mcontext, List<ModalDonateToCharity> priceDetails,OnDonateToCharity onDonateToCharity) {
        this.mcontext = mcontext;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        donateToCharities=new ArrayList<>();
        donateToCharities.clear();
        this.donateToCharities.addAll(priceDetails);
        //= priceDetails;
        this.filtereddata = donateToCharities;
        this.onDonateToCharity = onDonateToCharity;
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext,60);
        Log.e("donateToCharities:",""+donateToCharities.size());
        Log.e("filtereddata:",""+filtereddata.size());
    }

    @Override
    public int getCount() {
        if(filtereddata!=null){
            return filtereddata.size();
        }
        else{
            return 0;
        }


    }

    @Override
    public Object getItem(int position) {
        return filtereddata.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View lastView;
    int lastPosition = -1;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_donate_to_charity, parent, false);
            holder = new ViewHolder();
            holder.iv_profileImage = (CircleImageView) view.findViewById(R.id.iv_profileImage);
            holder.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(filtereddata.get(position).getProfileImage()!=null && !filtereddata.get(position).getProfileImage().equals("")){
            Picasso.with(mcontext).load(filtereddata.get(position).getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.iv_profileImage);
        }else{
            Picasso.with(mcontext).load(filtereddata.get(position).getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.iv_profileImage);
        }
        holder.txtItemName.setText(filtereddata.get(position).getBusinessName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDonateToCharity.onDonate(filtereddata.get(position).getId());
            }
        });


        final View finalView = view;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lastView != null && lastPosition != position) {
                    lastView.setBackgroundColor(Color.WHITE);
                }

                if (lastPosition != position) {
                    lastPosition = position;
                    lastView = finalView;
                    finalView.setBackgroundColor(Color.LTGRAY);
                }
                return false;
            }
        }); if(lastPosition ==position){
            view.setBackgroundColor(Color.LTGRAY);
        }else if(lastPosition!=position && lastPosition>=0){
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }

   /* @Override
    public Filter getFilter() {
        return mfilter;
    }*/

    private class ViewHolder {
        CircleImageView iv_profileImage;
        TextView txtItemName;
    }


    class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("Inside performFiltering", "Inside performFiltering" + constraint);
            List<ModalDonateToCharity> temp = donateToCharities;
            Log.e("Size of temp   ", temp.size() + "");
            List<ModalDonateToCharity> filterdata = new ArrayList<ModalDonateToCharity>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                Log.e("BusinessName   ", temp.get(cnt).getBusinessName().toLowerCase(Locale.getDefault()) + "");
                Log.e("constraint   ", constraint.toString().toLowerCase(Locale.getDefault()) + "");
                if (temp.get(cnt).getBusinessName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                    filterdata.add(temp.get(cnt));
                }
            }
            Log.e("filterdata", filterdata.size() + "");
            results.values = filterdata;
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filtereddata = (ArrayList<ModalDonateToCharity>) results.values;
            notifyDataSetChanged();
        }
    }
  /*  public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("charText:",""+charText);
        filtereddata.clear();
        if (charText.length() == 0) {
            Log.e("SIIZZEE: ",""+donateToCharities.size());
            filtereddata.addAll(donateToCharities);
        }
        else
        {
            for (ModalDonateToCharity wp : donateToCharities)
            {
                Log.e("WP:::",""+wp.getBusinessName());
                if (wp.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    filtereddata.add(wp);
                }
            }
        }
        Log.e("filtereddata:",""+filtereddata.size());
      //  adapterCloudImages = new AdapterCloudGalleryImages(KippinCloudGalleryActivity.this, kippinCloudgalleries);
      //  gridView.setAdapter(adapterCloudImages);
       // gridView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();

    }*/

}
