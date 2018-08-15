package com.kippinretail.Adapter;//package com.kippinretail.Adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.ToggleButton;
//
//import com.kippinretail.ApplicationuUlity.CommonUtility;
//import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftAnalytics;
//import com.kippinretail.Modal.MerchantList.Merchant;
//import com.kippinretail.Modal.MerchantModal;
//import com.kippinretail.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by sandeep.singh on 7/21/2016.
// */
//public class EnableDisableMerchantListAdapter extends BaseAdapter implements Filterable {
//    List<Merchant> listData;
//    Activity mContext ;
//    LayoutInflater inflater;
//    int hei,wid;
//    String currentUserId;
//    private Filter mfilter = new ItemFilter();
//
//    public EnableDisableMerchantListAdapter(List<Merchant> listData , String currneUserId,Activity activity){
//        this.listData = listData;
//        this.mContext = activity;
//        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        wid = CommonUtility.dpToPx(mContext, 250);
//        hei = CommonUtility.dpToPx(mContext,100);
//    }
//    @Override
//    public int getCount() {
//        if(listData!=null){
//            return listData.size();
//        }
//        else{
//            return  0;
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        ViewHolder holder=null;
//        if(listData !=null) {
//            final int index = position;
//            final Merchant merchant = listData.get(position);
//            if (view == null) {
//                holder = new ViewHolder();
//                view = inflater.inflate(R.layout.subiterm_merchant_list, parent, false);
//                holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
//                holder.swtch = (ToggleButton) view.findViewById(R.id.chbMerchant);
//                holder.layout_myPoint = (RelativeLayout) view.findViewById(R.id.layout_myPoint);
//                view.setTag(holder);
//            }
//            else {
//                holder = (ViewHolder) view.getTag();
//            }
//            holder.txtMerchant.setText(merchant.getBussinessName());
//            holder.swtch.setChecked(merchant.isSubscribedMerchant());
//            holder.swtch.setTag(new Integer(position));
//            holder.swtch.setId(position);
//            holder.swtch.setOnClickListener(onClick);
//        }
//        if(!isCheckboxNeeded){
//            holder.swtch.setVisibility(View.GONE);
//        }
//
//        return view;
//    }
//    @Override
//    public Filter getFilter() {
//        return mfilter;
//    }
//
//    private class ViewHolder {
//        TextView txtMerchant;
//        ToggleButton swtch;
//        RelativeLayout layout_myPoint;
//    }
//
//    class ItemFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<MerchantModal> temp = new ArrayList<MerchantModal>();
//            FilterResults results = new FilterResults();
//            for(int cnt = 0;cnt<temp.size() ; cnt++)
//            {
//                if(temp.get(cnt).getBussinessName().contains(constraint.toString())){
//                    temp.add(temp.get(cnt));
//                }
//            }
//            results.values = temp;
//            results.count = temp.size();
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filterData_merchants = (ArrayList <MerchantModal>)results.values;
//            notifyDataSetChanged();
//        }
//    }
//}
