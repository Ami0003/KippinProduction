package com.kippinretail.KippinInvoice.Fragments_invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.config.Config;
import com.kippin.utils.Utility;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.KippinInvoice.CreateInvoiceCustomer;
import com.kippinretail.KippinInvoice.InnvoiceCustomerCreate;
import com.kippinretail.KippinInvoice.InvoiceCustomerList;
import com.kippinretail.KippinInvoice.ReceivedInvoicePerformaActivity;
import com.kippinretail.KippinInvoice.ReportSectionActivity;
import com.kippinretail.KippinInvoice.SentInvoicePerformaActivity;
import com.kippinretail.KippinInvoice.SupplierCustomerListActivity;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.interfaces.ButtonYesNoListner;

import java.util.ArrayList;

/**
 * Created by kamaljeet.singh on 11/9/2016.
 */

public class FragmentInvoiceDashBoard extends Fragment {
    ArrayList<ModalGridElement> modalGridElements;
    AdapterView.OnItemClickListener onItemClickListener;
    private GridView gridView1;
    String roleid;
    ImageView iv_logo;

    public static FragmentInvoiceDashBoard newInstance() {
        FragmentInvoiceDashBoard fragment = new FragmentInvoiceDashBoard();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View localView = inflater.inflate(R.layout.fragment_fragment_user_dash_board, container, false);
        gridView1 = (GridView) localView.findViewById(R.id.gridView);
        iv_logo=(ImageView)localView.findViewById(R.id.iv_logo);
        updateUI();
        addListener();
        return localView;
    }

