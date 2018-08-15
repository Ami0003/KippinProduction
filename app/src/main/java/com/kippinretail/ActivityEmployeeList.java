package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.EmployeeListtAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForPointAnalytics;
import com.kippinretail.Modal.ServerResponseForEmployeeList.EmployeeListServerResponse;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;


import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityEmployeeList extends SuperActivity implements AdapterView.OnItemClickListener{
    private TextView txtSearch;
    private String merchatId;
    ListView  lvMerchants;
    LinearLayout layout_nonKippin;
    EmployeeListtAdapter adapter = null;
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_employee_list);
        initialiseUI();
        setUpUI();
        setUpListeners();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        txtSearch = (TextView)findViewById(R.id.txtSearch);
        lvMerchants = (ListView)findViewById(R.id.lvMerchants);
        lvMerchants.setOnItemClickListener(this);
        layout_nonKippin = (LinearLayout)findViewById(R.id.layout_nonKippin);
        generateActionBar(R.string.title_activity_employee_list, true, true, false);
    }

    @Override
    public void setUpUI() {
        super.setUpUI();
        layout_nonKippin.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
        String ar[] = {"Sandeep","Mandeep","Ravi","Sandeep","Mandeep","Ravi"};
      /// lvMerchants.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ar));
          makeEmployeeList();
    }

    private void makeEmployeeList() {
        final String merchantId = "" + CommonData.getUserData(this).getId();
        Log.e(merchantId ,merchantId);
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getEmployeeList(merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());

                Gson gson = new Gson();
                List<EmployeeListServerResponse> employeeListServerResponses = gson.fromJson(jsonElement.toString(),new TypeToken<List<EmployeeListServerResponse>>(){}.getType());
                boolean flag = Utility.isResponseValid(employeeListServerResponses)  ;
                if(flag){
                    adapter = new EmployeeListtAdapter(employeeListServerResponses,ActivityEmployeeList.this,merchantId);
                    lvMerchants.setAdapter(adapter);
                }else{
                    MessageDialog.showDialog(ActivityEmployeeList.this, employeeListServerResponses.get(0).getResponseMessage(), true);
                }

                LoadingBox.dismissLoadingDialog();;
            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(ActivityEmployeeList.this);

                LoadingBox.dismissLoadingDialog();
            }
        });
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
