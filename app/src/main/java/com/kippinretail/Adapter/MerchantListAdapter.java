package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantModal;
import com.kippinretail.Modal.Merchant_;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.R;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.saini on 3/11/2016.
 */
public class MerchantListAdapter extends ArrayAdapter<MerchantModal> implements Filterable
{
    Activity mcontext;
    private LayoutInflater inflater;
    public List<MerchantModal> originalData_merchantModals;
    public List<MerchantModal> filterData_merchantModals;
    private ItemFilter mfilter = new ItemFilter();
    private String currentUserId;

    boolean  isCheckboxNeeded=true;
    int hei,wei;
    int btnId;
    boolean alternate = false;
    int oddId =-1;
    int evenId=-1;
    View terms;



    public MerchantListAdapter(Activity mcontext,List<MerchantModal> merchantModals,String currentUserId,View terms)
    {
        super( mcontext, R.layout.subiterm_merchant_list, merchantModals );
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext) ;
        this.currentUserId = currentUserId;
        this.mcontext = mcontext;
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext,60);
        this.terms= terms;
    }
    public MerchantListAdapter(Activity mcontext,List<MerchantModal> merchantModals,String currentUserId)
    {
        super( mcontext, R.layout.subiterm_merchant_list, merchantModals );
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext) ;
        this.currentUserId = currentUserId;
        this.mcontext = mcontext;
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext,60);
    }
    public MerchantListAdapter(Activity mcontext,ArrayList<MerchantModal> merchantModals)
    {
        super( mcontext, R.layout.subiterm_merchant_list, merchantModals );
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext) ;
        this.mcontext = mcontext;
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext,60);
    }

    public MerchantListAdapter(Activity mcontext, ArrayList<MerchantModal> merchantModals, boolean isCheckboxNeeded, int btnId) {
        super( mcontext, R.layout.subiterm_merchant_list, merchantModals );
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext) ;
        this.mcontext = mcontext;
        this.isCheckboxNeeded = isCheckboxNeeded;
        this.btnId = btnId;
        hei = CommonUtility.dpToPx(mcontext, 60.00);
        wei = CommonUtility.dpToPx(mcontext,60.00);
    }

    public MerchantListAdapter(Context mcontext, ArrayList<MerchantModal> merchantModals, boolean isCheckboxNeeded, boolean alternate, int oddId , int evenId) {
        super( mcontext, R.layout.subiterm_merchant_list, merchantModals );
        this.originalData_merchantModals = merchantModals;
        this.filterData_merchantModals = merchantModals;
        inflater = LayoutInflater.from(mcontext) ;
        this.isCheckboxNeeded = isCheckboxNeeded;
        this.btnId = btnId;
    }

    @Override
    public int getCount() {
        if(filterData_merchantModals!=null){
            return filterData_merchantModals.size();
        }else{
            return  0;
        }

    }


    ViewHolder holder=null;


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(filterData_merchantModals !=null) {
            final int index = position;
            final MerchantModal merchantModal = filterData_merchantModals.get(position);
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.subiterm_merchant_list, parent, false);
                holder.iv_profileImage = (ImageView) view.findViewById(R.id.iv_profileImage);
                holder.txtMerchant = (TextView) view.findViewById(R.id.txtMerchant);
                holder.swtch = (ToggleButton) view.findViewById(R.id.chbMerchant);
                holder.layout_myPoint = (RelativeLayout) view.findViewById(R.id.layout_myPoint);
                view.setTag(holder);
            }
            else {
                 holder = (ViewHolder) view.getTag();
            }
            if(merchantModal.getProfileImage()!=null && !merchantModal.getProfileImage().equals("")){
                Picasso.with(mcontext).load(merchantModal.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.iv_profileImage);
            }
            else{
                Picasso.with(mcontext).load(merchantModal.getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei, hei).into(holder.iv_profileImage);
            }
            holder.txtMerchant.setText(merchantModal.getBussinessName());
            holder.txtMerchant.setTypeface(null,Typeface.NORMAL);
            holder.swtch.setChecked(merchantModal.isSubscribedMerchant());
            holder.swtch.setTag(new Integer(position));
            holder.swtch.setId(position);
            holder.swtch.setOnClickListener(onClick);
        }
        if(!isCheckboxNeeded){
            holder.swtch.setVisibility(View.GONE);
        }

        return view;
    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View buttonView) {


            ToggleButton swtch = (ToggleButton) buttonView;
            boolean isChecked = swtch.isChecked();
            Integer i = (Integer) swtch.getTag();
            MerchantModal merchantModal = filterData_merchantModals.get(i.intValue());
            if (merchantModal.getParent().equals("loyaltyProgramSignUp")) {
                if (isChecked) {
                    terms.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.translate_up);
                    terms.startAnimation(animation);
                    WebView w = (WebView)terms.findViewById(R.id.wv_terms);
                    w.loadData( merchantModal.getTermsConditions(),"text/html", "UTF-8");
                    terms.findViewById(R.id.txt_accept).setOnClickListener(new MyClass(i.intValue(), swtch, 1));
                    terms.findViewById(R.id.lalayout_ivBack).setOnClickListener(new MyClass(i.intValue(),swtch,2));

                } else {
                    unsubscribeMerchant(i.intValue(),swtch,true);
                }

            } else if (merchantModal.getParent().equals("enableMerchantVouchers")) {
                if (isChecked) {
                    enableMerchant(i.intValue(),swtch);
                } else {
                    disableMerchant(i.intValue(),swtch);
                }
            }
        }
    };


    private void  setSubscription(int index , boolean isSubscribed){

        filterData_merchantModals.get(index).setIsSubscribedMerchant(isSubscribed);

        String merchantId= filterData_merchantModals.get(index).getMerchantid() ;
        for (int i=0;i< originalData_merchantModals.size();i++){
            if(originalData_merchantModals.get(index).getMerchantid().equalsIgnoreCase(merchantId)){
                originalData_merchantModals.get(index).setIsSubscribedMerchant(isSubscribed);
                break;
            }
        }
    }


    private void subscribeMerchant(final int index, final ToggleButton aSwitch){
        MerchantModal modal = filterData_merchantModals.get(index);
        String merchantId = modal.getMerchantid();
        String barcode = modal.getBarcode()==null ? "":modal.getBarcode();
        barcode = barcode.length()==0 ? getBarcode(currentUserId, merchantId) : barcode;
        LoadingBox.showLoadingDialog(mcontext,"Loading");
        RestClient.getApiServiceForPojo().SubscribeMerchant(currentUserId, merchantId,"", new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("OUTPUT", jsonElement.toString());
                Log.e("URL", response.getUrl());
                // CODE CHANGE DUE TO PHASE 2
                //Merchant_ responseModal = (Merchant_) gson.fromJson(jsonElement.toString(), listType);
                /*if (responseModal != null && responseModal.getResponseMessage().equals("Success.")) {
                    MessageDialog.showDialog(mcontext, "Merchant has been successfully enabled", false);
                    setSubscription(index, true);

                } else {
                    MessageDialog.showDialog(mcontext,responseModal.getResponseMessage() ,false);
                    aSwitch.setChecked(false);

                }*/
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Merchant_>>(){}.getType();
                List<Merchant_> responseModal = (List<Merchant_>) gson.fromJson(jsonElement.toString(), listType);
                if (responseModal.get(0).getResponseCode().equals("1")) {
                    // Creating Subscriber Merchant data into database
                    MessageDialog.showDialog(mcontext, "Merchant has been successfully enabled", false);
                    setSubscription(index, true);
                } else {
                    MessageDialog.showDialog(mcontext,responseModal.get(0).getResponseMessage() ,false);
                    aSwitch.setChecked(false);
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("URL", error.getUrl());
                ErrorCodes.checkCode(mcontext, error);
                LoadingBox.dismissLoadingDialog();
            }
        });
    }


    public String getRandomNumber(int digCount) {
        Random rnd= new Random();
        StringBuilder sb = new StringBuilder(digCount);
        for(int i=0; i < digCount; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return sb.toString();
    }

    private String getBarcode(String Ist, String IInd) {
        return Ist+IInd+getRandomNumber(6);
    }


    private void unsubscribeMerchant(final   int index, final ToggleButton aSwitch, final boolean isdialogShow){
        MerchantModal modal = filterData_merchantModals.get(index);
        String merchantId = modal.getMerchantid();
        Log.e("currentUserId" + currentUserId, "merchantId" + merchantId);
        RestClient.getApiServiceForPojo().UnsubscribeMerchant(currentUserId,merchantId,"",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("OUTPUT", jsonElement.toString());
                Log.e("URL", response.getUrl());
                Type listType = new TypeToken<ResponseModal>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal responseModal = (ResponseModal) gson.fromJson(jsonElement.toString(), listType);
                if(responseModal!=null && responseModal.getResponseMessage().equals("Success.")) {
                    if (isdialogShow) {
                        MessageDialog.showDialog(mcontext, "Merchant has been successfully disabled", false);
                    }

                    setSubscription(index, false);
                }else
                {
                    if(isdialogShow){
                        MessageDialog.showDialog(mcontext,responseModal.getResponseMessage() ,false);
                    }

                    aSwitch.setChecked(true);
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("URL",error.getUrl());
                ErrorCodes.checkCode(mcontext, error);
                LoadingBox.dismissLoadingDialog();
            }
        });
    }
    private void enableMerchant(final int index , final ToggleButton aSwitch){
        MerchantModal modal = filterData_merchantModals.get(index);
        String merchantId = modal.getMerchantid();
        LoadingBox.showLoadingDialog(mcontext,"Loading");
        RestClient.getApiServiceForPojo().enableMerchant(currentUserId,merchantId,"",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d("enableMerchant ", jsonElement.toString());
                Log.d("URL ", response.getUrl());
                Type listType = new TypeToken<ResponseModal>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal responseModal = (ResponseModal) gson.fromJson(jsonElement.toString(), listType);
                if(responseModal!=null && responseModal.getResponseMessage().equals("Success.")){
                    MessageDialog.showDialog(mcontext, "Merchant has been successfully enabled", false);
                    setSubscription(index,true);
                }
                else {
                    MessageDialog.showDialog(mcontext,responseModal.getResponseMessage() ,false);
                    aSwitch.setChecked(false);
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Response",error.getUrl());
                ErrorCodes.checkCode(mcontext, error);
                LoadingBox.dismissLoadingDialog();
            }
        });
    }
    private void disableMerchant(final int index , final  ToggleButton aSwitch){
        MerchantModal modal = filterData_merchantModals.get(index);
        String merchantId = modal.getMerchantid();
        LoadingBox.showLoadingDialog(mcontext,"Loading");
        RestClient.getApiServiceForPojo().DisableMerchant(currentUserId, merchantId, "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d("disableMerchant ", jsonElement.toString());
                Log.d("URL ", response.getUrl());
                Type listType = new TypeToken<ResponseModal>() {
                }.getType();
                Gson gson = new Gson();
                ResponseModal responseModal = (ResponseModal) gson.fromJson(jsonElement.toString(), listType);
                if(responseModal!=null && responseModal.getResponseMessage().equals("Success.")){
                    MessageDialog.showDialog(mcontext, "Merchant has been successfully disabled",false);
                    setSubscription(index,false);
                }
                else
                {
                    MessageDialog.showDialog(mcontext,responseModal.getResponseMessage() ,false);
                    aSwitch.setChecked(true);
                }
                LoadingBox.dismissLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(mcontext,error);
            }
        });
    }
    @Override
    public Filter getFilter() {
        return mfilter;
    }

    private class ViewHolder {
        ImageView iv_profileImage;
        TextView txtMerchant;
        ToggleButton swtch;
        RelativeLayout layout_myPoint;
    }

    class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MerchantModal> temp = new ArrayList<MerchantModal>();
            FilterResults results = new FilterResults();
            for(int cnt = 0;cnt<originalData_merchantModals.size() ; cnt++)
            {
                if(originalData_merchantModals.get(cnt).getBussinessName().toLowerCase().contains(constraint.toString().toLowerCase())){
                    temp.add(originalData_merchantModals.get(cnt));
                }
            }
            results.values = temp;
            results.count = temp.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterData_merchantModals = (ArrayList <MerchantModal>)results.values;
            notifyDataSetChanged();
        }
    }

    class MyClass implements View.OnClickListener{
        int value ; ToggleButton view;int id;
        public MyClass (int value , ToggleButton view,int id){
            this.value = value;
            this.view= view;
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            if (id==1) {
                Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.translate_down);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        subscribeMerchant(value, view);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                terms.startAnimation(animation);
                terms.setVisibility(View.GONE);

            }if(id == 2)
            {
                Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.translate_down);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        unsubscribeMerchant(value, view,false);
                        view.setChecked(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                terms.startAnimation(animation);
                terms.setVisibility(View.GONE);
            }

        }
    };

}
