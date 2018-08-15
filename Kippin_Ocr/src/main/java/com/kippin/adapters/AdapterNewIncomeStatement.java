package com.kippin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kippin.activities.ActivityNewIncomeStatement;
import com.kippin.kippin.R;
import com.kippin.webclient.model.ModalRevenueExpense;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static com.kippin.activities.ActivityNewIncomeStatement.COST_OF_GOODS_SOLD;
import static com.kippin.activities.ActivityNewIncomeStatement.GENERAL_AND_ADMINISTRATIVE_EXPENSE;
import static com.kippin.activities.ActivityNewIncomeStatement.OTHERS_REVENUE;
import static com.kippin.activities.ActivityNewIncomeStatement.PAYROLL_EXPENSES;
import static com.kippin.activities.ActivityNewIncomeStatement.SALES_REVENUE;

public class AdapterNewIncomeStatement extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    ArrayList<ModalRevenueExpense> revenueExpenseArrayList = new ArrayList<>();
    private ArrayList<Integer> mSectionIndices = new ArrayList<>();
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    ModalRevenueExpense[] credits, debits;

    /*public AdapterNewIncomeStatement(Context context, ArrayList<ModelClassification> modelClassifications) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.modelClassifications = modelClassifications;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }*/

    public AdapterNewIncomeStatement(Context context, ArrayList<ModalRevenueExpense> revenueExpenseArrayList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.revenueExpenseArrayList = revenueExpenseArrayList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private ArrayList<Integer> getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int lastFirstChar = getSectionTag(revenueExpenseArrayList.get(1).getClassificationChartAccountNumber());
        sectionIndices.add(1);
        for (int i = 2; i < revenueExpenseArrayList.size(); i++) {
            int strAccountNumber = getSectionTag(revenueExpenseArrayList.get(i).getClassificationChartAccountNumber());
            if (strAccountNumber != lastFirstChar) {
                lastFirstChar = strAccountNumber;
                sectionIndices.add(i);
            }
        }

        ArrayList<Integer> sections = new ArrayList<>();
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections.add(sectionIndices.get(i));
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        try {
            Character[] letters = new Character[mSectionIndices.size()];
            Log.e("Lenght:",""+letters.length);
            Log.e("Size::",""+mSectionIndices.size());
            for (int i = 0; i < mSectionIndices.size(); i++) {
                letters[i] = revenueExpenseArrayList.get(mSectionIndices.get(i)).getClassificationName().charAt(0);


            }
            return letters;
        }
        catch (IndexOutOfBoundsException e){

        }
        return null;
    }

    @Override
    public int getCount() {
        return revenueExpenseArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return revenueExpenseArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (revenueExpenseArrayList.get(position).isSectionItem()) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_entry, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.list_item_entry_title);
            holder.entry_summary = (TextView) convertView.findViewById(R.id.list_item_entry_summary);
            //holder.llHeaderView = (LinearLayout) convertView.findViewById(R.id.llHeaderView);

            holder.title.setText(revenueExpenseArrayList.get(position).getClassificationName());
            holder.entry_summary.setText("" + revenueExpenseArrayList.get(position).getGrossTotal());
            //  holder.llHeaderView.setBackgroundColor(revenueExpenseArrayList.get(position).getColor());
        } else {
            HeaderViewHolder sectionViewTitle = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_sticky_networklist1, parent, false);
            sectionViewTitle.text = (TextView) convertView.findViewById(R.id.text1);
            sectionViewTitle.headerLayout = (LinearLayout) convertView.findViewById(R.id.headerLayout);
            sectionViewTitle.text.setText(revenueExpenseArrayList.get(position).getTitleOfSection());
            sectionViewTitle.headerLayout.setBackgroundColor(revenueExpenseArrayList.get(position).getColor());
