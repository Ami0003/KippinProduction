package com.kippinretail.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.kippinretail.Activity_MyPoint_InviteKippinFriends_FriendList;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.FriendList.ModalFriends;
import com.kippinretail.R;
import com.kippinretail.app.Retail;
import com.kippinretail.retrofit.RestClientAdavanced;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gaganpreet.singh on 4/24/2016.
 */
public class FriendAdapter extends BaseAdapter implements Filterable {

    Activity_MyPoint_InviteKippinFriends_FriendList activity_myPoint_inviteKippinFriends_friendList;
    ArrayList<ModalFriends> modalFriendses;
    ArrayList<ModalFriends> originalData_modalFriendses;
    LayoutInflater layoutInflater;
    int hei, wei;
    Activity_MyPoint_InviteKippinFriends_FriendList.ListType temp;
    FriendListFilter mFilter = new FriendListFilter();

    public FriendAdapter(Activity_MyPoint_InviteKippinFriends_FriendList activity_myPoint_inviteKippinFriends_friendList, ArrayList<ModalFriends> modalFriendses, Activity mContextc) {
        this.activity_myPoint_inviteKippinFriends_friendList = activity_myPoint_inviteKippinFriends_friendList;
        this.originalData_modalFriendses = this.modalFriendses = modalFriendses;
        layoutInflater = activity_myPoint_inviteKippinFriends_friendList.getLayoutInflater();
        hei = CommonUtility.dpToPx(activity_myPoint_inviteKippinFriends_friendList, 60);
        wei = CommonUtility.dpToPx(activity_myPoint_inviteKippinFriends_friendList, 60);
    }

    public FriendAdapter(Activity_MyPoint_InviteKippinFriends_FriendList activity_myPoint_inviteKippinFriends_friendList, ArrayList<ModalFriends> modalFriendses, Activity_MyPoint_InviteKippinFriends_FriendList.ListType temp) {
        this.activity_myPoint_inviteKippinFriends_friendList = activity_myPoint_inviteKippinFriends_friendList;
        this.originalData_modalFriendses = this.modalFriendses = modalFriendses;
        layoutInflater = activity_myPoint_inviteKippinFriends_friendList.getLayoutInflater();
        hei = CommonUtility.dpToPx(activity_myPoint_inviteKippinFriends_friendList, 60);
        wei = CommonUtility.dpToPx(activity_myPoint_inviteKippinFriends_friendList, 60);
        this.temp = temp;
    }

    @Override
    public int getCount() {
        if (modalFriendses != null) {
            return modalFriendses.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return modalFriendses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View lastView;
    int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();
        ;

        //     if(convertView==null){

        convertView = layoutInflater.inflate(R.layout.item_friend_list, parent, false);
        viewHolder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
        viewHolder.tvInvite = (TextView) convertView.findViewById(R.id.tv_Invite);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        convertView.setTag(viewHolder);
        //    }else{
        //   viewHolder = (ViewHolder) convertView.getTag(position);
        //    }

        switch (temp) {
            case SHOW_FRIENDS:
                break;
            case INVITE_FRIENDS:
                viewHolder.tvInvite.setText("Invite");
                viewHolder.tvInvite.setBackgroundColor(activity_myPoint_inviteKippinFriends_friendList.getResources().getColor(R.color.red));
                break;
        }
        if (modalFriendses.get(position).getProfileImage() != null) {

            Picasso.with(activity_myPoint_inviteKippinFriends_friendList).
                    load(modalFriendses.get(position).getProfileImage()).placeholder(R.drawable.user).into(viewHolder.profile_image);

        } else {
            Picasso.with(activity_myPoint_inviteKippinFriends_friendList).
                    load(modalFriendses.get(position).getProfileImage()).into(viewHolder.profile_image);
        }


        viewHolder.tvName.setText(modalFriendses.get(position).getFirstname() + " " + modalFriendses.get(position).getLastname());

        if (modalFriendses.get(position).getIsInvited()) {
            viewHolder.tvInvite.setText("Invited");
            viewHolder.tvInvite.setBackgroundColor(Retail.res.getColor(R.color.light_blue));
            viewHolder.tvInvite.setOnClickListener(null);
        } else {
            viewHolder.tvInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (modalFriendses.get(position).getIsInvited()) {
//                        cancelInvite(position);
                    } else {
                        invite(position);
                    }

                }


                private void invite(int position) {
                    RestClientAdavanced.getApiServiceForPojo(activity_myPoint_inviteKippinFriends_friendList)
                            .InviteFriendForTradePoints(CommonData.getUserData(activity_myPoint_inviteKippinFriends_friendList).getId()
                                    , modalFriendses.get(position).getId()
                                    , ""
                                    , activity_myPoint_inviteKippinFriends_friendList.getCallback(new Callback<JsonElement>() {
                                        @Override
                                        public void success(JsonElement jsonElement, Response response) {
                                            if (Boolean.parseBoolean(jsonElement.toString().toString())) {
                                                MessageDialog.showDialog(activity_myPoint_inviteKippinFriends_friendList, "Friend request sent to user", Activity_MyPoint_InviteKippinFriends_FriendList.class);
                                                //MessageDialog.showDialog(activity_myPoint_inviteKippinFriends_friendList, "Friend request sent to user", UserGiftCardActivity.class);
                                                viewHolder.tvInvite.setText("Invited");
                                                viewHolder.tvInvite.setBackgroundColor(activity_myPoint_inviteKippinFriends_friendList.getResources().getColor(R.color.light_blue));
                                                activity_myPoint_inviteKippinFriends_friendList.finish();
                                            } else {
                                                MessageDialog.showDialog(activity_myPoint_inviteKippinFriends_friendList, "Invite Sent Cancelled");
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.e(error.toString(), error.getMessage());
                                        }
                                    }));
                }
            });
        }

        final View finalView = convertView;
        convertView.setOnTouchListener(new View.OnTouchListener() {
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
        });


        if (lastPosition == position) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else if (lastPosition != position && lastPosition >= 0) {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    class ViewHolder {

        TextView tvName;
        CircleImageView profile_image;
        TextView tvInvite;
    }

    class FriendListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ModalFriends> temp = new ArrayList<ModalFriends>();
            FilterResults results = new FilterResults();
            for (ModalFriends friend : originalData_modalFriendses) {
                StringBuilder name = new StringBuilder();
                if (friend.getFirstname() != null) {
                    name.append(friend.getFirstname());
                }
                if (friend.getLastname() != null) {
                    name.append(friend.getLastname());
                }
                if (name.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    temp.add(friend);
                }
            }
            results.values = temp;
            results.count = temp.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modalFriendses = (ArrayList<ModalFriends>) results.values;
            notifyDataSetChanged();
        }
    }
}
