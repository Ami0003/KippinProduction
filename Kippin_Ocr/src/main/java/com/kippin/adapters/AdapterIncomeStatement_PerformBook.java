package com.kippin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.interfaces.IntefaceEntryItemListener;
import com.kippin.kippin.R;
import com.kippin.utils.Utility;
import com.kippin.utils.pinnedlistview.EntryItem;
import com.kippin.utils.pinnedlistview.Item;
import com.kippin.utils.pinnedlistview.SectionItem;
import com.pack.kippin.PerformBook;

import java.util.ArrayList;


public class AdapterIncomeStatement_PerformBook extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;
    private ListView listView;
    private IntefaceEntryItemListener intefaceEntryItemListener;

    public AdapterIncomeStatement_PerformBook(Context context,ListView listView, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.listView = listView;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public AdapterIncomeStatement_PerformBook(Context context, ListView listView, ArrayList<Item> items, IntefaceEntryItemListener intefaceEntryItemListener) {
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



        final Item i = items.get(position);
        if (i != null) {
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
        }



        return v;
    }

}


