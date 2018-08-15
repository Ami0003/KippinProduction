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
import android.widget.Filterable;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.FriendList.FriendDetail;
import com.kippinretail.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sandeep.saini on 3/21/2016.
 */
public class FriendListAdapter extends BaseAdapter implements Filterable
{
    private LayoutInflater inflater = null;
    private Context mcontext;
    private List<FriendDetail> originalData_friendDetail;
    private List<FriendDetail> filtereddata;
    int hei,wei;
    private ItemFilter mfilter = new ItemFilter();

    public FriendListAdapter(List<FriendDetail> friendDetail, Activity mcontext)
    {
        this.originalData_friendDetail = friendDetail;
        filtereddata = originalData_friendDetail;
        this.mcontext = mcontext;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hei = CommonUtility.dpToPx(mcontext, 60);
        wei = CommonUtility.dpToPx(mcontext,60);
    }

    @Override
    public int getCount()
    {
        return filtereddata.size();
    }

    @Override
    public Object getItem(int position) {
        return filtereddata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View lastView;
    int lastPosition = -1;
    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder = null;
        if(view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.subitem_friendlist,parent,false);
            holder.profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
            holder.txtFriend = (TextView)view.findViewById(R.id.txtFriend);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        if(filtereddata.get(position).getProfileImage()!=null && !filtereddata.get(position).getProfileImage().equals("")){
            Picasso.with(mcontext).load(filtereddata.get(position).getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei,hei).into(holder.profile_image);
            Log.e("Inside If ",filtereddata.get(position).getProfileImage()+"");
        }else{
            Picasso.with(mcontext).load(filtereddata.get(position).getProfileImage()).placeholder(R.drawable.icon_placeholder).resize(wei,hei).into(holder.profile_image);
            Log.e("Inside else ", filtereddata.get(position).getProfileImage()+"");
        }
        if(filtereddata.get(position)!=null && filtereddata.get(position).getFirstname()!=null)
        {
            holder.txtFriend.setText(filtereddata.get(position).getFirstname());
            if(filtereddata.get(position).getLastname()!=null){
                holder.txtFriend.setText(filtereddata.get(position).getFirstname()+" "+filtereddata.get(position).getLastname());
            }
        }

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

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    class ViewHolder
    {
        CircleImageView profile_image;
        TextView txtFriend;
    }

    class ItemFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("Inside performFiltering","Inside performFiltering"+constraint);
            List<FriendDetail> temp = originalData_friendDetail;
            Log.e("Size of temp   ",temp.size()+"");
            List<FriendDetail> filterdata = new ArrayList<FriendDetail>();
            FilterResults results = new FilterResults();

            for(int cnt = 0;cnt<temp.size() ; cnt++)
            {
                if(temp.get(cnt).getUsername().toLowerCase().contains(constraint.toString().toLowerCase())){
                    filterdata.add(temp.get(cnt));
                }
            }
            Log.e("filterdata",filterdata.size()+"");
        results.values = filterdata;
        results.count = filterdata.size();
        return results;
        }

@Override
protected void publishResults(CharSequence constraint, FilterResults results) {
        filtereddata = (ArrayList <FriendDetail>)results.values;
        notifyDataSetChanged();
        }
        }
        }
