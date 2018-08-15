
package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ActivityEmployeeAuthentication;
import com.kippinretail.ActivityPunchCards_Scan;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Employee_ScanItem;
import com.kippinretail.GetNewPunchCard;
import com.kippinretail.GiftCardsActivity;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.MyPoint_FolderListActivity;
import com.kippinretail.MyPoint_UserLoyalityCardActivity;
import com.kippinretail.PurchasedGiftcardsActivity;
import com.kippinretail.R;
import com.kippinretail.VoucherListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MerchantVoucherListAdapterModifyImage extends BaseAdapter implements Filterable {
    Activity mcontext;
    LayoutInflater inflate;
    List<MerchantModal> originalData_merchant;
    List<MerchantModal> filteredData_merchant;
    int width, height;
    private ItemFilter mfilter = new ItemFilter();

    String parent;


    public MerchantVoucherListAdapterModifyImage(Activity mcontext, List<MerchantModal> merchant, String parent) {
        this.parent = parent;
        this.originalData_merchant = merchant;
        this.mcontext = mcontext;
        filteredData_merchant = originalData_merchant;
        inflate = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width = height = CommonUtility.dpToPx(mcontext, 60);
    }

    @Override
    public int getCount() {
        return filteredData_merchant.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        int index = position;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflate.inflate(R.layout.subitem_merchant_voucher_list_image, parent, false);
            holder.imageviewMyPoint = (ImageView) view.findViewById(R.id.imageviewMyPoint);
            holder.layoutOfSubItem = (RelativeLayout) view.findViewById(R.id.layoutOfSubItem);
            holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
            holder.iv_star = (ImageView) view.findViewById(R.id.iv_star);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final MerchantModal merchantModal = filteredData_merchant.get(position);
        if (merchantModal.getProfileImage() != null && !merchantModal.getProfileImage().equals("")) {
            Picasso.with(mcontext).load(merchantModal.getProfileImage()).resize(width, CommonUtility.dpToPx(mcontext, height)).placeholder(R.drawable.icon_placeholder).into(holder.imageviewMyPoint);
        } else {
            Picasso.with(mcontext).load(R.drawable.addtocart).resize(width, CommonUtility.dpToPx(mcontext, height)).placeholder(R.drawable.icon_placeholder).into(holder.imageviewMyPoint);
        }
        final String text = (merchantModal.getName() == null && merchantModal.getBussinessName() == null) ? "" :
                (merchantModal.getName().length() == 0 ? merchantModal.getBussinessName() : merchantModal.getName());


//        if(filteredData_merchant.get(position).getName()!=null){
        holder.txtMerchant.setText(text);
//        }
        if (merchantModal.isRead() == true) {
            holder.iv_star.setVisibility(View.VISIBLE);
        } else {
            holder.iv_star.setVisibility(View.INVISIBLE);
        }

        if (filteredData_merchant.get(position).getMerchantid() != null) {
            holder.layoutOfSubItem.setTag(filteredData_merchant.get(position));
        }
        holder.layoutOfSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantModal modal = (MerchantModal) v.getTag();
                Intent i = new Intent();
                // Listener is also in class
                if (modal.getParent().equals("ViewMerchantVoucherActivity")) {
                    i.setClass(mcontext, VoucherListActivity.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    mcontext.startActivity(i);
                }

                if (modal.getParent().equals("SelectMerchantActivity")) {
                    i.setClass(mcontext, GiftCardsActivity.class);
                    i.putExtra("parent", "SelectMerchantActivity");
                    i.putExtra("merchantId", modal.getMerchantid());
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("MerchantsOfPurchasedGiftCardsActivity")) {
                    if (modal.getParentButton().equals("giftCardFromThirdParty")) {
                        i.setClass(mcontext, PurchasedGiftcardsActivity.class);
                        i.putExtra("merchantId", modal.getMerchantid());
                        i.putExtra("merchantName", modal.getBussinessName());
                        i.putExtra("parentButton", "giftCardFromThirdParty");
                    } else if (modal.getParentButton().equals("myGiftCard") && modal.getChildActivity().equals("PurchasedGiftcardsActivity")) {
                        i.setClass(mcontext, PurchasedGiftcardsActivity.class);
                        i.putExtra("merchantId", modal.getMerchantid());
                        i.putExtra("merchantName", modal.getBussinessName());

                        i.putExtra("parentButton", MerchantVoucherListAdapterModifyImage.this.parent);
                    } else if (modal.getParentButton().equals("myGiftCard") && modal.getChildActivity().equals("MyPoint_FolderListActivity")) {
                        i.setClass(mcontext, position > 0 ? PurchasedGiftcardsActivity.class : MyPoint_FolderListActivity.class);
                        i.putExtra("merchantId", modal.getMerchantid());
                        i.putExtra("merchantName", modal.getBussinessName());
                        i.putExtra("parentButton", MerchantVoucherListAdapterModifyImage.this.parent);
                        i.putExtra("nonKippin", position == 0 ? true : false);
                        i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.GIFTCARD);
                    }


                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("TransferGiftCard_MerchantListActivity")) {
                    i.setClass(mcontext, GiftCardsActivity.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("merchantName", modal.getBussinessName());
                    i.putExtra("parent", "TransferGiftCard_MerchantListActivity");
                    i.putExtra("friendId", modal.getFriendId());
                    i.putExtra("merchantId", modal.getMerchantid());
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("MyPoint_MerchantListActivity") && position == 0) {
                    i.setClass(mcontext, MyPoint_FolderListActivity.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.LOYALITY_CARD);
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("MyPoint_MerchantListActivity")) {
                    i.setClass(mcontext, MyPoint_UserLoyalityCardActivity.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("barcode", modal.getBarcode());
                    i.putExtra("name", modal.getLoyalityCardName());
                    i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.LOYALITY_CARD);
                    mcontext.startActivity(i);

                } else if (modal.getParent().equals("MerchantAsEmployee_GiftCard")) {
                    i.setClass(mcontext, merchantModal.isAuthenticated() ? Employee_ScanItem.class : ActivityEmployeeAuthentication.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("merchantName", text);
                    i.putExtra("operation", "MerchantAsEmployee_GiftCard");
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("MerchantAsEmployee_PunchCard")) {
                    i.setClass(mcontext, merchantModal.isAuthenticated() ? ActivityPunchCards_Scan.class : ActivityEmployeeAuthentication.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("merchantName", text);
                    i.putExtra("operation", "MerchantAsEmployee_PunchCard");
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("MyPunchCards")) {
                    //     i.setClass(mcontext, ActivityMyPunchCards.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("merchantName", text);
                    mcontext.startActivity(i);
                } else if (modal.getParent().equals("GetNewPunchCard")) {
                    i.setClass(mcontext, GetNewPunchCard.class);
                    i.putExtra("merchantId", modal.getMerchantid());
                    i.putExtra("merchantName", text);
                    mcontext.startActivity(i);
                }

                Log.e("merchantId", modal.getMerchantid());

                mcontext.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MerchantModal> temp = originalData_merchant;

            List<MerchantModal> filterdata = new ArrayList<MerchantModal>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                Log.e("===" + temp.get(cnt).getName(), constraint.toString());
                if (temp.get(cnt).getName().contains(constraint.toString())) {
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
            filteredData_merchant = (ArrayList<MerchantModal>) results.values;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView imageviewMyPoint;
        RelativeLayout layoutOfSubItem;
        TextView txtMerchant;
        ImageView iv_star;

    }
}
