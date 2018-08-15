
package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.saini on 3/11/2016.
 */
public class MerchantListAdapterModifiedImage extends ArrayAdapter<MerchantModal> implements Filterable {
    Activity mcontext;
    private LayoutInflater inflater;
    public List<MerchantModal> originalData_merchantModals;
    public List<MerchantModal> filterData_merchantModals;
    private ItemFilter mfilter = new ItemFilter();


    boolean isCheckboxNeeded = true;

    int btnId;
    boolean alternate = false;
    int oddId = -1;
    int evenId = -1;


    public MerchantListAdapterModifiedImage(Activity mcontext, List<MerchantModal> merchantModals) {
        super(mcontext, R.layout.subiterm_merchant_list, merchantModals);
        this.mcontext = mcontext;
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext);
    }

    public MerchantListAdapterModifiedImage(Activity mcontext, ArrayList<MerchantModal> merchantModals, boolean isCheckboxNeeded) {
        this(mcontext, merchantModals, isCheckboxNeeded, -1);
        this.mcontext = mcontext;
    }

    public MerchantListAdapterModifiedImage(Activity mcontext, ArrayList<MerchantModal> merchantModals, boolean isCheckboxNeeded, int btnId) {
        super(mcontext, R.layout.subiterm_merchant_list, merchantModals);
        this.mcontext = mcontext;
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext);
        this.isCheckboxNeeded = isCheckboxNeeded;
        this.btnId = btnId;
    }

    public MerchantListAdapterModifiedImage(Context mcontext, ArrayList<MerchantModal> merchantModals, boolean isCheckboxNeeded, boolean alternate, int oddId, int evenId) {
        super(mcontext, R.layout.subiterm_merchant_list_image, merchantModals);
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext);
        this.isCheckboxNeeded = isCheckboxNeeded;
        this.btnId = btnId;
    }



    @Override
    public int getCount() {
        return filterData_merchantModals.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (filterData_merchantModals != null) {
            final int index = position;
            final MerchantModal merchantModal = filterData_merchantModals.get(position);
            TextView txtview;
            RelativeLayout layout_myPoint = null;
            ImageView imageviewMyPoint = null;
            ImageView iv_star;
            Switch swtch;
            if (view == null) {
                view = inflater.inflate(R.layout.subitem_charity_merchant_list_image, parent, false);
                txtview = (TextView) view.findViewById(R.id.txtMerchant);
                swtch = (Switch) view.findViewById(R.id.chbMerchant);
                imageviewMyPoint = (ImageView) view.findViewById(R.id.imageviewMyPoint);
                layout_myPoint = (RelativeLayout) view.findViewById(R.id.layout_myPoint);
                iv_star = (ImageView) view.findViewById(R.id.iv_star);
                view.setTag(new ViewHolder(txtview, swtch, layout_myPoint, imageviewMyPoint, iv_star));

                swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Switch swtch = (Switch) buttonView;
                        MerchantModal modal = (MerchantModal) swtch.getTag();
                        if (isChecked) {
                            filterData_merchantModals.get(index).setChecked(true);
                        } else {
                            filterData_merchantModals.get(index).setChecked(false);
                        }
                    }

                });
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                txtview = holder.getUsername();
                swtch = holder.getSwtch();
                imageviewMyPoint = holder.getImageviewMyPoint();
                layout_myPoint = holder.getLayout_myPoint();
                iv_star = holder.getIv_star();
            }

            txtview.setText(merchantModal.getBussinessName());
            if (merchantModal.getIsRead().booleanValue()) {
                iv_star.setVisibility(View.VISIBLE);
            } else {
                iv_star.setVisibility(View.GONE);
            }
            if (filterData_merchantModals.get(index).getProfileImage() != null && !filterData_merchantModals.get(index).getProfileImage().equals("")) {
                Picasso.with(mcontext).load(filterData_merchantModals.get(index).getProfileImage())
                        .resize(CommonUtility.dpToPx(mcontext, 60), CommonUtility.dpToPx(mcontext, 60)).
                        placeholder(R.drawable.icon_placeholder).into(imageviewMyPoint);
            } else {
                Picasso.with(mcontext).load(filterData_merchantModals.get(index).getProfileImage())
                        .resize(CommonUtility.dpToPx(mcontext, 60), CommonUtility.dpToPx(mcontext, 60)).
                        placeholder(R.drawable.icon_placeholder).into(imageviewMyPoint);
            }
            if (!isCheckboxNeeded) {
                swtch.setVisibility(View.GONE);
            } else {
                swtch.setTag(merchantModal);
                swtch.setChecked(merchantModal.isChecked());
            }

            if (alternate) {

                if (position % 2 == 0)//even
                {

                }

            }

            //  layout_myPoint.setBackgroundResource(btnId);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("CharSequence: ",""+constraint.toString().toLowerCase());
            System.out.println("Search Pattern :: " + constraint.toString());
            List<MerchantModal> temp = originalData_merchantModals;
            List<MerchantModal> filterdata = new ArrayList<MerchantModal>();
            FilterResults results = new FilterResults();
            System.out.println("Total User " + temp.size() + "");
            for (int cnt = 0; cnt < temp.size(); cnt++) {
                if (temp.get(cnt).getBussinessName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterdata.add(temp.get(cnt));
                }
            }
            results.values = filterdata;
            System.out.println("filtered Elt :: " + filterdata.size() + "");
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterData_merchantModals = (ArrayList<MerchantModal>) results.values;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        TextView username;
        Switch swtch;
        ImageView imageviewMyPoint;
        RelativeLayout layout_myPoint;
        ImageView iv_star;

        public ImageView getIv_star() {
            return iv_star;
        }

        public void setIv_star(ImageView iv_star) {
            this.iv_star = iv_star;
        }

        public ViewHolder() {
        }

        public ImageView getImageviewMyPoint() {
            return imageviewMyPoint;
        }

        public void setImageviewMyPoint(ImageView imageviewMyPoint) {
            this.imageviewMyPoint = imageviewMyPoint;
        }

        public void setLayout_myPoint(RelativeLayout layout_myPoint) {
            this.layout_myPoint = layout_myPoint;
        }

        public ViewHolder(TextView username, Switch swtch, RelativeLayout layout_myPoint, ImageView imageviewMyPoint, ImageView iv_star) {
            this.iv_star = iv_star;
            this.username = username;
            this.swtch = swtch;
            this.layout_myPoint = layout_myPoint;
            this.imageviewMyPoint = imageviewMyPoint;
        }

        public RelativeLayout getLayout_myPoint() {
            return layout_myPoint;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public Switch getSwtch() {
            return swtch;
        }

        public void setSwtch(Switch swtch) {
            this.swtch = swtch;
        }

    }

}
