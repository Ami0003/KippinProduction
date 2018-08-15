package com.kippin.connectedbankacc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kippin.addCashEntry.ActivityCashEntry;
import com.kippin.connectedbankacc.adapter.BankAdapter;
import com.kippin.connectedbankacc.adapter.BankStatementAdapter;
import com.kippin.kippin.R;
import com.kippin.selectmonthyear.TemplateMonth;
import com.kippin.selectmonthyear.TemplateMonths;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.codes.ResponseCode;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalMobileDropdownListing;
import com.kippin.webclient.model.ModelBankAccounts;
import com.kippin.webclient.model.TemplateData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class ConnectedBankAccount extends SuperActivity {

  //  @Bind(R.id.lvBanks)
    ListView lvBanks;
   // @Bind(R.id.spSelectBank)
    CustomSpinner spSelectBank;
    BankAdapter bankAdapter = null;
  //  @Bind(R.id.spSelectBankText)
    TextView spSelectBank1;

   // @Bind(R.id.ivAddCashEntry)
    TextView ivAddCashEntry;

  //  @Bind(R.id.ivViewCashEntry)
    TextView ivViewCashEntry;

//    String[] bankOptions  =null;
    ArrayList<ModalMobileDropdownListing> bankOptions= new ArrayList<>() ;

    BankStatementAdapter bankStatementAdapter;
    public int bank_id = -1;
    public  boolean isItemSelected = false;
    private boolean isCreditCard = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkCrashTracker(ConnectedBankAccount.this);
//        isCreditCard = getIntent().getExtras()!=null && getIntent().getExtras().getBoolean(Utility.IS_CREDIT_CARD) ;

        isCreditCard = Singleton.isCreditCard  ;


        setContentView(R.layout.activity_connected_bank_account);
        initialiseUI();
        setUpListeners();
        spSelectBank.setOnItemSelectedListener(onItemSelectedListener);
        spSelectBank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpinnerClick();
            }
        });


        if(isCreditCard ) {
            ivAddCashEntry.setVisibility(View.GONE);
            ivViewCashEntry.setVisibility(View.GONE);
        }


        generateActionBar(R.string.account_list, true, true);
        fetchBankList();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        lvBanks =(ListView)findViewById(R.id.lvBanks);
        spSelectBank =(CustomSpinner)findViewById(R.id.spSelectBank);
        spSelectBank1 =(TextView)findViewById(R.id.spSelectBankText);
        ivAddCashEntry =(TextView)findViewById(R.id.ivAddCashEntry);
        ivViewCashEntry =(TextView)findViewById(R.id.ivViewCashEntry);
    }
    private void fetchBankList() {
        WSUtils.hitServiceGet(ConnectedBankAccount.this, Url.getUrlGetMobileDrpDwnListing(isCreditCard ? 2 : 1), new ArrayListPost(), TemplateData.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                Type listType = new TypeToken<List<ModalMobileDropdownListing>>() {
                }.getType();
                bankOptions = new Gson().fromJson(data.data, listType);

                ModalMobileDropdownListing modalMobileDropdownListing = new ModalMobileDropdownListing();
                modalMobileDropdownListing.setId(7 + "");
                modalMobileDropdownListing.setBankName("Cash");
                bankOptions.add(modalMobileDropdownListing);
                Utility.banks = bankOptions;


                fetchAllBanks();

            }
        })  ;
    }

    private void fetchAllBanks() {
        WSUtils.hitServiceGet(this, Url.getUrlAllBankListing(), new ArrayListPost(), ModelBankAccounts.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                ModelBankAccounts modelBankAccounts = data.getData(ModelBankAccounts.class);
                if (modelBankAccounts.accounts != null && modelBankAccounts.accounts.size() == 1 && modelBankAccounts.accounts.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)) {

                    new DialogBox(ConnectedBankAccount.this, modelBankAccounts.accounts.get(0).getResponseMessage(), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                        }
                    });
                    modelBankAccounts.accounts = new ArrayList<>();
                }
                bankStatementAdapter = new BankStatementAdapter(ConnectedBankAccount.this, bank_id, modelBankAccounts.accounts);
                lvBanks.setAdapter(bankStatementAdapter);
            }
        });
    }
    @Override
    public void setUpListeners() {
        ivViewCashEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spSelectBank.getCount()>0){
                    spSelectBank.setSelection(bankOptions.size() - 1);
                }else{
                    setBankAdapter();
                    spSelectBank.setSelection(bankOptions.size() - 1);
                }

                spSelectBank1.setText("Please Select");
            }
        });

        ivAddCashEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.startActivity(ConnectedBankAccount.this, ActivityCashEntry.class, true);
            }
        });
    }

    boolean isSpinnerClickedEarlier= false;


   /* //@OnClick(R.id.ivViewCashEntry)
    public void onClick(View view){

        if(spSelectBank.getCount()>0){
            spSelectBank.setSelection(bankOptions.size() - 1);
        }else{
            setBankAdapter();
            spSelectBank.setSelection(bankOptions.size() - 1);
        }

        spSelectBank1.setText("Please Select");
    }*/

    private void setBankAdapter(){
        bankAdapter = new BankAdapter(this);
        spSelectBank.setAdapter(bankAdapter);
    }

    private void onSpinnerClick(){

        if(!isItemSelected){
           setBankAdapter();
            isItemSelected= true;
//            spSelectBank.performClick();

            Utility.showDialog(ConnectedBankAccount.this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            spSelectBank.performClick();
                            Utility.dismissDialog();
                        }
                    });
                }
            }).start();


        }else
        spSelectBank.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Singleton.reloadStatements){
            Singleton.reloadStatements=false;
            if(!isItemSelected) {
                setBankAdapter();
                isItemSelected=true;
            }

//            if(bankOptions !=null) ;//bankOptions= getResources().getStringArray(R.array.banks);
//            spSelectBank.setSelection(bankOptions.size()-1);
        }

    }

    Handler handler = new Handler() ;

  /* // @OnClick(R.id.ivAddCashEntry)
    public void addCashEntry(View v)
    {
        Utility.startActivity(this, ActivityCashEntry.class, true);
    }
*/



    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
            isItemSelected=true;
            ModalMobileDropdownListing modalMobileDropdownListing = (ModalMobileDropdownListing)spSelectBank.getSelectedItem() ;
            spSelectBank1.setText(modalMobileDropdownListing.getBankName());
            bank_id = Integer.parseInt(modalMobileDropdownListing.getId());
            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelBankAccounts.class;
            wsTemplate.context = ConnectedBankAccount.this;
            wsTemplate.message_id=R.string.pls_wait;
            wsTemplate.methods= WSMethods.GET;
            wsTemplate.url= Url.getBankStatementListUrl(bank_id+"");
            wsTemplate.requestCode = position+1;
            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    ModelBankAccounts modelBankAccounts = data.getData(ModelBankAccounts.class);
                    if(modelBankAccounts.accounts!=null && modelBankAccounts.accounts.size()==1 && modelBankAccounts.accounts.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)){

                        new DialogBox(ConnectedBankAccount.this, modelBankAccounts.accounts.get(0).getResponseMessage(), new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                            }
                        });
                        modelBankAccounts.accounts=new ArrayList<>();
                    }
                    if(position==5){
                        spSelectBank1.setText("Please Select");
                        isItemSelected = false;
                    }

                    for(int i = 0 ; i < modelBankAccounts.accounts.size() ;i++){
//                        bank_id
                        modelBankAccounts.accounts.get(i).setBankId(bank_id);
                    }

                    bankStatementAdapter = new BankStatementAdapter(ConnectedBankAccount.this,bank_id,modelBankAccounts.accounts);
                    lvBanks.setAdapter(bankStatementAdapter);

                }
            };
            new WSHandler(wsTemplate).execute();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


}