    private void updateUI() {
        iv_logo.setBackgroundResource(R.drawable.kippin_invoicing);//ImageResource(R.drawable.kippin_invoicing);
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.create_invoice, "CREATE INVOICE"));
        modalGridElements.add(getElement(R.drawable.create_proforma, "CREATE QUOTE"));
        modalGridElements.add(getElement(R.drawable.received_invoice_proforma, "RECEIVED INVOICE/QUOTE"));
        modalGridElements.add(getElement(R.drawable.sent_invoice_proforma, "SENT INVOICE/QUOTE"));
        modalGridElements.add(getElement(R.drawable.invoice_report, "INVOICE REPORT"));
        modalGridElements.add(getElement(R.drawable.supplier_customer_list, "SUPPLIER/CUSTOMER LIST"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }




    protected void setData(ArrayList<ModalGridElement> modalGridElements, AdapterView.OnItemClickListener onItemClickListener) {
        this.modalGridElements = modalGridElements;
        this.onItemClickListener = onItemClickListener;
        loadGrid();

    }

    private void loadGrid() {
        MyAbstractGridAdapter1 adapter = new MyAbstractGridAdapter1(getActivity(), modalGridElements);
        gridView1.setAdapter(adapter);

    }

    private void resetClick() {
        CommonUtility.giftcardClicked = false;
        CommonUtility.layaltycardClicked = false;
        CommonUtility.aboutsUsClicked = false;
        CommonUtility.contactusClicked = false;
        CommonUtility.voucherclicked = false;

    }

    protected ModalGridElement getElement(int id, String title) {
        ModalGridElement modalGridElement = new ModalGridElement();
        modalGridElement.res = id;
        modalGridElement.string = title;
        return modalGridElement;
    }


    private void addListener() {
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        roleid="2";
                        Config.SCREEN_OPEN=3;
                        CommonUtility.InvoiceTitle = "1";
                        CommonUtility.INVOICE_TYPE = ((SuperActivity)getActivity()).Invoice;
                        callNextIntent(InvoiceCustomerList.class, CreateInvoiceCustomer.class);
                        break;
                    case 1:
                        roleid="1";
                        Config.SCREEN_OPEN=3;
                        CommonUtility.InvoiceTitle = "2";
                        CommonUtility.INVOICE_TYPE = ((SuperActivity)getActivity()).Performa;
                        callNextIntent(InvoiceCustomerList.class, CreateInvoiceCustomer.class);
                        break;
                    case 2:
                        CommonUtility.Sent_Receiver = "1";
                        callSupplierIntent(ReceivedInvoicePerformaActivity.class);
                        break;
                    case 3:
                        CommonUtility.Sent_Receiver = "0";
                        callSupplierIntent(SentInvoicePerformaActivity.class);
                        break;
                    case 4:
                        callSupplierIntent(ReportSectionActivity.class);

                        break;
                    case 5:
                        callSupplierIntent(SupplierCustomerListActivity.class);
                        break;
                }
            }


        });
    }

    private void callSupplierIntent(Class c) {
        Intent i = new Intent();
        i.setClass(getActivity(), c );
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void callNextIntent(final Class c , final Class targetClass) {

        CommonDialog.DialogToCreateuser(getActivity(), "Is this for a new Customer?", new ButtonYesNoListner() {
            @Override
            public void YesButton() {
                Yes_NoClickIntent(targetClass);
            }

            @Override
            public void NoButton() {
//                Yes_NoClickIntent(c);
                Config.SCREEN_OPEN=3;
                Intent i = new Intent();
                i.setClass(getActivity(), InnvoiceCustomerCreate.class);
                i.putExtra("roleId",roleid);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

            }
        });

    }

    public void Yes_NoClickIntent(final Class c){
        Config.SCREEN_OPEN=3;
        Intent i = new Intent();
        i.setClass(getActivity(), c );
      //  i.putExtra("roleId",((SuperActivity)getActivity()).Customer);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    public class MyAbstractGridAdapter1 extends BaseAdapter {

        private Activity mContext;
        private LayoutInflater mLayoutInflater;
        private int width, height;
        private ArrayList<ModalGridElement> refForImages = null;


        public MyAbstractGridAdapter1(Activity dashBoardMerchantActivity,ArrayList<ModalGridElement> refForImages) {
            mContext = dashBoardMerchantActivity;
            this.refForImages = refForImages;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 85);
                float x=  (myX/ 2f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 150);
                float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 40))  ;
                width = Math.round(x);
                height = Math.round(y);
                Log.e("if x axis width "+myX , "y axis height "+myHeightPX);
                Log.e("if width "+width , "height "+height);
            }else{

                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 40);
                float x=  (myX/ 4f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
                float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
                width = Math.round(x);
                height = Math.round(y);
                Log.e("else x axis width "+myX , "y axis height "+myHeightPX);
                Log.e("else width "+width , "height "+height);
            }

        }
        public MyAbstractGridAdapter1(Activity dashBoardMerchantActivity, ArrayList<ModalGridElement> refForImages, GridView grid) {
            mContext = dashBoardMerchantActivity;
            this.refForImages = refForImages;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 75);
                float x=  (myX/ 2f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 90);
                float y = (myHeightPX/ 3f - Utility.dpToPx(mContext , 5))  ;
                width = Math.round(x);
                height = Math.round(y);
            }else{

                float myX = mContext.getResources().getDisplayMetrics().widthPixels - Utility.dpToPx(mContext, 35);
                float x=  (myX/ 4f);
                float myHeightPX = mContext.getResources().getDisplayMetrics().heightPixels - Utility.dpToPx(mContext , 40);
                float y = (myHeightPX/ 2f - Utility.dpToPx(mContext , 5))  ;
                width = Math.round(x);
                height = Math.round(y);
            }

        }

        @Override
        public int getCount() {
            return refForImages.size();
        }

        @Override
        public Object getItem(int position) {
            return refForImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            ModalGridElement modalGridElement = refForImages.get(position);

            if (convertView == null) {
                holder = new Holder();
                convertView = mLayoutInflater.inflate(R.layout.item_abstract_grid, null);
                holder = new Holder();
                holder.ivGridBg = (ImageView) convertView.findViewById(R.id.grid_bg);

                holder.ivGridIcon = (ImageView) convertView.findViewById(R.id.grid_icon);

                holder.tvName = (TextView) convertView.findViewById(R.id.grid_text);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            int res = getBackgroundDrawableUsingPosition(position);
            //holder.ivGridBg.setImageResource(res);
            holder.ivGridBg.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.ivGridIcon.setVisibility(View.GONE);
            if(!modalGridElement.string.equals((""))){
                holder.ivGridBg.setImageResource(modalGridElement.res);
            }else{
                holder.ivGridBg.setVisibility(View.INVISIBLE);
            }
            //holder.tvName.setText(modalGridElement.string);
            holder.tvName.setVisibility(View.GONE);
            convertView.setLayoutParams(new AbsListView.LayoutParams(width , height));
            return convertView;
        }

        private int getBackgroundDrawableUsingPosition(int position) {

            switch (position) {
                case 0:
                    return R.drawable.blue;

                case 1:
                    return R.drawable.points_bg;

                case 2:
                    return R.drawable.gift_bg;

                case 3:
                    return R.drawable.voucher_bg;

                case 4:
                    if(CommonData.getUserData(mContext)!=null && CommonUtility.UserType.equals("1")) {
                        if (CommonData.getUserData(mContext).isEmployee()) {
                            return R.drawable.analysis_bg;
                        } else {
                            return R.drawable.icon_kippin_user;
                        }
                    }else{
                        return R.drawable.analysis_bg;
                    }

                case 5:
                    return R.drawable.compare_bg;
            }

            return R.drawable.analysis_bg;
        }

        class Holder {

            ImageView ivGridBg, ivGridIcon;
            TextView tvName;
        }

    }
}
