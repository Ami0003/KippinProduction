package com.kippinretail.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterDonatePoints extends BaseAdapter  implements Filterable {

    private LayoutInflater inflater = null;
    public List<MerchantModal> filterData_voucherModals;
    public List<MerchantModal> originalData_voucherModals;
    private Context mcontext;
    private int width,height;
    OnSelectionChanged onSelectionChanged;
    boolean  isMultichoice  = true;
    CheckBox old_cb= null;
    int old_position = -1;
    boolean old_check = false;
    private ListFilter mfilter = new ListFilter();

    public AdapterDonatePoints(Context mcontext, List<MerchantModal> voucherModals)
    {
        this.mcontext = mcontext;
        this.originalData_voucherModals =  this.filterData_voucherModals = voucherModals;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =(mcontext.getResources().getDisplayMetrics().widthPixels)/2;
        height = dpToPx(100);
    }

    public AdapterDonatePoints(Context mcontext, List<MerchantModal> voucherModals, boolean isMultiChoice ,OnSelectionChanged onSelectionChanged) {
        this.mcontext = mcontext;
        this.originalData_voucherModals =  this.filterData_voucherModals = voucherModals;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width =(mcontext.getResources().getDisplayMetrics().widthPixels)/2;
        height = dpToPx(100);
        this.isMultichoice = isMultiChoice;
        this.onSelectionChanged = onSelectionChanged;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mcontext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    @Override
    public int getCount()
    {
        return filterData_voucherModals.size();
    }

    @Override
    public Object getItem(int position) {
        return filterData_voucherModals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder = null;

        if(view == null)
        {
            view = inflater.inflate(R.layout.adapter_donate_points , parent , false);
            holder = new ViewHolder();
            holder.tvPoints =  (TextView)view.findViewById(R.id.tvMerchantPoints);
            holder.cbSelection = (CheckBox)view.findViewById(R.id.cbSelection);
            holder.tvMerchantName = (TextView)view.findViewById(R.id.tvMerchantName);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }


        if(filterData_voucherModals.get(position).getPoints()!=null )
        {
            String amount = filterData_voucherModals.get(position).getPoints();
            Double _amount = Double.parseDouble(amount);
            int __amount = (int)_amount.doubleValue();
            holder.tvPoints.setText(String.valueOf(__amount));
        }


        if(filterData_voucherModals.get(position).getBussinessName()!=null)
        {
            String merchant_name = filterData_voucherModals.get(position).getBussinessName();
            holder.tvMerchantName.setText(merchant_name);
        }


        boolean isChecked  = filterData_voucherModals.get(position).isChecked();

        holder.cbSelection.setChecked(isChecked);

        if(isChecked){
            old_cb = holder.cbSelection;
            old_position = position;
        }

        holder.cbSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_cb != null && position != old_position && !isMultichoice) {
                    old_cb.setChecked(false);
                }
                CheckBox cb = (CheckBox) v;
                boolean isChecked = cb.isChecked();
                filterData_voucherModals.get(position).setChecked(isChecked);
                if (!isMultichoice) {
                    if (position != old_position && old_cb != null)
                        filterData_voucherModals.get(old_position).setChecked(false);
                    AdapterDonatePoints.this.old_cb = cb;
                    AdapterDonatePoints.this.old_position = position;
                }

                if(onSelectionChanged!=null)
                onSelectionChanged.onSelectionChanged(position, filterData_voucherModals.get(position),isChecked);
            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    class ViewHolder
    {
        TextView tvPoints,tvMerchantName;
        CheckBox cbSelection;

    }

    class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("constraint", "constraint");
            List<MerchantModal> temp = new ArrayList<MerchantModal>();
            FilterResults result = new FilterResults();
            for(int i = 0 ; i < originalData_voucherModals.size() ; i++){
                if(originalData_voucherModals.get(i).getBussinessName().contains(constraint)){
                    temp.add(originalData_voucherModals.get(i));
                }
            }
            result.values = temp;
            result.count = temp.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterData_voucherModals = (List<MerchantModal>)results.values;
            notifyDataSetChanged();
        }
    }
}
