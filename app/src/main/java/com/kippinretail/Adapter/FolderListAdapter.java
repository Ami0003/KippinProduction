package com.kippinretail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.FolderList.FolderDetail;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by sandeep.saini on 3/24/2016.
 */
public class FolderListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private Context mcontext;
    private List<FolderDetail> folderDetail;

    public FolderListAdapter( Context mcontext,List<FolderDetail> folderDetail)
    {
        this.folderDetail = folderDetail;
        this.mcontext = mcontext;
        inflater =(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return folderDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if(view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.subitem_friendlist,parent,false);
            holder.txtFriend = (TextView)view.findViewById(R.id.txtFriend);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        if(folderDetail.get(position)!=null && folderDetail.get(position).getFolderName()!=null)
        {
            holder.txtFriend.setText(folderDetail.get(position).getFolderName());
        }
        return view;
    }
    class ViewHolder
    {
        TextView txtFriend;
    }
}
