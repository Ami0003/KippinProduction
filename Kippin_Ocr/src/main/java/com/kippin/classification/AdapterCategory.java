package com.kippin.classification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.webclient.model.ModelCategoryList;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class AdapterCategory extends BaseAdapter {

    Context context ;
    ArrayList<ModelCategoryList> categories;

    private boolean isDataLoaded;

        public AdapterCategory(Context context, ArrayList<ModelCategoryList> categories){
            this.context= context;
            this.categories = categories;
        }

    @Override
    public int getCount() {
        return categories.size();

    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(isDataLoaded && position==0){
            return new View(context);
        }

        View view= ((View)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_item_classification1,parent,false));

        TextView textView = ((TextView)view.findViewById(R.id.tvClassification));
        textView.setText(categories.get(position).getCategoryType());



        return view;
    }

    public void  disableDefaultItem(){

        isDataLoaded = true;


    }

}
