package com.kippin.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kippin.adapters.AdapterIncomeStatement;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Utility;
import com.kippin.utils.pinnedlistview.EntryItem;
import com.kippin.utils.pinnedlistview.Item;
import com.kippin.utils.pinnedlistview.SectionItem;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalIncomeStatement;
import com.kippin.webclient.model.ModalRevenueExpense;
import com.library_for_stickylist.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ActivityIncomeStatement extends SuperActivity implements StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    ModalIncomeStatement modalIncomeStatement = null;

    //  @Bind(R.id.pinnedList)
    StickyListHeadersListView pinnedSectionListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_statement);

        generateActionBar(R.string.income_statement, true, true);
        ArrayListPost templatePosts = new ArrayListPost();
        initialiseUI();
        setUpListeners();
        modalIncomeStatement = getData(ModalIncomeStatement.class, returnName(ModalIncomeStatement.class));

        ModalRevenueExpense[] credits = modalIncomeStatement.getObjRevenueList();
        ModalRevenueExpense[] debits = modalIncomeStatement.getObjExpenseList();
        ArrayList<Item> items = new ArrayList<Item>();

        double gross_revenue = 0;
        if (credits.length > 0) {

            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "CTotal Revenue = " + (Utility.parseDouble(gross_revenue)).replace("-", ""), false).setColor(Color.parseColor("#D6392B")));
            for (int i = 0; i < credits.length; i++) {
                items.add(new SectionItem(credits[i].getClassificationName(), Color.RED));
            }


            for (int iCount = 0; iCount < credits.length; iCount++) {
                if (credits[iCount].getClassificationChartAccountNumber() > 40010000 &&
                        credits[iCount].getClassificationChartAccountNumber() < 40019999) {
                    //Sales Revenue
                } else if (credits[iCount].getClassificationChartAccountNumber() > 44000000 &&
                        credits[iCount].getClassificationChartAccountNumber() < 44009999) {
                    //Other Revnue
                }
            }

            gross_revenue = group(credits, "Revenue", items, gross_revenue, true);

            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "CTotal Revenue = " + (Utility.parseDouble(gross_revenue)).replace("-", ""), false).setColor(Color.parseColor("#D6392B")));
            // items.add(new SectionItem("Total Revenue:     "+(Utility.parseDouble(gross_revenue )).replace("-" , ""), Color.RED)) ;
            // for space
            items.add(new EntryItem("", "", -1, "CTotal", true).setColor(Color.parseColor("#296FC0")));
        } else {
            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "Revenue (No record found) ", false).setColor(Color.parseColor("#D6392B")));
            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "Total Revenue = 0.0", false).setColor(Color.parseColor("#D6392B")));
            //   items.add(new SectionItem("Revenue (No record found) ",  Color.RED)) ;
            //   items.add(new SectionItem("Total Revenue = 0.00",  Color.RED)) ;
            // for space
            items.add(new EntryItem("", "", -1, "Total", true).setColor(Color.parseColor("#296FC0")));
        }


        double gross_expense = 0;
        //  items.add(null);
        //items.add(new EntryItem(modelDebitCredit.getClassificationName(),Utility.parseDouble(Double.parseDouble( gross_ )).replace("-","") , -1,H,true ).setColor(Color.parseColor("#296FC0") ) ) ;
        if (debits.length > 0) {

            for (int iCount = 0; iCount < debits.length; iCount++) {
                if (debits[iCount].getClassificationChartAccountNumber() > 50010000 &&
                        debits[iCount].getClassificationChartAccountNumber() < 50019999) {
                    //COST OF GOODS SOLD
                } else if (debits[iCount].getClassificationChartAccountNumber() > 54000000 &&
                        debits[iCount].getClassificationChartAccountNumber() < 54009999) {
                    //Payroll Expenses
                } else if (debits[iCount].getClassificationChartAccountNumber() > 64000000 &&
                        debits[iCount].getClassificationChartAccountNumber() < 64009999) {
                    //GENERAL AND ADMINISTRATIVE EXPENSE
                }
            }

            gross_expense = group(debits, "Expense", items, gross_expense, false);
            //    items.add(new SectionItem("Total Expense =     "+(Utility.parseDouble(gross_expense)).replace("-","")  ,  Color.RED)) ;
            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "CTotal Expense = " + (Utility.parseDouble(gross_expense)).replace("-", ""), false).setColor(Color.parseColor("#D6392B")));
            // for space
            items.add(new EntryItem("", "", -1, "CTotal", true).setColor(Color.parseColor("#296FC0")));
        } else {
            //    items.add(new SectionItem("Expense (No record found) ",  Color.RED)) ;
            //     items.add(new SectionItem("Total Expense = 0.00",  Color.RED)) ;
            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "Expense (No record found) ", false).setColor(Color.parseColor("#D6392B")));
            items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, "Total Expense = 0.0", false).setColor(Color.parseColor("#D6392B")));
            // for space
            items.add(new EntryItem("", "", -1, "Total", true).setColor(Color.parseColor("#296FC0")));

        }


        //    items.add(null);
        //items.add(new EntryItem(modelDebitCredit.getClassificationName(),Utility.parseDouble(Double.parseDouble( gross_ )).replace("-","") , -1,H,true ).setColor(Color.parseColor("#296FC0") ) ) ;

        if (gross_revenue < 0) {
            gross_revenue = -gross_revenue;
        }


        if (gross_expense < 0) {
            gross_expense = -gross_expense;
        }

        items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, ("Net Income =    " + (Utility.parseDouble(gross_revenue - gross_expense)).replace("-", "")), false).setColor(Color.parseColor("#D6392B")));
        //items.add(new EntryItem("",Utility.parseDouble(0.0).replace("-","") , -1,("Net Income =    "+(Utility.parseDouble(gross_revenue+gross_expense) ).replace("-" ,"")),false ).setColor(Color.parseColor("#D6392B") ) ) ;
