package com.kippin.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kippin.adapters.AdapterNewIncomeStatement;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Utility;
import com.kippin.webclient.model.ModalIncomeStatement;
import com.kippin.webclient.model.ModalRevenueExpense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActivityNewIncomeStatement extends SuperActivity {

    ModalIncomeStatement modalIncomeStatement = null;
    StickyListHeadersListView lvGeneralStatement;

    //    ModalRevenueExpense[] credits;
//    ModalRevenueExpense[] debits;
    List<ModalRevenueExpense> creditsList;
    List<ModalRevenueExpense> debitList;
    ArrayList<ModalRevenueExpense> revenueExpenseArrayList;
    Button btShowChartView;

    AdapterNewIncomeStatement mAdapterNewIncomeStmt;

    public static final int SALES_REVENUE = 4001;
    public static final int OTHERS_REVENUE = 4400;
    public static final int COST_OF_GOODS_SOLD = 5001;
    public static final int PAYROLL_EXPENSES = 5400;
    public static final int GENERAL_AND_ADMINISTRATIVE_EXPENSE = 6400;

    String strSectionTitle = "";
    float iTotalExpense = 0, iTotalRevenue = 0;
    public static int iPosOfExpense = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income_statement);

        generateActionBar(R.string.income_statement, true, true);
        lvGeneralStatement = (StickyListHeadersListView) findViewById(R.id.lvGeneralStatement);
        btShowChartView = (Button) findViewById(R.id.btShowChartView);
        setUpListeners();
        modalIncomeStatement = getData(ModalIncomeStatement.class, returnName(ModalIncomeStatement.class));

        setUpListeners();

        creditsList = Arrays.asList(modalIncomeStatement.getObjRevenueList());
        debitList = Arrays.asList(modalIncomeStatement.getObjExpenseList());

        createSectionList();
    }

    private void createSectionList() {

        sortArrayElements(debitList);
        sortArrayElements(creditsList);

        int iChartAccountNo = 0;

        revenueExpenseArrayList = new ArrayList<>();
        revenueExpenseArrayList.clear();

        iTotalExpense = 0;
        iTotalRevenue = 0;
        strSectionTitle = "";

        int lastFirstChar = getSectionTag(creditsList.get(0).getClassificationChartAccountNumber());
        float iTotalScores = 0;
        for (int iCount = 0; iCount < creditsList.size(); iCount++) {

            int strAccountNumber = getSectionTag(creditsList.get(iCount).getClassificationChartAccountNumber());
            if (strAccountNumber != lastFirstChar) {
                revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, getSectionTitle(lastFirstChar) + iTotalScores, Color.parseColor("#D6392B")));
                iTotalScores = 0;
                lastFirstChar = strAccountNumber;
            }

            iTotalScores += creditsList.get(iCount).getGrossTotal();
            iTotalRevenue += creditsList.get(iCount).getGrossTotal();

            ModalRevenueExpense modalRevenueExpense = new ModalRevenueExpense();
            modalRevenueExpense.setClassificationChartAccountNumber(creditsList.get(iCount).getClassificationChartAccountNumber());
            modalRevenueExpense.setClassificationName(creditsList.get(iCount).getClassificationName());
            modalRevenueExpense.setClassificationType(creditsList.get(iCount).getClassificationType());
            modalRevenueExpense.setGrossTotal(creditsList.get(iCount).getGrossTotal());
            modalRevenueExpense.setTitleOfSection(null);
            modalRevenueExpense.setSectionItem(true);
            modalRevenueExpense.setColor(Color.parseColor("#ffffff"));

            iChartAccountNo = creditsList.get(iCount).getClassificationChartAccountNumber();

            revenueExpenseArrayList.add(modalRevenueExpense);
        }

        revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, getSectionTitle(lastFirstChar)  + iTotalScores, Color.parseColor("#D6392B")));
        revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, "TOTAL REVENUE = " + iTotalRevenue, Color.parseColor("#D6392B")));
        iPosOfExpense = revenueExpenseArrayList.size();

        lastFirstChar = getSectionTag(debitList.get(0).getClassificationChartAccountNumber());
        iTotalScores = 0;
        for (int iCount = 0; iCount < debitList.size(); iCount++) {

            int strAccountNumber = getSectionTag(debitList.get(iCount).getClassificationChartAccountNumber());
            if (strAccountNumber != lastFirstChar) {
                revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "" , null, 0, false, getSectionTitle(lastFirstChar)  + iTotalScores, Color.parseColor("#D6392B")));
                iTotalScores = 0;
                lastFirstChar = strAccountNumber;
            }

            iTotalScores += debitList.get(iCount).getGrossTotal();
            iTotalExpense += debitList.get(iCount).getGrossTotal();

            ModalRevenueExpense modalRevenueExpense = new ModalRevenueExpense();
            modalRevenueExpense.setClassificationChartAccountNumber(debitList.get(iCount).getClassificationChartAccountNumber());
            modalRevenueExpense.setClassificationName(debitList.get(iCount).getClassificationName());
            modalRevenueExpense.setClassificationType(debitList.get(iCount).getClassificationType());
            modalRevenueExpense.setGrossTotal(debitList.get(iCount).getGrossTotal());
            modalRevenueExpense.setTitleOfSection(null);
            modalRevenueExpense.setSectionItem(true);
            modalRevenueExpense.setColor(Color.parseColor("#ffffff"));

            iChartAccountNo = debitList.get(iCount).getClassificationChartAccountNumber();

            revenueExpenseArrayList.add(modalRevenueExpense);
        }

        revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, getSectionTitle(lastFirstChar) + iTotalScores, Color.parseColor("#D6392B")));
        revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, "TOTAL EXPENSE = " + iTotalExpense, Color.parseColor("#D6392B")));
        revenueExpenseArrayList.add(new ModalRevenueExpense(iChartAccountNo, "", null, 0, false, "NET INCOME = " + modalIncomeStatement.getNetIncome1(), Color.parseColor("#D6392B")));

        mAdapterNewIncomeStmt = new AdapterNewIncomeStatement(ActivityNewIncomeStatement.this, revenueExpenseArrayList);
        lvGeneralStatement.setAdapter(mAdapterNewIncomeStmt);
    }

    private void sortArrayElements(List<ModalRevenueExpense> mUnsortedList) {
        Collections.sort(mUnsortedList, new Comparator<ModalRevenueExpense>() {
            @Override
            public int compare(ModalRevenueExpense lhs, ModalRevenueExpense rhs) {

                return Integer.valueOf(rhs.getClassificationChartAccountNumber()).compareTo(lhs.getClassificationChartAccountNumber());
            }
        });
    }

    @Override
    public void setUpListeners() {
        btShowChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(returnName(ModalIncomeStatement.class), modalIncomeStatement);
                Utility.startActivityFinishBundle(ActivityNewIncomeStatement.this, ActivityIncomePieChart.class, bundle, true);
            }
        });
    }

    public int getSectionTag(int iClassificationChartAccountNumber) {
        if (iClassificationChartAccountNumber > 40010000 &&
                iClassificationChartAccountNumber < 40019999) {
            strSectionTitle = "SALES REVENUE = ";
            return SALES_REVENUE;
        } else if (iClassificationChartAccountNumber > 44000000 &&
                iClassificationChartAccountNumber < 44009999) {
            strSectionTitle = "OTHER REVENUE = ";
            return OTHERS_REVENUE;
        } else if (iClassificationChartAccountNumber > 50010000 &&
                iClassificationChartAccountNumber < 50019999) {
            strSectionTitle = "COST OF GOODS SOLD = ";
            return COST_OF_GOODS_SOLD;
        } else if (iClassificationChartAccountNumber > 54000000 &&
                iClassificationChartAccountNumber < 54009999) {
            strSectionTitle = "PAYROLL EXPENSES = ";
            return PAYROLL_EXPENSES;
        } else if (iClassificationChartAccountNumber > 60000000 &&
                iClassificationChartAccountNumber < 60009999) {
            strSectionTitle = "TOTAL GEN. & ADMIN. EXPENSE = ";
            return GENERAL_AND_ADMINISTRATIVE_EXPENSE;
        }
        return 0;
    }

    public String getSectionTitle(int icctAccountNumber) {

        switch (icctAccountNumber) {
            case SALES_REVENUE:
                return "TOTAL SALES REVENUE = ";

            case OTHERS_REVENUE:
                return "TOTAL OTHERS REVENUE = ";

            case COST_OF_GOODS_SOLD:
                return "TOTAL COST OF GOODS SOLD = ";

            case PAYROLL_EXPENSES:
                return "TOTAL PAYROLL EXPENSES = ";

            case GENERAL_AND_ADMINISTRATIVE_EXPENSE:
                return "TOTAL GEN. & ADMIN. EXPENSE = ";
        }
        return "N/A";
    }
}
