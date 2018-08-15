package com.kippin.connectedbankacc.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.connectedbankacc.CryptLib;
import com.kippin.kippin.R;
import com.kippin.selectdate.ActivitySelectDate;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.model.ModelBankAccount;
import com.kippin.webclient.model.ModelUpdateAccountName;
import com.kippin.webclient.model.TemplateData;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by gaganpreet.singh on 1/21/2016.
 */
public class BankStatementAdapter extends BaseAdapter {

    //    private final int bank_id;
    private ArrayList<ModelBankAccount> entries = new ArrayList<>();
    private Context context = null;
    private LayoutInflater layoutInflater;
    Activity activity;
    String s = "";


    //    BANKS
    private final int RBC = 1;
    private final int SCOTIA = 2;
    private final int BMO = 3;
    private final int TD_BANK = 4;
    private final int CIBC = 5;
    private final int CASH = 6;

//    BANK CLOSED

    public BankStatementAdapter(Context context, int bank_id, ArrayList<ModelBankAccount> modelBankAccounts) {
//        this.bank_id = bank_id;
        this.context = context;
        activity = (Activity) context;
        entries = modelBankAccounts;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position).getAccountNumber();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View view;
    ViewHolder viewHolder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int bank_id = entries.get(position).getBankId();

//        if(convertView==null) {
        view = layoutInflater.inflate(R.layout.include_bank_statement, parent, false);
        convertView = view;
        viewHolder = new ViewHolder();
        viewHolder.tvBankName = (TextView) convertView.findViewById(R.id.tvBankStatement);
        viewHolder.ivView = (ImageView) convertView.findViewById(R.id.ivViewIcon);
        viewHolder.ivEdit = (ImageView) convertView.findViewById(R.id.ivEditIcon);
        viewHolder.ivBank = (ImageView) convertView.findViewById(R.id.ivBankIcon);
        viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        convertView.setTag(viewHolder);

//        }else{
//            view =convertView;
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        if (entries.get(position).getBankLogo() != null) {
            Picasso.with(context)
                    .load(entries.get(position).getBankLogo().toString())
                    .into(viewHolder.ivBank);

        } else {
            Log.e("LOGO:",""+entries.get(position).getBankLogo());
            //  setBankIcon(viewHolder.ivBank , bank_id);
        }
        setUserName(viewHolder.tvUserName, position, entries.get(position).getBankId());

        try {
            Log.e("acount number", entries.get(position).getAccountNumber());
            s = CryptLib.decrypt(entries.get(position).getAccountNumber(), context.getResources().getString(R.string.key_of_cryptography),
                    context.getResources().getString(R.string.iv_intialzation_vector));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        viewHolder.tvBankName.setText(s);

        viewHolder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ActivitySelectDate.ACCOUNT_NAME, entries.get(position).getAccountName());
                bundle.putString(ActivitySelectDate.ACCOUNT_NUMBER, entries.get(position).getAccountNumber());
                bundle.putString(ActivitySelectDate.BANK_ID, entries.get(position).getBankId() + "");
//                bundle.putString(ActivitySelectDate.USER_ID, entries.get(position).getUserId());
                bundle.putString(ActivitySelectDate.USER_ID, Singleton.getUser().getId());
                Utility.startActivity(activity, ActivitySelectDate.class, bundle, true);

//                if(position==1){
                Utility.accNumber = entries.get(position).getAccountNumber();
//                }else Utility.accNumber = null;

                Utility.accName = viewHolder.tvUserName.getText().toString();
                Utility.bankId = bank_id + "";

            }
        });

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestChangeNameDialog(position);
            }
        });

        return convertView;


    }

    private void requestChangeNameDialog(final int position) {
//              /*  String userId , */String bankId, String accountNumber , String accountName

        final ModelBankAccount modelBankAccount = entries.get(position);

        int bank_id = modelBankAccount.getBankId();

        modelBankAccount.setUserId(Singleton.getUser().getId());

        modelBankAccount.setBankId(bank_id);
//        modelBankAccount.setAccountName(entries.get(position).getAccountName());

        new DialogBox(context, modelBankAccount, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                ModelUpdateAccountName modelUpdateAccountName = data.getData(ModelUpdateAccountName.class);

                if (modelUpdateAccountName.getResponseMessage().equalsIgnoreCase("Success")) {
//                    BankStatementAdapter.this.entries.get(position).setAccountName(modelUpdateAccountName.getAccountName());
//                    BankStatementAdapter.this.notifyDataSetChanged();

//                    viewHolder.tvUserName.setText(modelUpdateAccountName.getAccountName());
                    entries.get(position).setAccountName(modelUpdateAccountName.getAccountName());
                    notifyDataSetChanged();
                    new DialogBox(context, Kippin.res.getString(R.string.message_success_bank_statement), (DialogBoxListener) null);

                } else {
                    new DialogBox(context, modelUpdateAccountName.getResponseMessage(), (DialogBoxListener) null);
                }

            }
        });
    }

    private void setBankIcon(ImageView bank, int bank_id) {

        switch (bank_id) {
            case RBC:
                bank.setImageResource(R.drawable.bank_icon_rbc);
                break;

            case SCOTIA:
                bank.setImageResource(R.drawable.bank_icon_scotia);
                break;

            case TD_BANK:
                bank.setImageResource(R.drawable.bank_icon_td);
                break;

            case BMO:
                bank.setImageResource(R.drawable.bank_icon_bmo);
                break;

            case CIBC:
                bank.setImageResource(R.drawable.bank_icon_cibc);
                break;

            case CASH:
                bank.setImageResource(R.drawable.money);
                break;
        }
    }


    public void setUserName(TextView userName, int position, int bank_id) {

        bank_id = bank_id - 1;

        if (entries.get(position).getAccountName() == (null)
                || entries.get(position).getAccountName().equals("null")
                || entries.get(position).getAccountName().equals("")) {
//            String[] banks = Kippin.res.getStringArray(R.array.banks);
            userName.setText(Utility.banks.get(bank_id).getBankName());
        } else {
            userName.setText(entries.get(position).getAccountName());
        }

    }


    class ViewHolder {
        TextView tvBankName, tvUserName;
        ImageView ivView, ivEdit, ivBank;
    }

}
