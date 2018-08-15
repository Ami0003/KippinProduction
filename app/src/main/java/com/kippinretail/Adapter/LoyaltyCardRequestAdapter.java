package com.kippinretail.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.ApplicationuUlity.RequestType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ServerResponseForLoyaltyRequest;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sandeep.singh on 8/1/2016.
 */
public class LoyaltyCardRequestAdapter  extends BaseAdapter implements Filterable {


    SuperActivity mcontext;
    List<ServerResponseForLoyaltyRequest> list_data,originalData;
    LayoutInflater layoutInflater;
    private ItemFilter mfilter = new ItemFilter();
    private int hei,wei;
    invite[] positions = null;

    public LoyaltyCardRequestAdapter(SuperActivity mcontext, List<ServerResponseForLoyaltyRequest> list_data) {
        this.mcontext = mcontext;
       this.originalData =  this.list_data = list_data;
        positions = new invite[list_data.size()];
        layoutInflater = mcontext.getLayoutInflater();
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext, 60);
    }
    public LoyaltyCardRequestAdapter(SuperActivity mcontext, ArrayList<ServerResponseForLoyaltyRequest> list_data,RequestType requestType) {
        this.mcontext = mcontext;
        this.list_data = list_data;
        positions = new invite[list_data.size()];
        layoutInflater = mcontext.getLayoutInflater();
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext, 60);
    }

    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        viewHolder = new ViewHolder();

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_accept_loyalty, parent, false);
            viewHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            viewHolder.tvAccept = (TextView) convertView.findViewById(R.id.tvInvite);
            viewHolder.tvReject = (TextView) convertView.findViewById(R.id.tvReject);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvReject.setVisibility(View.VISIBLE);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvAccept.setBackgroundColor(Color.parseColor("#FF0000"));
        }

        if(list_data.get(position).getLogoImage()!=null && !(list_data.get(position).getLogoImage().equals(""))){
            Picasso.with(mcontext).load(list_data.get(position).getLogoImage().replace("//","/")).placeholder(R.drawable.icon_placeholder).resize(hei, wei).into(viewHolder.profile_image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Log.e("Inside on Sucess","Inside on Sucess");
                }

                @Override
                public void onError() {
                    Log.e("Inside on error","Inside on error");
                }
            });
            Log.e("path",list_data.get(position).getLogoImage());
        }
        /*else {
            Picasso.with(mcontext).load(list_data.get(position).getLogoImage()).placeholder(R.drawable.icon_placeholder).resize(hei, wei).placeholder(R.drawable.user).into(viewHolder.profile_image);
        }*/



        viewHolder.tvName.setText(list_data.get(position).getFolderName() );


        if (positions[position] == null)
            positions[position] = invite.NORMAL;

        switch (positions[position]) {


            case SENT:
                changeAcceptanceStatus(true);
                break;


            case REJECT:
                changeAcceptanceStatus(false);
                break;

            default:
                viewHolder.tvReject.setVisibility(View.VISIBLE);
                viewHolder.tvReject.setVisibility(View.VISIBLE);

                break;
        }


        viewHolder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(position, true);
            }

        });

        viewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(position, false);

            }

        });

        return convertView;
    }


    class ViewHolder {

        TextView tvName;
        CircleImageView profile_image;
        TextView tvAccept, tvReject;
    }


    private void changeAcceptanceStatus(boolean isAccepted) {
        if (isAccepted) {
            viewHolder.tvAccept.setText("Accepted");
            viewHolder.tvAccept.setBackgroundColor(Color.BLUE);
            viewHolder.tvReject.setVisibility(View.GONE);
        } else {
            viewHolder.tvReject.setText("Rejected");
            viewHolder.tvReject.setBackgroundColor(Color.BLUE);
            viewHolder.tvAccept.setVisibility(View.GONE);
        }
    }


    private void accept(final int position, final boolean isAccepted) {

//
//        ("Account/User/AcceptFriendRequestForTradePoints/")+"\(arrayFriendsID![tappedIndexPath.row])"+"/"+"\(CustomerID)", parameters:
//        nil)

        if (isAccepted)
            RestClientAdavanced.getApiServiceForPojo(mcontext)
                    .AcceptLoyaltyCard(
                            list_data.get(position).getSenderId(),
                            list_data.get(position).getReceiverId()
                            , list_data.get(position).getTransferId(), ""
                            , mcontext.getCallback(new MyCallback(position, isAccepted)));

        else
            RestClientAdavanced.getApiServiceForPojo(mcontext)
                    .RejectLoyaltyCard(
                            list_data.get(position).getSenderId(),
                            list_data.get(position).getReceiverId()
                            ,list_data.get(position).getTransferId(),
                            list_data.get(position).getFolderName(),""
                            , mcontext.getCallback(new MyCallback(position, isAccepted)));
    }

    private enum invite {

        NORMAL, SENT, REJECT
    }


    class MyCallback implements Callback<JsonElement> {

        int position;
        boolean isAccepted;


        public MyCallback(int position, boolean isAccepted) {

            this.position = position;
            this.isAccepted = isAccepted;
            removeNotificationForNonKippinLoyaltyCard(position);

        }


        @Override
        public void success(JsonElement jsonElement, Response response) {


            // IN ACCEPT I RECEIVE ON BIT USING IT I REMOVE NOTI
            if (Boolean.parseBoolean(jsonElement.toString().toString())) {

                positions[position] = (isAccepted ? invite.SENT : invite.REJECT);

             list_data.get(position).setIsAccept(isAccepted);

                if (isAccepted) {
                    MessageDialog.showDialog(mcontext, "Loyalty card accepted successfully", true);
                } else {
                    MessageDialog.showDialog(mcontext, "Loyalty card rejected", true);
                }
               // changeAcceptanceStatus(isAccepted);

            } else {
                if (isAccepted)
                    MessageDialog.showDialog(mcontext, "Invite Sent not accepted");
                else
                    MessageDialog.showDialog(mcontext, "Fail");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(error.toString(), error.getMessage());
        }
    }

    private void removeNotificationForNonKippinLoyaltyCard(int position) {

        CommonUtility.isMoveForward=true;
        RestClient.getApiServiceForPojo().RemoveIsReadTradePoints(String.valueOf(CommonData.getUserData(mcontext).getId()), list_data.get(position).getBarCode(), "",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Gson gson = new Gson();
                Boolean serverresult = gson.fromJson(jsonElement.toString(), Boolean.class);
                if (serverresult.booleanValue()) {
                    Log.e("Notification ", "Remove ");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }


    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ServerResponseForLoyaltyRequest> temp = originalData;

            List<ServerResponseForLoyaltyRequest> filterdata = new ArrayList<ServerResponseForLoyaltyRequest>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                android.util.Log.e("===" + temp.get(cnt).getFolderName().toLowerCase(), constraint.toString());
                if (temp.get(cnt).getFolderName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterdata.add(temp.get(cnt));
                }
            }
            android.util.Log.e("filterdata", filterdata.size() + "");
            results.values = filterdata;
            results.count = filterdata.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list_data = (ArrayList<ServerResponseForLoyaltyRequest>) results.values;
            notifyDataSetChanged();
        }
    }
}
