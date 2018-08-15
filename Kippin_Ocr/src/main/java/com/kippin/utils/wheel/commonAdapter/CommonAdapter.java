package com.kippin.utils.wheel.commonAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippin.kippin.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class CommonAdapter extends BaseAdapter {

    Context context ;
    ArrayList<String> datas;

    int selectedPosition = -1;

    TextView oldTextView;

    CommonAdapterCallaback commonAdapterCallaback;

    private boolean isDataLoaded;

        public CommonAdapter(Context context, ArrayList<String> datas,CommonAdapterCallaback commonAdapterCallaback){
            this.context= context;
            this.datas = datas;
            this.commonAdapterCallaback  = commonAdapterCallaback;

        }



    @Override
    public int getCount() {
        return datas.size();

    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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


        final View view= ( ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_item_classification1,null,false));

        final TextView textView = (TextView) view.findViewById(R.id.tvClassification);

        textView.setText(datas.get(position));

        if(position==selectedPosition) oldTextView = textView;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
             CommonAdapter.this.commonAdapterCallaback.onClick(position);
                switchSelection(textView);
            }
        });


        if(position!=selectedPosition){
            setColorUnSelection(textView);
        }else setColorSelection(textView);

        return view;
    }




    private void switchSelection(TextView newTextView){
        setColorUnSelection(oldTextView);
        setColorSelection(newTextView);
    }

    private void setColorSelection(TextView textView){
        changeColor(textView,Color.LTGRAY);
    }


    private void setColorUnSelection(TextView textView){
        changeColor(textView,Color.WHITE);
    }

    private void changeColor(TextView textView , int color){
        if(textView==null)return;
        textView.setBackgroundColor(color) ;
    }


}
