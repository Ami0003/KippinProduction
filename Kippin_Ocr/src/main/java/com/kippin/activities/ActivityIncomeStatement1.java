//package com.kippin.activities;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//
//import com.kippin.adapters.AdapterIncomeStatement;
//import com.kippin.kippin.R;
//import com.kippin.superviews.SuperActivity;
//import com.kippin.utils.Utility;
//import com.kippin.utils.pinnedlistview.EntryItem;
//import com.kippin.utils.pinnedlistview.Item;
//import com.kippin.webclient.model.ArrayListPost;
//import com.kippin.webclient.model.ModalIncomeStatement;
//import com.kippin.webclient.model.ModalRevenueExpense;
//import com.library_for_stickylist.StickyListHeadersListView;
//
//import java.util.ArrayList;
//import java.util.StringTokenizer;
//
//public class ActivityIncomeStatement1 extends SuperActivity implements StickyListHeadersListView.OnHeaderClickListener,
//        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
//        StickyListHeadersListView.OnStickyHeaderChangedListener {
//
//    ModalIncomeStatement modalIncomeStatement =null;
//
//  //  @Bind(R.id.pinnedList)
//  StickyListHeadersListView pinnedSectionListView;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_income_statement);
//
//        generateActionBar(R.string.income_statement, true, true);
//        ArrayListPost templatePosts = new ArrayListPost();
//        initialiseUI();
//        setUpListeners();
//        modalIncomeStatement = getData(ModalIncomeStatement.class, returnName(ModalIncomeStatement.class));
//
//        ModalRevenueExpense[] credits= modalIncomeStatement.getObjRevenueList();
//        ModalRevenueExpense[] debits = modalIncomeStatement.getObjExpenseList();
//        ArrayList<Item> items = new ArrayList<Item>();
//
//        double gross_revenue = 0;
//        if(credits.length>0){
//            gross_revenue=  group(credits , "Revenue" , items , gross_revenue, true );
//         //   items.add(new SectionItem("Total Revenue:     "+(Utility.parseDouble(gross_revenue )).replace("-" , ""), Color.RED)) ;
//        }
//        else{
//          //  items.add(new SectionItem("Revenue (No record found) ",  Color.RED)) ;
//       //     items.add(new SectionItem("Total Revenue = 0.00",  Color.RED)) ;
//        }
//
//
//        double gross_expense = 0;
//        items.add(null);
//        if(debits.length>0){
//        gross_expense =     group(debits , "Expense" , items , gross_expense ,  false   );
//       // items.add(new SectionItem("Total Expense =     "+(Utility.parseDouble(gross_expense)).replace("-","")  ,  Color.RED)) ;
//        }else{
//        //    items.add(new SectionItem("Expense (No record found) ",  Color.RED)) ;
//         //   items.add(new SectionItem("Total Expense = 0.00",  Color.RED)) ;
//        }
//
//
//        items.add(null);
//
//
//        if(gross_expense <0 && gross_revenue<0){
//            gross_expense = - gross_expense;
//        }
//
//
//       // items.add(new SectionItem("Net Income =    "+(Utility.parseDouble(Utility.add(gross_revenue,gross_expense)) ).replace("-" ,"") ,  Color.RED)) ;
      // AdapterIncomeStatement adapterIncomeStatement = new AdapterIncomeStatement(this,pinnedSectionListView,items) ;
