package com.kippin.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Utility;
import com.kippin.webclient.model.ModalIncomeStatement;
import com.kippin.webclient.model.ModalRevenueExpense;

import java.util.ArrayList;

public class ActivityIncomePieChart extends SuperActivity implements OnChartValueSelectedListener {
    public static final int[] SET_COLORS = {
            Color.rgb(251, 11, 38), Color.rgb(0, 117, 194)
    };

    ModalIncomeStatement modalIncomeStatement;
    Button btShowListView;
    PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_pie_chart);

        generateActionBar(R.string.income_statement, true, true);

        modalIncomeStatement = getData(ModalIncomeStatement.class, returnName(ModalIncomeStatement.class));

        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        btShowListView = (Button) findViewById(R.id.btShowListView);

        setUpListeners();

        initPieChart();
    }

    private void initPieChart() {
        ModalRevenueExpense[] revenueList= modalIncomeStatement.getObjRevenueList();
        ModalRevenueExpense[] expenseList = modalIncomeStatement.getObjExpenseList();

        double totalRevenue = 0;
        for (int iCount = 0; iCount < revenueList.length; iCount++) {
            totalRevenue += revenueList[iCount].getGrossTotal();
        }

        double totalExpense = 0;
        for (int iCount = 0; iCount < expenseList.length; iCount++) {
            totalExpense += expenseList[iCount].getGrossTotal();
        }

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry((float) totalExpense, 0));
        yvalues.add(new Entry((float) totalRevenue, 1));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Expense");
        xVals.add("Revenue");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(false);

        dataSet.setColors(SET_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setOnChartValueSelectedListener(this);

        //pieChart.animateXY(1400, 1400);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
        Toast.makeText(ActivityIncomePieChart.this, "AMOUNT: "+e.getVal(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void setUpListeners() {
        btShowListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(returnName(ModalIncomeStatement.class), modalIncomeStatement);
                Utility.startActivityFinishBundle(ActivityIncomePieChart.this, ActivityNewIncomeStatement.class, bundle, true);
            }
        });
    }
}
