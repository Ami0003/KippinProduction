package com.kippin.classification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.automaticStatement.AdapterConnectedAccounts;
import com.automaticStatement.OnRemoveListener;
import com.kippin.kippin.R;
import com.kippin.utils.Url;
import com.kippin.views.UnderlineTextView;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.automatic.Common;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AdapterAutomatic extends BaseAdapter implements
        StickyListHeadersAdapter,SectionIndexer {

    private final Context mContext;
    Activity activity;
    // private String[] mCountries;
    ArrayList<Common> modelClassifications;

    ArrayList<Common> arraylist;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    OnRemoveListener onRemoveListener;

    public AdapterAutomatic(Activity activity, ArrayList<Common> modelClassifications,OnRemoveListener onRemoveListener) {
        mContext = activity;
        this.activity=activity;
        mInflater = LayoutInflater.from(activity);
        this.modelClassifications = modelClassifications;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(modelClassifications);
        this.onRemoveListener = onRemoveListener;
        if (modelClassifications.size() != 0) {
            mSectionIndices = getSectionIndices();
            mSectionLetters = getSectionLetters();
        }

    }

    public AdapterAutomatic(Activity activity, ArrayList<Common> modelClassifications) {
        mContext = activity;
        this.activity=activity;
        mInflater = LayoutInflater.from(activity);
        this.modelClassifications = modelClassifications;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(modelClassifications);
        //this.onRemoveListener = onRemoveListener;
        if (modelClassifications.size() != 0) {
            mSectionIndices = getSectionIndices();
            mSectionLetters = getSectionLetters();
        }

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int lastFirstChar = modelClassifications.get(0).getBankId();//charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < modelClassifications.size(); i++) {

            int iCategoryID = modelClassifications.get(0).getBankId();
            if (iCategoryID != lastFirstChar) {
                lastFirstChar = modelClassifications.get(0).getBankId();
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i]=String.valueOf(modelClassifications.get(mSectionIndices[i]).getBankId()).charAt(0);
            // letters[i] = ""+modelClassifications.get(mSectionIndices[i]).getBankId().charAt(0);
        }
        return letters;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.subitem_connected_acc, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvBank = (TextView) convertView.findViewById(R.id.tvBankName);
            holder.tvAcc = (TextView) convertView.findViewById(R.id.tvAccNum);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivDelete);
            holder.btUnlinkAcc = (Button) convertView.findViewById(R.id.btUnlinkAcc);
            holder.btUnlinkBank = (Button) convertView.findViewById(R.id.btUnlinkBank);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(""+modelClassifications.get(position).getAccountName());
        holder.tvBank.setText(""+modelClassifications.get(position).getBankName());
        holder.tvAcc.setText(""+modelClassifications.get(position).getAccountNumber());

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.WHITE);
            holder.ivImage.setBackgroundResource(R.drawable.delete_unselected);
        } else {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            holder.ivImage.setBackgroundResource(R.drawable.delete_selected);
        }

        holder.btUnlinkAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Box_unlinkAccount(activity,"Are you sureyou want to unlink this account?",position);
            }
        });

        holder.btUnlinkBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Box_unlinkbank(activity,"Are you sure you want to unlink this bank?",position);

            }
        });

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.tvType = (UnderlineTextView) convertView.findViewById(R.id.tvType);
            holder.tv_Type= (UnderlineTextView) convertView.findViewById(R.id.tvType);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        //CharSequence headerChar = mCountries[position].subSequence(0, 1);
        holder.tvType.setVisibility(View.GONE);
        holder.tv_Type.setVisibility(View.VISIBLE);
        int catId = modelClassifications.get(position).getBankId();
        Log.e("catId:",""+catId);
        holder.tv_Type.setText(getSectionTitle(catId));

        return convertView;
    }

    private String getSectionTitle(int iCategoryId) {
        switch (iCategoryId) {
            case 1:
                return "BANKS";

            case 2:
                return "CREDITS";


        }
        return "Others";
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        Log.e("getHeaderId:",""+modelClassifications.get(position).getBankId());
        return modelClassifications.get(position).getBankId();
        //[position].subSequence(0, 1).charAt(0);
    }



    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionLetters[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        modelClassifications.clear();
        mSectionIndices = new int[0];
        mSectionLetters = new Character[0];
        notifyDataSetChanged();
    }

    /*public void restore() {
        mCountries = mContext.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }*/



    class HeaderViewHolder {
        UnderlineTextView tvType,tv_Type;
    }

    class ViewHolder {
        TextView tvName, tvBank, tvAcc;
        ImageView ivImage;
        Button btUnlinkAcc, btUnlinkBank;
    }

    public void Dialog_Box_unlinkbank(final Context context, final String mMessager,final int pos) {
        try {
            //. mActivity = new Activity();
            final Dialog lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessager);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            textViewNo.setText("NO");
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewYes.setText("YES");
//            textViewNo.setText("Cancel");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    //onClickListener.onNegativeClick();
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    ArrayListPost templatePosts = new ArrayListPost();


                    /**********************************************************************************************
                     * http://52.27.249.143/Kippin/Finance/KippinFinanceApi/RemoveAccount/{$userId}/SiteAccountId
                     **********************************************************************************************/

                    WSUtils.hitService(activity, Url.getYodleeRemoveAccountApi(""+modelClassifications.get(pos).getSiteAccountId()), templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
                        @Override
                        public void onResult(int requestCode, TemplateData data) {
                            System.out.print("" + data.data);
                            Log.e("btUnlinkBank:",""+data.data);

                            onRemoveListener.onRemove();
                            Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Bank Account " + data.getResponseMsg());
                            Log.e(AdapterConnectedAccounts.class.getSimpleName(), "Unlink Bank Account " + data.data);

                        }
                    });
                    // onClickListener.onPositiveClick();
                }
            });

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    public void Dialog_Box_unlinkAccount(final Context context, final String mMessager,final int pos) {
        try {
            //. mActivity = new Activity();
            final Dialog lDialog = new Dialog(context);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_box_yes_no);
            TextView textViewMessage = (TextView) lDialog.findViewById(R.id.text_msg);
            textViewMessage.setText(mMessager);
            TextView textViewNo = (TextView) lDialog.findViewById(R.id.no_btn);
            textViewNo.setText("NO");
            TextView textViewYes = (TextView) lDialog.findViewById(R.id.yes_btn);
            textViewYes.setText("YES");
//            textViewNo.setText("Cancel");


            textViewNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    //onClickListener.onNegativeClick();
                }
            });

            textViewYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                    ArrayListPost templatePosts = new ArrayListPost();


                    /**********************************************************************************************
                     * http://52.27.249.143/Kippin/Finance/KippinFinanceApi/RemoveBankAccount/{$userId}/ItemAccountId
                     **********************************************************************************************/
                    WSUtils.hitService(activity, Url.getYodleeRemoveBankAccountApi(""+modelClassifications.get(pos).getItemAccountId()), templatePosts, WSMethods.POST, TemplateData.class, new WSInterface() {
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

            lDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

}