//
//        items.add(new SectionItem("Net Income =    "+(Utility.parseDouble(gross_revenue+gross_expense) ).replace("-" ,"") ,  Color.RED)) ;
        //   items.add(new SectionItem("Net Income =    "+( (Utility.sub(gross_revenue , gross_expense)) ) ,  Color.RED)) ;

        AdapterIncomeStatement adapterIncomeStatement = new AdapterIncomeStatement(this, pinnedSectionListView, items);
        pinnedSectionListView.setAdapter(adapterIncomeStatement);
    }

    public void initialiseUI() {
        pinnedSectionListView = (StickyListHeadersListView) findViewById(R.id.pinnedList);
        pinnedSectionListView.setOnHeaderClickListener(this);
        pinnedSectionListView.setOnStickyHeaderChangedListener(this);
        pinnedSectionListView.setOnStickyHeaderOffsetChangedListener(this);
        pinnedSectionListView.setEmptyView(findViewById(R.id.empty));
        pinnedSectionListView.setDrawingListUnderStickyHeader(true);
        //       pinnedSectionListView.onHeaderClickreHeadersSticky(true);
    }

    private double group(ModalRevenueExpense[] debits, String title, ArrayList<Item> items, double gross, boolean negativeAllowed) {

        ArrayList<ModalRevenueExpense> a = new ArrayList<>();
        ArrayList<ModalRevenueExpense> b = new ArrayList<>();
        ArrayList<ModalRevenueExpense> c = new ArrayList<>();
        ArrayList<ModalRevenueExpense> d = new ArrayList<>();
        ArrayList<ModalRevenueExpense> e = new ArrayList<>();


        for (int i = 0; i < debits.length; i++) {

            int classificationId = Utility.getClassification_id(debits[i].getClassificationChartAccountNumber());

            switch (classificationId) {
                case 1:
                    a.add(debits[i]);
                    break;
                case 2:
                    b.add(debits[i]);
                    break;
                case 3:
                    c.add(debits[i]);
                    break;
                case 4:
                    d.add(debits[i]);
                    break;
                case 5:
                    e.add(debits[i]);
                    break;
            }
        }

        // items.add(new SectionItem(title , Color.RED)) ;#D6392B
        items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, title, false).setColor(Color.parseColor("#D6392B")));

        double gross_a = processIncomeArray(a, items, 1, negativeAllowed);
        double gross_b = processIncomeArray(b, items, 2, negativeAllowed);
        double gross_c = processIncomeArray(c, items, 3, negativeAllowed);
        double gross_d = processIncomeArray(d, items, 4, negativeAllowed);
        double gross_e = processIncomeArray(e, items, 5, negativeAllowed);

        return Utility.add(gross_a, gross_b, gross_c, gross_d, gross_e);
    }

    private double processIncomeArray(ArrayList<ModalRevenueExpense> modalRevenueExpenses, ArrayList<Item> items, int chartId, boolean negativeAllowed) {
        if (modalRevenueExpenses.size() == 0) return 0;

        double gross = 0;

        StringTokenizer stringTokenizer = new StringTokenizer(Utility.getClassificationHeader_H_T_ByCustomChartId(chartId), ":");
        String H = stringTokenizer.nextToken();
        String T = stringTokenizer.nextToken();
        Log.e("H" + H, "T" + T);
        //  items.add(new SectionItem( H, Color.parseColor("#296FC0")  )) ;
        items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, H, false).setColor(Color.parseColor("#296FC0")));
        for (int i = 0; i < modalRevenueExpenses.size(); i++) {
            ModalRevenueExpense modelDebitCredit = modalRevenueExpenses.get(i);

            String gross_ = modelDebitCredit.getGrossTotal() + "";

            if (!negativeAllowed) {
                gross_ = gross_.replace("-", "");
            }

            items.add(new EntryItem(modelDebitCredit.getClassificationName(), Utility.parseDouble(Double.parseDouble(gross_)).replace("-", ""), -1, H, true).setColor(Color.parseColor("#296FC0")));


            gross = Utility.add(gross, modelDebitCredit.getGrossTotal());
        }
        items.add(new EntryItem("", Utility.parseDouble(0.0).replace("-", ""), -1, T + " = " + Utility.parseDouble(gross).replace("-", ""), false).setColor(Color.parseColor("#296FC0")));

        //  items.add(new SectionItem( T +"="+  Utility.parseDouble(gross).replace("-", ""), Color.parseColor("#296FC0") )) ;
        return gross;

    }


    @Override
    public void setUpListeners() {

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }
}
