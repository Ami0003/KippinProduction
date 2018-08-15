package com.kippin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kippin.app.Kippin;
import com.kippin.interfaces.IntefaceEntryItemListener;
import com.kippin.kippin.R;
import com.kippin.utils.Utility;
import com.kippin.utils.pinnedlistview.EntryItem;
import com.kippin.utils.pinnedlistview.Item;
import com.kippin.utils.pinnedlistview.SectionItem;
import com.library_for_stickylist.StickyListHeadersAdapter;
import com.library_for_stickylist.StickyListHeadersListView;
import com.pack.kippin.PerformBook;

import java.util.ArrayList;


public class AdapterIncomeStatement extends ArrayAdapter<Item> implements StickyListHeadersAdapter {

    private Context context;
    private final ArrayList<Item> items;
    private LayoutInflater vi;
    private StickyListHeadersListView listView;
    private IntefaceEntryItemListener intefaceEntryItemListener;

    public AdapterIncomeStatement(Context context,StickyListHeadersListView listView, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.listView = listView;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public AdapterIncomeStatement(Context context, StickyListHeadersListView listView, ArrayList<Item> items, IntefaceEntryItemListener intefaceEntryItemListener) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.listView = listView;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.intefaceEntryItemListener =intefaceEntryItemListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        SubItemHolder subItemHolder = null;
        final Item i = items.get(position);
        EntryItem ei = (EntryItem) i;

        try {
//            if (convertView == null)
            if (true)
            {
                subItemHolder = new SubItemHolder();
                convertView = vi.inflate(R.layout.list_item_entry, parent, false);
                subItemHolder.title = (TextView) convertView.findViewById(R.id.list_item_entry_title);
                subItemHolder.subtitle = (TextView) convertView.findViewById(R.id.list_item_entry_summary);
                convertView.setTag(subItemHolder);
            }else {
                subItemHolder = (SubItemHolder)convertView.getTag();
            }

            subItemHolder.title.setText(ei.title);
            subItemHolder.subtitle.setText(ei.subtitle);

            if (ei.color != 0) {
                subItemHolder.title.setTextColor(ei.color);
                subItemHolder.subtitle.setTextColor(Color.BLACK);
            }
            if(ei.isListItem){
                convertView.setVisibility(View.VISIBLE);
            }else{
                convertView.setVisibility(View.GONE);
            }
        /*if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(android.R.layout.preference_category, parent,false);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v;
                sectionView.setTextColor(Color.WHITE);

                if(si.getColor()!=0){

                    Log.e("debug", position + "-" + si.getColor());

                    sectionView.setBackgroundColor(si.getColor());

                    sectionView.setGravity(Gravity.CENTER);

                    sectionView.setPadding(Utility.dpToPx(context , 7),Utility.dpToPx(context , 7),Utility.dpToPx(context , 7),Utility.dpToPx(context , 7));

                }




                sectionView.setText(si.getTitle());


            } else {
                EntryItem ei = (EntryItem) i;
                v = vi.inflate(R.layout.list_item_entry, parent,false);
                final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
                final TextView subtitle = (TextView) v.findViewById(R.id.list_item_entry_summary);

                if (title != null)
                    title.setText(ei.title);
                if (subtitle != null)
                    subtitle.setText(ei.subtitle);

                if(ei.color!=0){
                    title.setTextColor(ei.color);
                    subtitle.setTextColor(Color.BLACK);
                }

            }
            if(!i.isSection() && intefaceEntryItemListener!=null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EntryItem entryItem = (EntryItem) i;
                        intefaceEntryItemListener.onClick(entryItem.itemPosition , entryItem.subPosition);
                    }
                });
            }
        }else{
            v = vi.inflate(android.R.layout.preference_category, parent,false);
        }*/


        }catch(Exception ex){
            Log.e("154",ex.getMessage());
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
      //  try {
            HeaderViewHolder holder;
//            if (convertView == null)
            if (true)
            {
                holder = new HeaderViewHolder();
                LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = i.inflate(R.layout.header_sticky_networklist1, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.text1);
                holder.headerLayout = (LinearLayout)convertView.findViewById(R.id.headerLayout);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            EntryItem temp = (EntryItem) items.get(position);
            if (temp!=null && temp.getHeaderTitle().length()>0) {
                convertView.setVisibility(View.VISIBLE);
                CharSequence headerChar = temp.getHeaderTitle();
                holder.headerLayout.setBackgroundColor(((EntryItem) items.get(position)).getColor());
                Log.e("final TextView subtitle", headerChar + "");
                if(headerChar.toString().startsWith("CTotal")){
                    StringBuilder sb = new StringBuilder(headerChar.toString());
                    sb.deleteCharAt(0);
                    holder.text.setText(sb.toString());
                }else{
                    holder.text.setText(headerChar);
                }

                return  convertView;
            }else{
               convertView.setVisibility(View.GONE);
                new View(context);
                return convertView;
            }

          /*  if(((EntryItem) items.get(position)).getHeaderTitle().equals("")){
                convertView.setVisibility(View.GONE);

            }else{
                convertView.setVisibility(View.VISIBLE);
            }*/
       /* }catch(Exception ex){
            Log.e("184",ex.getMessage());
        }*/


    }

    @Override
    public long getHeaderId(int position) {
        int longId = 0;
        try {

            longId = ((EntryItem)items.get(position)).getHeaderTitle().subSequence(0, 5).charAt(0);;
            Log.e("final TextView subtitle", ((EntryItem) items.get(position)).getHeaderTitle().subSequence(0, 5).charAt(0) + "   "+longId);
        }catch(Exception ex){
            Log.e("197",ex.getMessage());
        }
        return longId;
    }
    class HeaderViewHolder {
        TextView text;
        LinearLayout headerLayout;
    }


    class SubItemHolder{
        TextView title ;
        TextView subtitle ;
    }
}


