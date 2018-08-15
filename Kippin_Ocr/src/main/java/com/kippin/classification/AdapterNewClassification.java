package com.kippin.classification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kippin.kippin.R;
import com.kippin.views.UnderlineTextView;
import com.kippin.webclient.model.ModelClassification;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AdapterNewClassification extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    // private String[] mCountries;
    ArrayList<ModelClassification> modelClassifications;

    ArrayList<ModelClassification> arraylist;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;

    public AdapterNewClassification(Context context, ArrayList<ModelClassification> modelClassifications) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.modelClassifications = modelClassifications;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(modelClassifications);
        if (modelClassifications.size() != 0) {
            mSectionIndices = getSectionIndices();
            mSectionLetters = getSectionLetters();
        }

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int lastFirstChar = Integer.parseInt(modelClassifications.get(0).getCategoryId());//charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < modelClassifications.size(); i++) {

            int iCategoryID = Integer.parseInt(modelClassifications.get(i).getCategoryId());
            if (iCategoryID != lastFirstChar) {
                lastFirstChar = Integer.parseInt(modelClassifications.get(i).getCategoryId());
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
            letters[i] = modelClassifications.get(mSectionIndices[i]).getCategoryId().charAt(0);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item_classification1, parent, false);
            holder.tvClassification = (TextView) convertView.findViewById(R.id.tvClassification);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvClassification.setText(modelClassifications.get(position).getClassificationType());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.tvType = (UnderlineTextView) convertView.findViewById(R.id.tvType);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        //CharSequence headerChar = mCountries[position].subSequence(0, 1);
        int catId = Integer.parseInt(modelClassifications.get(position).getCategoryId());
        holder.tvType.setText(getSectionTitle(catId));

        return convertView;
    }

    private String getSectionTitle(int iCategoryId) {
        switch (iCategoryId) {
            case 1:
                return "Assets";

            case 2:
                return "Expense";

            case 3:
                return "Revenue";

            case 4:
                return "Liability";

            case 5:
                return "Equity";
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
        return Integer.parseInt(modelClassifications.get(position).getCategoryId());
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
        return mSectionLetters[section]/*mSectionIndices[section]*/;
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        modelClassifications.clear();
        if (charText.length() == 0) {
            modelClassifications.addAll(arraylist);
        } else {
            if (arraylist.size() != 0) {
                for (ModelClassification wp : arraylist) {
                    if (wp.getClassificationType().toLowerCase().contains(charText)) {
                        modelClassifications.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        UnderlineTextView tvType;
    }

    class ViewHolder {
        TextView tvClassification;
    }
}