//        pinnedSectionListView.setAdapter(adapterIncomeStatement);
//    }
//    public void initialiseUI(){
//        try {
//            pinnedSectionListView = (StickyListHeadersListView) findViewById(R.id.pinnedList);
//            pinnedSectionListView.setOnHeaderClickListener(this);
//            pinnedSectionListView.setOnStickyHeaderChangedListener(this);
//            pinnedSectionListView.setOnStickyHeaderOffsetChangedListener(this);
//            pinnedSectionListView.setEmptyView(findViewById(R.id.empty));
//            pinnedSectionListView.setDrawingListUnderStickyHeader(true);
//            pinnedSectionListView.setAreHeadersSticky(true);
//        }catch(Exception ex){
//
//        }
//    }
//    private double group( ModalRevenueExpense[] debits , String title ,  ArrayList<Item> items , double gross , boolean negativeAllowed){
//
//        ArrayList<ModalRevenueExpense> a= new ArrayList<>() ;
//        ArrayList<ModalRevenueExpense> b= new ArrayList<>() ;
//        ArrayList<ModalRevenueExpense> c= new ArrayList<>() ;
//        ArrayList<ModalRevenueExpense> d= new ArrayList<>() ;
//        ArrayList<ModalRevenueExpense> e= new ArrayList<>() ;
//
//
//        for(int i= 0 ; i <debits.length; i++){
//
//            int classificationId = Utility.getClassification_id(debits[i].getClassificationChartAccountNumber()) ;
//
//            switch (classificationId){
//                case 1:
//                    a.add(debits[i]) ;
//                    break;
//                case 2:
//                    b.add(debits[i]) ;
//                    break;
//                case 3:
//                    c.add(debits[i]) ;
//                    break;
//                case 4:
//                    d.add(debits[i]) ;
//                    break;
//                case 5:
//                    e.add(debits[i]) ;
//                    break;
//            }
//        }
//
//      //  items.add(new SectionItem(title , Color.RED)) ;
//
//
//        double gross_a = processIncomeArray( a, items , 1 ,negativeAllowed) ;
//        double gross_b = processIncomeArray( b, items , 2 ,negativeAllowed) ;
//        double gross_c = processIncomeArray( c, items , 3 ,negativeAllowed) ;
//        double gross_d = processIncomeArray( d, items , 4 ,negativeAllowed) ;
//        double gross_e = processIncomeArray( e, items , 5 ,negativeAllowed) ;
//            gross = Utility.add(gross_a,gross_b,gross_c,gross_d,gross_e);
//        return gross;
//    }
//
//    private double processIncomeArray( ArrayList<ModalRevenueExpense>  modalRevenueExpenses  ,ArrayList<Item> items ,   int chartId, boolean negativeAllowed ){
//        if(modalRevenueExpenses.size()==0) return 0 ;
//
//        double gross = 0;
//
//        StringTokenizer stringTokenizer = new StringTokenizer(Utility.getClassificationHeader_H_T_ByCustomChartId(chartId)  , ":")  ;
//        String H = stringTokenizer.nextToken();
//        String T = stringTokenizer.nextToken();  // HEADER
//      // items.add(new EntryItem(modelDebitCredit.getClassificationName(),Utility.parseDouble(Double.parseDouble( gross_ )).replace("-","") , -1 , H ).setColor(Color.parseColor("#296FC0") ) ) ;
//
//        for(int i= 0 ; i <modalRevenueExpenses.size(); i++){
//            ModalRevenueExpense modelDebitCredit = modalRevenueExpenses.get(i);
//
//            String gross_ = modelDebitCredit.getGrossTotal()+"";
//
//            if(!negativeAllowed){
//                gross_ = gross_.replace("-" , "" );
//            }
//            if(i== modalRevenueExpenses.size()-1){
//
//                items.add(new EntryItem(modelDebitCredit.getClassificationName(),Utility.parseDouble(Double.parseDouble( gross_ )).replace("-","") , -1 , (T +"="+  Utility.parseDouble(gross).replace("-", "")).toString() ).setColor(Color.parseColor("#296FC0") ) ) ;
//            }else{
//                items.add(new EntryItem(modelDebitCredit.getClassificationName(),Utility.parseDouble(Double.parseDouble( gross_ )).replace("-","") , -1 , H ).setColor(Color.parseColor("#296FC0") ) ) ;
//            }
//
//
//            gross   = Utility.add(gross,modelDebitCredit.getGrossTotal())  ;
//        }
//
//
//      //  items.add(new SectionItem( T +"="+  Utility.parseDouble(gross).replace("-", ""), Color.parseColor("#296FC0") )) ;
//        return  gross;
//
//    }
//
//
//    @Override
//    public void setUpListeners() {
//
//    }
//
//    @Override
//    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
//
//    }
//
//    @Override
//    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
//
//    }
//
//    @Override
//    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
//
//    }
//}