//            sectionViewTitle.tvSectionTitle.setVisibility(View.GONE);
            return convertView;
        }

        /*if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_entry, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.entry_summary = (TextView) convertView.findViewById(R.id.list_item_entry_summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(revenueExpenseArrayList.get(position).getClassificationName());
        holder.entry_summary.setText(String.valueOf(revenueExpenseArrayList.get(position).getGrossTotal()));*/

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_sticky_networklist1, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            holder.tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
            holder.headerLayout = (LinearLayout) convertView.findViewById(R.id.headerLayout);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.tvSectionTitle.setVisibility(View.VISIBLE);
            holder.tvSectionTitle.setText("REVENUE");
        } else if (position == ActivityNewIncomeStatement.iPosOfExpense) {
            holder.tvSectionTitle.setVisibility(View.VISIBLE);
            holder.tvSectionTitle.setText("EXPENSE");
        } else {
            holder.tvSectionTitle.setVisibility(View.GONE);
        }

        int classificationChartAccountNumber = revenueExpenseArrayList.get(position).getClassificationChartAccountNumber();
        holder.text.setText(getSectionTitle(classificationChartAccountNumber, revenueExpenseArrayList.get(position).isSectionItem()));

        holder.headerLayout.setBackgroundColor(Color.parseColor("#1B79BF"));


        return convertView;
    }

    public String getSectionTitle(int iClassificationChartAccountNumber, boolean isSectionTitle) {

        if (iClassificationChartAccountNumber == 3) {
            return "Revenue";
        } else if (iClassificationChartAccountNumber == 4) {
            return "Expense";
        } else if (iClassificationChartAccountNumber > 40010000 &&
                iClassificationChartAccountNumber < 40019999) {
            //Sales Revenue
            //if (isSectionTitle) {
            return "SALES REVENUE";
            // } else {
            //     return "Revenue";
            //  }
        } else if (iClassificationChartAccountNumber > 44000000 &&
                iClassificationChartAccountNumber < 44009999) {
            //Other Revnue
            return "OTHERS REVENUE";
        } else if (iClassificationChartAccountNumber > 50000000 &&
                iClassificationChartAccountNumber < 50019999) {
            //COST OF GOODS SOLD
            //if (isSectionTitle) {
            return "COST OF GOODS SOLD";
            // } else {
            //     return "Expense";
            //  }
        } else if (iClassificationChartAccountNumber > 54000000 &&
                iClassificationChartAccountNumber < 54009999) {
            //Payroll Expenses
            return "PAYROLL EXPENSES";
        } else if (iClassificationChartAccountNumber > 60000000 &&
                iClassificationChartAccountNumber < 60009999) {
            //GENERAL AND ADMINISTRATIVE EXPENSE
            return "GENERAL AND ADMINISTRATIVE EXPENSE";
        } else if (iClassificationChartAccountNumber == 40000000) {
            //GENERAL AND ADMINISTRATIVE EXPENSE
            return "Revenue";
        } else if (iClassificationChartAccountNumber == 50000000) {
            //GENERAL AND ADMINISTRATIVE EXPENSE
            return "Expense";
        }
        return "N/A";
    }

    private int getSectionTag(int iClassificationChartAccountNumber) {
        if (iClassificationChartAccountNumber > 40010000 &&
                iClassificationChartAccountNumber < 40019999) {
            //Sales Revenue
            return SALES_REVENUE;
        } else if (iClassificationChartAccountNumber > 44000000 &&
                iClassificationChartAccountNumber < 44009999) {
            //Other Revnue
            return OTHERS_REVENUE;
        } else if (iClassificationChartAccountNumber > 50010000 &&
                iClassificationChartAccountNumber < 50019999) {
            //COST OF GOODS SOLD
            return COST_OF_GOODS_SOLD;
        } else if (iClassificationChartAccountNumber > 54000000 &&
                iClassificationChartAccountNumber < 54009999) {
            //Payroll Expenses
            return PAYROLL_EXPENSES;
        } else if (iClassificationChartAccountNumber > 60000000 &&
                iClassificationChartAccountNumber < 60009999) {
            //GENERAL AND ADMINISTRATIVE EXPENSE
            return GENERAL_AND_ADMINISTRATIVE_EXPENSE;
        }
        return 0;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        int iAccountNo = revenueExpenseArrayList.get(position).getClassificationChartAccountNumber();
        return ((ActivityNewIncomeStatement) mContext).getSectionTag(iAccountNo);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.size() == 0) {
            return 0;
        }

        if (section >= mSectionIndices.size()) {
            section = mSectionIndices.size() - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionLetters[section]/*mSectionIndices[section]*/;
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.size(); i++) {
            if (position < mSectionIndices.get(i)) {
                return i - 1;
            }
        }
        return mSectionIndices.size() - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        revenueExpenseArrayList.clear();
        mSectionIndices = new ArrayList<>();
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
        //UnderlineTextView tvType;
        TextView text, tvSectionTitle;
        LinearLayout headerLayout;
    }

    class ViewHolder {
        TextView title, entry_summary;
        LinearLayout llHeaderView;
    }

}
