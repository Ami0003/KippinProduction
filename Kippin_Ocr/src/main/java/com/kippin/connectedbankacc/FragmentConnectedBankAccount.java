package com.kippin.connectedbankacc;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kippin.addCashEntry.ActivityCashEntry;
import com.kippin.connectedbankacc.adapter.BankAdapter;
import com.kippin.connectedbankacc.adapter.BankStatementAdapter;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperFragment;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSHandler;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSTemplate;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.codes.ResponseCode;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalMobileDropdownListing;
import com.kippin.webclient.model.ModelBankAccount;
import com.kippin.webclient.model.ModelBankAccounts;
import com.kippin.webclient.model.TemplateData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class FragmentConnectedBankAccount extends SuperFragment {

  //  @Bind(R.id.lvBanks)
    ListView lvBanks;
 //   @Bind(R.id.spSelectBank)
    CustomSpinner spSelectBank;
    BankAdapter bankAdapter = null;
  //  @Bind(R.id.spSelectBankText)
    TextView spSelectBank1;

 //   @Bind(R.id.ivAddCashEntry)
    TextView ivAddCashEntry;

  //  @Bind(R.id.ivViewCashEntry)
    TextView ivViewCashEntry;

    //    String[] bankOptions  =null;
    ArrayList<ModalMobileDropdownListing> bankOptions= new ArrayList<>() ;



    BankStatementAdapter bankStatementAdapter;

    public int bank_id = -1;

    public  boolean isItemSelected = false;


    private boolean isCreditCard = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Utility.checkCrashTracker( getActivity());

        root = inflater.inflate(R.layout.fragment_connected_bank_account , container, false) ;
        lvBanks = (ListView)root.findViewById(R.id.lvBanks);
        spSelectBank = (CustomSpinner)root.findViewById(R.id.spSelectBank);
        spSelectBank1 = (TextView)root.findViewById(R.id.spSelectBankText);
        ivAddCashEntry = (TextView)root.findViewById(R.id.ivAddCashEntry);
        ivViewCashEntry = (TextView)root.findViewById(R.id.ivViewCashEntry);
        isCreditCard  = Singleton.isCreditCard;

        initialiseUI();

        spSelectBank.setOnItemSelectedListener(onItemSelectedListener);
        spSelectBank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpinnerClick();
            }
        });

        generateActionBar(R.string.account_list, false, false);

//        isCreditCard = getIntent().getExtras()!=null && getIntent().getExtras().getBoolean(Utility.IS_CREDIT_CARD) ;

        if(isCreditCard ) {
            ivAddCashEntry.setVisibility(View.GONE);
            ivViewCashEntry.setVisibility(View.GONE);
        }

        fetchBankList();

        ivAddCashEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCashEntry(v);
            }
        });
        ivViewCashEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewCashEntry(v);
            }
        });
        return root;
    }


    private void fetchBankList() {
        WSUtils.hitServiceGet(getActivity(), Url.getUrlGetMobileDrpDwnListing(isCreditCard ? 2 : 1), new ArrayListPost(), TemplateData.class, new WSInterface() {
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
                fetchAllBankList();

            }
        })  ;
    }


    private void fetchAllBankList() {

        WSUtils.hitServiceGet(getActivity(), Url.getUrlAllBankListing() , new ArrayListPost(),  ModelBankAccounts.class , new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                ModelBankAccounts modelBankAccounts = data.getData(ModelBankAccounts.class);
                if(modelBankAccounts.accounts!=null && modelBankAccounts.accounts.size()==1 && modelBankAccounts.accounts.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)){

                    new DialogBox(getActivity(), modelBankAccounts.accounts.get(0).getResponseMessage(), new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {
                        }
                    });
                    modelBankAccounts.accounts=new ArrayList<>();
                }
                bankStatementAdapter = new BankStatementAdapter(getActivity(),bank_id,modelBankAccounts.accounts);
                lvBanks.setAdapter(bankStatementAdapter);
//                isItemSelected = true;

            }
        });

    }




    boolean isSpinnerClickedEarlier= false;


   // @OnClick(R.id.ivViewCashEntry)
    public void onViewCashEntry(View view){

        int dummy = 0;
        int dummy1 = 0;
        int dummy2 = 0;
        if(spSelectBank.getCount()>0){

            spSelectBank.setSelection(bankOptions.size() - 1);
        }else{

            setBankAdapter();
            spSelectBank.setSelection(bankOptions.size() - 1);
        }

        spSelectBank1.setText("Please Select");
    }

    private void setBankAdapter(){
        bankAdapter = new BankAdapter(getActivity());
        spSelectBank.setAdapter(bankAdapter);
    }

    private void onSpinnerClick(){

        if(!isItemSelected){
           setBankAdapter();


            isItemSelected= true;
//            spSelectBank.performClick();

            Utility.showDialog(getActivity());

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


        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    spSelectBank.performClick();
                    Utility.dismissDialog();
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(bank_id==-1){
            Singleton.reloadStatements = false;
        }

        if(Singleton.reloadStatements){
            Singleton.reloadStatements=false;
            if(!isItemSelected) {
                setBankAdapter();
                isItemSelected=true;
            }

            if(bankOptions !=null) ;//bankOptions= getResources().getStringArray(R.array.banks);
            spSelectBank.setSelection(bankOptions.size()-1);
        }

    }

    Handler handler = new Handler() ;

    //@OnClick(R.id.ivAddCashEntry)
    public void addCashEntry(View v)
    {
        Utility.startActivity(getActivity(),ActivityCashEntry.class,true);
    }


    @Override
    public void initialiseUI() {

        super.initialiseUI(root);
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {


//            if(position==getActivity().getResources().getStringArray(R.array.banks).length-1
//
//                    ){
//                ivAddCashEntry.setVisibility(View.VISIBLE);
//            }else {
//                ivAddCashEntry.setVisibility(View.GONE);
//            }

            isItemSelected=true;

//            spSelectBank1.setText((String)spSelectBank.getSelectedItem());
//
//            bank_id  = position+1;


            ModalMobileDropdownListing modalMobileDropdownListing = (ModalMobileDropdownListing)spSelectBank.getSelectedItem() ;
            spSelectBank1.setText(modalMobileDropdownListing.getBankName());
            bank_id = Integer.parseInt(modalMobileDropdownListing.getId());


            WSTemplate wsTemplate = new WSTemplate();
            wsTemplate.aClass = ModelBankAccounts.class;
            wsTemplate.context = getActivity();
            wsTemplate.message_id=R.string.pls_wait;
            wsTemplate.methods= WSMethods.GET;
            wsTemplate.url= Url.getBankStatementListUrl(bank_id+""  );
            wsTemplate.requestCode = position+1;
            wsTemplate.wsInterface = new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {
                    ModelBankAccounts modelBankAccounts = data.getData(ModelBankAccounts.class);
                    if(modelBankAccounts.accounts!=null && modelBankAccounts.accounts.size()==1 && modelBankAccounts.accounts.get(0).getResponseCode().equalsIgnoreCase(ResponseCode.NO_RECORDS_FOUND)){

                        new DialogBox(getActivity(), modelBankAccounts.accounts.get(0).getResponseMessage(), new DialogBoxListener() {
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

                    bankStatementAdapter = new BankStatementAdapter(getActivity(),bank_id,modelBankAccounts.accounts);
                    lvBanks.setAdapter(bankStatementAdapter);

                }
            };
            new WSHandler(wsTemplate).execute();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    public void setUpListeners() {

//        spSelectBank1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                spSelectBank.performClick();
//            }
//        });

    }
}
