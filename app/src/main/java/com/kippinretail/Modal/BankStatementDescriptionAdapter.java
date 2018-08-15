package com.kippinretail.Modal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForGiftAnalytics;
import com.kippinretail.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaganpreet.singh on 1/27/2016.
 */
public class BankStatementDescriptionAdapter extends BaseAdapter {

    private Activity context;
    private List<ResponseForGiftAnalytics> modelBankStatements_;

    public BankStatementDescriptionAdapter(Activity activityBankStatement,   List<ResponseForGiftAnalytics> modelBankStatements_ ) {
        this.context = activityBankStatement;
        this.modelBankStatements_=modelBankStatements_;
    }

    @Override
    public int getCount() {
        return modelBankStatements_.size();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return modelBankStatements_.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ResponseForGiftAnalytics statement = modelBankStatements_.get(position);

        if(true){
            viewHolder =new ViewHolder();
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_bank_statement_detail_1, parent, false);

//            viewHolder .tvDesc = (TextView)convertView.findViewById(R.id.tvDescription);
            viewHolder .tvGiftCardCount = (TextView)convertView.findViewById(R.id.tvGiftCardCount);
            viewHolder .tvCountry = (TextView)convertView.findViewById(R.id.tvCountry);


            convertView.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        if(position%2==0){
//            convertView.setBackgroundColor((context.getResources().getColor(R.color.bank_statement_even_items)));
//        }
//        else{
//            convertView.setBackgroundColor(Color.WHITE);
//        }

        viewHolder.tvGiftCardCount.setText(statement.getGiftcardCount());
        viewHolder.tvCountry.setText(statement.getLocation());
        return convertView;
    }



    class ViewHolder{
        TextView  tvGiftCardCount,tvCountry;
    }

}
