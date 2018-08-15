package com.automaticStatement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kippin.classification.AdapterAutomatic;
import com.kippin.kippin.R;

import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;

import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.WS_Interface;
import com.kippin.webclient.model.ModalAutomaticStatement;
import com.kippin.webclient.model.ModalAutomaticStatements;
import com.kippin.webclient.model.automatic.AccountList;
import com.kippin.webclient.model.automatic.AccountList_;
import com.kippin.webclient.model.automatic.Common;
import com.kippin.webclient.model.automatic.ModalAutomaticStatementAccountList;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActivityAutomaticStatement extends SuperActivity implements View.OnClickListener {

    //  @Bind(R.id.lvConnectedAccounts)
   // ListView lvList;
    StickyListHeadersListView stickyListHeadersListView;

    // @Bind(R.id.btnAdd)
    ImageView btnAdd;
    ArrayList<Common> commonArrayList=new ArrayList<>();
    ArrayList<ModalAutomaticStatement> modalAutomaticStatements;
    //ModalAutomaticStatementAccountList modalAutomaticStatementAccountList;
    AdapterConnectedAccounts adapterConnectedAccounts;
    List<AccountList> accountList = new ArrayList<>();
    List<AccountList_> accountList_=new ArrayList<>();
    private final int REQUEST_CODE_SELECT_BANK = 1;
    private final int REQUEST_CODE_IS_RELOAD_NEEDED = 2;

    String bankId = "7";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_automatic_statement);
        generateActionBar(R.string.connected_bank_account1, true, true);
        initialiseUI();
        setUpListeners();
        setUpUI();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
      stickyListHeadersListView = (StickyListHeadersListView) findViewById(R.id.stickyConnected);
        btnAdd = (ImageView) findViewById(R.id.btnAdd);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpUI() {

   /*   int result = Integer.parseInt(Singleton.getUser().getId());
        System.out.println(result);

        LoadingBox.isDialogShowing();

          //  LoadingBox.showLoadingDialog(activity, "Wait ...");
            RestClient.getApiFinanceServiceForPojo().getAccountList(result, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonElement, Response response) {
                            LoadingBox.dismissLoadingDialog();
                           // LoadingBox.dismissLoadingDialog();
                            Log.e("Rest-Client", jsonElement.toString() + " \n " + response.getUrl());
                           // Gson gson = new Gson();
                          //  EditRecievedInvoice editRecievedInvoice = gson.fromJson(jsonElement.toString(), new TypeToken<EditRecievedInvoice>() {
                        //    }.getType());


                        }

                        @Override
                        public void failure(RetrofitError error) {
                            LoadingBox.dismissLoadingDialog();
                            Log.e("Rest-------Client", error.getUrl() + "");
                        }
                    }

            );*/
        Log.e("hitServiceGetAuto","hitServiceGetAuto");

        WSUtils.hitServiceGetAuto(this, Url.URL_GET_CONNECTED_ACC + Singleton.getUser().getId(), ModalAutomaticStatements.class, new WS_Interface() {


            @Override
            public void on_Result(int requestCode, ModalAutomaticStatementAccountList modalAutomaticStatementAccount_List) {
                Log.e("requestCode:","requestCode:"+requestCode);
                Log.e("modalAutomaticStatementAccount_List:",":"+modalAutomaticStatementAccount_List.getResponseCode());
                accountList.clear();
                commonArrayList.clear();
                if (modalAutomaticStatementAccount_List.getResponseCode() == 1) {
                    Log.e("Success","Success");
                    accountList = modalAutomaticStatementAccount_List.getAccountList();
                    if (accountList.size() != 0) {

                        for (int i = 0; i < accountList.size(); i++) {
                            if (accountList.get(i).getAccountType().equals("bank") || accountList.get(i).getAccountType().equals("BANK")) {
                                accountList_.clear();
                                accountList_=accountList.get(i).getAccountList();
                                for (int z = 0; z < accountList_.size(); z++) {
                                    Common common=new Common();
                                    common.setAccountName(accountList_.get(z).getAccountName());
                                    common.setBankName(accountList_.get(z).getBankName());
                                    common.setAccountNumber(accountList_.get(z).getAccountNumber());
                                    common.setSiteAccountId(accountList_.get(z).getSiteAccountId());
                                    common.setBankType(""+accountList.get(i).getAccountType());
                                    common.setItemAccountId(""+accountList_.get(z).getItemAccountId());
                                    common.setBankId(1);
                                    commonArrayList.add(common);
                                }
                            }
                            else if(accountList.get(i).getAccountType().equals("credits") || accountList.get(i).getAccountType().equals("CREDITS")){
                                accountList_.clear();
                                accountList_=accountList.get(i).getAccountList();
                                for (int z = 0; z < accountList_.size(); z++) {
                                    Common common=new Common();
                                    common.setAccountName(accountList_.get(z).getAccountName());
                                    common.setBankName(accountList_.get(z).getBankName());
                                    common.setAccountNumber(accountList_.get(z).getAccountNumber());
                                    common.setSiteAccountId(accountList_.get(z).getSiteAccountId());
                                    common.setBankType(""+accountList.get(i).getAccountType());
                                    common.setItemAccountId(""+accountList_.get(z).getItemAccountId());
                                    common.setBankId(2);
                                    commonArrayList.add(common);
                                }
                            }
                        }
                        Log.e("Final Size:",""+commonArrayList.size());
                        for (int i = 0; i < commonArrayList.size(); i++) {
                            Log.e("ID:",""+commonArrayList.get(i).getBankId());
                            Log.e("NAME:",""+commonArrayList.get(i).getAccountName());
                        }
                        AdapterAutomatic adapterAutomatic=new AdapterAutomatic(ActivityAutomaticStatement.this,commonArrayList,new OnRemoveListener() {
                            @Override
                            public void onRemove() {
                                Log.e("AGAIN", "AGAIN");
                                setUpUI();
                            }
                        });
                       stickyListHeadersListView.setAdapter(adapterAutomatic);
                    }
                } else {
                    AdapterAutomatic adapterAutomatic=new AdapterAutomatic(ActivityAutomaticStatement.this,commonArrayList);
                    stickyListHeadersListView.setAdapter(adapterAutomatic);
                    new DialogBox(ActivityAutomaticStatement.this, "No Record.", (DialogBoxListener) null, 0, 0);
                    return;
                }
            /*    if (modalAutomaticStatements.get(0).getResponseCode().equalsIgnoreCase("1")) {
                    if (modalAutomaticStatements.size() == 0) {
                        adapterConnectedAccounts = new AdapterConnectedAccounts((Activity) ActivityAutomaticStatement.this, modalAutomaticStatements, new OnRemoveListener() {
                            @Override
                            public void onRemove() {
                                Log.e("AGAIN", "AGAIN");
                                setUpUI();
                            }
                        });
                        lvList.setAdapter(adapterConnectedAccounts);
                        new DialogBox(ActivityAutomaticStatement.this, "No Record.", (DialogBoxListener) null, 0, 0);
                        return;

                    } else {
                        adapterConnectedAccounts = new AdapterConnectedAccounts((Activity) ActivityAutomaticStatement.this, modalAutomaticStatements, new OnRemoveListener() {

                            @Override
                            public void onRemove() {

                                setUpUI();
                            }
                        });
                        lvList.setAdapter(adapterConnectedAccounts);
                    }
                } else {
                    if (!modalAutomaticStatements.get(0).getResponseCode().equalsIgnoreCase("1")) {

                        new DialogBox(ActivityAutomaticStatement.this, "No Record.", (DialogBoxListener) null, 0, 0);

                    }
                    modalAutomaticStatements.clear();
                    adapterConnectedAccounts = new AdapterConnectedAccounts(ActivityAutomaticStatement.this, modalAutomaticStatements);
                    lvList.setAdapter(adapterConnectedAccounts);
                    return;
                }

                Log.e("HERE", "HERE");

                if (!modalAutomaticStatements.get(0).getResponseCode().equalsIgnoreCase("1")) {
                    Log.e("No Record", "No Record");
                    new DialogBox(ActivityAutomaticStatement.this, "No Record.", (DialogBoxListener) null, 0, 0);
                    return;
                }*/


            }


        });

    }


    @Override
    public void setUpListeners() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAdd) {
            Intent intent = new Intent(ActivityAutomaticStatement.this, ActivityAddAccountSelectBank.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_BANK);
        }
            /*switch (v.getId()){
                case R.id.btnAdd:
                    Intent intent = new Intent(ActivityAutomaticStatement.this , ActivityAddAccountSelectBank.class) ;
                    startActivityForResult(intent , REQUEST_CODE_SELECT_BANK );
                    break;
            }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_SELECT_BANK:
                /*if(resultCode == R.id.ivRbc){
                    bankId = "1";
                }else if(resultCode==R.id.ivScotia){
                    bankId = "2";
                }else if(resultCode== R.id.ivBmo){
                    bankId = "3";
                }else if(resultCode== R.id.ivTD){
                    bankId = "4";
                }
                else if(resultCode== R.id.ivCIBC){
                    bankId = "5";
                }
                else if(resultCode== R.id.ivOtherBank){
                    bankId = "7";
                }*/
                /*switch (resultCode){
//                    bankId = (requestCode ==R.id.ivRbc) ? 1
                    case R.id.ivRbc:
                            bankId = "1";
                        break;

                    case R.id.ivScotia:
                        bankId = "2";
                        break;

                    case R.id.ivBmo:
                        bankId = "3";
                        break;
                    case R.id.ivTD:
                        bankId = "4";
                        break;
                    case R.id.ivCIBC:
                        bankId = "5";
                        break;
                    case R.id.ivOtherBank:
                        bankId = "7";
                        break;
                }*/
              /*  Bundle bundle = new Bundle() ;
                bundle.putString(ActivityYodleeLogin.OPERATION, ActivityYodleeLogin.ADD_ACCOUNT);
                bundle.putString("BankId", bankId);

                Intent intent = new Intent(this , ActivityYodleeLogin.class );
                intent.putExtras(bundle) ;
                startActivityForResult(intent, REQUEST_CODE_IS_RELOAD_NEEDED);
                break;

            case REQUEST_CODE_IS_RELOAD_NEEDED:

                if(resultCode==Activity.RESULT_OK){
                setUpUI();
                }

                break;*/

        }
    }
}
