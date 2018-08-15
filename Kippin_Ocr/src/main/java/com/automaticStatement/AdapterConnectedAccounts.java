package com.automaticStatement;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.utils.Url;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalAutomaticStatement;
import com.kippin.webclient.model.TemplateData;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 5/24/2016.
 */
public class AdapterConnectedAccounts extends BaseAdapter {


    Activity activity;
    ArrayList<ModalAutomaticStatement> modalAutomaticStatements;
    LayoutInflater layoutInflater;

    OnRemoveListener onRemoveListener;

    public AdapterConnectedAccounts(Activity activity, ArrayList<ModalAutomaticStatement> modalAutomaticStatements, OnRemoveListener onRemoveListener) {
        this.activity = activity;
        this.modalAutomaticStatements = modalAutomaticStatements;
        layoutInflater = activity.getLayoutInflater();
        this.onRemoveListener = onRemoveListener;
    }

    public AdapterConnectedAccounts(Activity activity, ArrayList<ModalAutomaticStatement> modalAutomaticStatements) {
        this.activity = activity;
        this.modalAutomaticStatements = modalAutomaticStatements;
        layoutInflater = activity.getLayoutInflater();

    }


    @Override
    public int getCount() {
        return modalAutomaticStatements.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.subitem_connected_acc, null, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvBank = (TextView) convertView.findViewById(R.id.tvBankName);
            viewHolder.tvAcc = (TextView) convertView.findViewById(R.id.tvAccNum);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivDelete);
            viewHolder.btUnlinkAcc = (Button) convertView.findViewById(R.id.btUnlinkAcc);
            viewHolder.btUnlinkBank = (Button) convertView.findViewById(R.id.btUnlinkBank);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(modalAutomaticStatements.get(position).getAccountName());
        viewHolder.tvBank.setText(modalAutomaticStatements.get(position).getBankName());
        viewHolder.tvAcc.setText(modalAutomaticStatements.get(position).getAccountNumber());

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.ivImage.setBackgroundResource(R.drawable.delete_unselected);
        } else {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            viewHolder.ivImage.setBackgroundResource(R.drawable.delete_selected);
        }

       /* viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                [6:18:51 PM] ankush agnihotri: For removing account from yodlee
//                        [6:18:53 PM] ankush agnihotri: RemoveAccount/{UserId}/{siteAccountId}
//                        [6:19:22 PM] ankush agnihotri: Type POST
//                [6:19:40 PM] ankush agnihotri: Parameters: int UserId, string siteAccountId

                ArrayListPost templatePosts =  new ArrayListPost();

                WSUtils.hitService (activity, Url.getYodleeRemoveAccountApi(modalAutomaticStatements.get(position).getSiteAccountId()), templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {
                        System.out.print(""+data.data);
                        onRemoveListener.onRemove();
                    }
                }) ;

            }
        });*/

        viewHolder.btUnlinkAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayListPost templatePosts = new ArrayListPost();


                /**********************************************************************************************
                 * http://52.27.249.143/Kippin/Finance/KippinFinanceApi/RemoveBankAccount/{$userId}/siteAccountId
                 **********************************************************************************************/
                WSUtils.hitService(activity, Url.getYodleeRemoveBankAccountApi(modalAutomaticStatements.get(position).getItemAccountId()), templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {
                        System.out.print("RemoveAccount FINAL:" + data.data);
                        Log.e("btUnlinkAcc:",""+data.data);
                        onRemoveListener.onRemove();
                        Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Account " + data.getResponseMsg());
                        Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Account " + data.data);
                    }
                });
            }
        });

        viewHolder.btUnlinkBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayListPost templatePosts = new ArrayListPost();


                /**********************************************************************************************
                 * http://52.27.249.143/Kippin/Finance/KippinFinanceApi/RemoveAccount/{$userId}/itemAccountId
                 **********************************************************************************************/

                WSUtils.hitService(activity, Url.getYodleeRemoveAccountApi(modalAutomaticStatements.get(position).getSiteAccountId()), templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                    @Override
                    public void onResult(int requestCode, TemplateData data) {
                        System.out.print("" + data.data);
                        Log.e("btUnlinkBank:",""+data.data);

                        onRemoveListener.onRemove();
                        Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Bank Account " + data.getResponseMsg());
                        Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Bank Account " + data.data);

                    }
                });
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tvName, tvBank, tvAcc;
        ImageView ivImage;
        Button btUnlinkAcc, btUnlinkBank;
    }

}
