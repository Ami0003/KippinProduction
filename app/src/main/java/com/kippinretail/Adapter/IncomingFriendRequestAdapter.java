package com.kippinretail.Adapter;

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
import com.kippinretail.Modal.FriendList.ModalFriends;
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
 * Created by gaganpreet.singh on 5/1/2016.
 */
public class IncomingFriendRequestAdapter extends BaseAdapter implements Filterable {


    SuperActivity mcontext;
    ArrayList<ModalFriends> modalFriendses;
    ArrayList<ModalFriends> list_data;
    LayoutInflater layoutInflater;
    private int hei,wei;
    invite[] positions = null;
    private ItemFilter mfilter = new ItemFilter();
    public IncomingFriendRequestAdapter(SuperActivity mcontext, ArrayList<ModalFriends> modalFriendses,RequestType requestType) {
        this.mcontext = mcontext;
     this.list_data =    this.modalFriendses = modalFriendses;
        positions = new invite[modalFriendses.size()];
        layoutInflater = mcontext.getLayoutInflater();
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext, 60);
    }

    @Override
    public int getCount() {
        return modalFriendses.size();
    }

    @Override
    public Object getItem(int position) {
        return modalFriendses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder = new ViewHolder();

        if(convertView==null){
        convertView = layoutInflater.inflate(R.layout.item_friend_list, parent, false);
        viewHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
        viewHolder.tvAccept = (TextView) convertView.findViewById(R.id.tv_Invite);
        viewHolder.tvReject = (TextView) convertView.findViewById(R.id.tv_Reject);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        viewHolder.tvReject.setVisibility(View.VISIBLE);
        convertView.setTag(viewHolder);
       }else{
          viewHolder = (ViewHolder) convertView.getTag();
      }

        final  ViewHolder viewHolder1 = viewHolder;

        if(modalFriendses.get(position).getProfileImage()!=null && !(modalFriendses.get(position).getProfileImage().equals(""))){
            Picasso.with(mcontext).load(modalFriendses.get(position).getProfileImage()).resize(hei,wei).into(viewHolder.profile_image);
        }
        else {
            Picasso.with(mcontext).load(modalFriendses.get(position).getProfileImage()).resize(hei,wei).placeholder(R.drawable.user).into(viewHolder.profile_image);
        }



        viewHolder.tvName.setText(modalFriendses.get(position).getFirstname() + " " + modalFriendses.get(position).getLastname());


        if (positions[position] == null)
            positions[position] = invite.NORMAL;

        switch (positions[position]) {


            case SENT:
                changeAcceptanceStatus(viewHolder ,true);
                break;


            case REJECT:
                changeAcceptanceStatus(viewHolder,false);
                break;

            default:
                viewHolder.tvReject.setVisibility(View.VISIBLE);
                viewHolder.tvReject.setVisibility(View.VISIBLE);
                break;
        }


        viewHolder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(position, true , viewHolder1);
            }

        });

        viewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(position, false, viewHolder1);

            }

        });

        return convertView;
    }


    class ViewHolder {

        TextView tvName;
        CircleImageView profile_image;
        TextView tvAccept, tvReject;
    }


    private void changeAcceptanceStatus( ViewHolder viewHolder, boolean isAccepted) {
        if (isAccepted) {
            viewHolder.tvAccept.setText("Accepted");
            viewHolder.tvReject.setText("Rejected");
            /*viewHolder.tvAccept.setBackgroundColor(Color.BLUE);
            viewHolder.tvReject.setVisibility(View.GONE);*/
        } else {
            viewHolder.tvAccept.setText("Accepted");
            viewHolder.tvReject.setText("Rejected");

          /*  viewHolder.tvReject.setBackgroundColor(Color.BLUE);
            viewHolder.tvAccept.setVisibility(View.GONE);*/
        }
    }


    private void accept(final int position, final boolean isAccepted , ViewHolder viewHolder) {

//
//        ("Account/User/AcceptFriendRequestForTradePoints/")+"\(arrayFriendsID![tappedIndexPath.row])"+"/"+"\(CustomerID)", parameters:
//        nil)

        if (isAccepted)
            RestClientAdavanced.getApiServiceForPojo(mcontext)
                    .AcceptFriendRequestForTradePoints(
                            modalFriendses.get(position).getId(),
                            CommonData.getUserData(mcontext).getId()
                            , ""
                            , mcontext.getCallback(new MyCallback(position, isAccepted , viewHolder)));

        else
            RestClientAdavanced.getApiServiceForPojo(mcontext)
                    .RejectFriendRequestForTradePoints(
                            modalFriendses.get(position).getId(),
                            CommonData.getUserData(mcontext).getId()
                            , ""
                            , mcontext.getCallback(new MyCallback(position, isAccepted , viewHolder)));
    }

    private enum invite {

        NORMAL, SENT, REJECT
    }


    class MyCallback implements Callback<JsonElement> {

        int position;
        boolean isAccepted;
        ViewHolder viewHolder;


        public MyCallback(int position, boolean isAccepted,ViewHolder viewHolder) {

            this.position = position;
            this.isAccepted = isAccepted;
            this.viewHolder = viewHolder;
        }


        @Override
        public void success(JsonElement jsonElement, Response response) {

            if (Boolean.parseBoolean(jsonElement.toString().toString())) {

                positions[position] = (isAccepted ? invite.SENT : invite.REJECT);

                modalFriendses.get(position).setIsInvited(isAccepted);
                changeAcceptanceStatus(viewHolder,isAccepted);
                if (isAccepted) {
                    remomeFriendNotification(position,"Friend request accepted succesfully",isAccepted);
                    CommonUtility.isMoveForward = true;
     ;
                } else {
                    remomeFriendNotification(position,"Friend request rejected",isAccepted);
                    CommonUtility.isMoveForward = true;

                }



            } else {
                if (isAccepted)
                    MessageDialog.showDialog(mcontext, "Fail",true);
                else
                    MessageDialog.showDialog(mcontext, "Fail",true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(error.toString(), error.getMessage());
        }
    }

    private void remomeFriendNotification(int index, final String message,boolean isaccepted  ) {
        final boolean result;
        String userId = String.valueOf(CommonData.getUserData(mcontext).getId());

        RestClient.getApiServiceForPojo().RemoveIsReadInvite(String.valueOf(modalFriendses.get(index).getId()),userId,"",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Gson gson = new Gson();
                Boolean serverresult = gson.fromJson(jsonElement.toString(), Boolean.class);
                if(serverresult.booleanValue()){
                    MessageDialog.showDialog(mcontext, message, true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("",error.getMessage()+error.getUrl());
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
            List<ModalFriends> temp = list_data;

            List<ModalFriends> filterdata = new ArrayList<ModalFriends>();
            FilterResults results = new FilterResults();

            for (int cnt = 0; cnt < temp.size(); cnt++) {
                android.util.Log.e("===" + temp.get(cnt).getUsername(), constraint.toString());
                if (temp.get(cnt).getFirstname().toLowerCase().contains(constraint.toString().toLowerCase() )) {
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
            modalFriendses = (ArrayList<ModalFriends>) results.values;
            notifyDataSetChanged();
        }
    }
}
