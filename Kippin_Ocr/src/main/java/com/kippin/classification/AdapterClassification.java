package com.kippin.classification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.webclient.model.ModelClassification;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class AdapterClassification extends BaseAdapter {

    int index_assets  = -1;
    int index_expense  = -1;
    int index_revenue  = -1;
    int index_liability  = -1;
    int index_equity  = -1;


    Context context ;
    ArrayList<ModelClassification> modelClassifications ;

        public AdapterClassification(Context context , ArrayList<ModelClassification> modelClassifications){
            this.context= context;
            this.modelClassifications = modelClassifications;
        }

    @Override
    public int getCount() {
        return modelClassifications.size();

    }

    @Override
    public Object getItem(int position) {
        return modelClassifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View textView = ((View)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_item_classification1,parent,false));

        int catId  = Integer.parseInt(modelClassifications.get(position).getCategoryId());

        if(/*position>0  &&*/ (index_assets==-1  ||index_revenue==-1  || index_expense==-1 || index_equity==-1 || index_liability==-1)){
            switch (catId) {
                case 1:
                    if(index_assets==-1)
                    index_assets = position;
                    break;
                case 2 :
                    if(index_expense==-1)
                    index_expense = position ;
                    break;
                case 3 :
                    if(index_revenue==-1)
                    index_revenue = position ;
                    break;
                case 4 :
                    if(index_liability==-1)
                    index_liability = position;
                    break;
                case 5 :
                    if(index_equity==-1)
                    index_equity = position;
                    break;
            }
        }

            if(position == index_assets) {
                textView.findViewById(R.id.tvType).setVisibility(View.VISIBLE);
                textView.findViewById(R.id.vLine).setVisibility(View.VISIBLE);
                ((TextView)textView.findViewById(R.id.tvType)).setText("Assets");
            }else  if(position == index_expense) {
                textView.findViewById(R.id.tvType).setVisibility(View.VISIBLE);
                textView.findViewById(R.id.vLine).setVisibility(View.VISIBLE);
                ((TextView)textView.findViewById(R.id.tvType)).setText("Expense");
            }else    if(position == index_revenue) {
                textView.findViewById(R.id.tvType).setVisibility(View.VISIBLE);
                textView.findViewById(R.id.vLine).setVisibility(View.VISIBLE);
                ((TextView)textView.findViewById(R.id.tvType)).setText("Revenue");
            } else   if(position == index_liability) {
                textView.findViewById(R.id.tvType).setVisibility(View.VISIBLE);
                textView.findViewById(R.id.vLine).setVisibility(View.VISIBLE);
                ((TextView)textView.findViewById(R.id.tvType)).setText("Liability");
            }else    if(position == index_equity) {
                textView.findViewById(R.id.tvType).setVisibility(View.VISIBLE);
                textView.findViewById(R.id.vLine).setVisibility(View.VISIBLE);
                ((TextView)textView.findViewById(R.id.tvType)).setText("Equity");
            }else{
                textView.findViewById(R.id.tvType).setVisibility(View.GONE);
                textView.findViewById(R.id.vLine).setVisibility(View.GONE);
            }

        ((TextView)textView.findViewById(R.id.tvClassification)).setText( modelClassifications.get(position).getClassificationType());

        return textView;
    }
}
