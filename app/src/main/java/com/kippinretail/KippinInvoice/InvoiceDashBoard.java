package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.MyAbstractGridActivity;
import com.kippinretail.R;
import com.kippinretail.config.Config;
import com.kippinretail.interfaces.ButtonYesNoListner;
import com.kippin.app.Kippin;

import java.util.ArrayList;

/**
 * Created by kamaljeet.singh on 11/1/2016.
 */

public class InvoiceDashBoard extends MyAbstractGridActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("InvoiceDashBoard","InvoiceDashBoard");
        updateUI();
        changeLogo();

    }

    @Override
    public void changeLogo() {
        super.changeLogo();
        iv_logo.setBackgroundResource(R.drawable.kippin_invoicing_logo);
    }

    @Override
    public void updateToolbar() {
        generateTitleWithLogoutString(getString(R.string.title_invoice_Dashboard), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonData.getInvoiceUserData(InvoiceDashBoard.this) == null){
                    Intent in = new Intent();
                    in.setClass(InvoiceDashBoard.this, Kippin._FinanceDashBoard);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("RegistrationType", "0");
                    in.putExtra("InvoiceUserType", "true");
                    startActivity(in);
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }
                else{
                    finish();
                    overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                }


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
        if (CommonData.getInvoiceUserData(InvoiceDashBoard.this) == null){
            Intent in = new Intent();
            in.setClass(InvoiceDashBoard.this, Kippin._FinanceDashBoard);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("RegistrationType", "0");
            in.putExtra("InvoiceUserType", "true");
            startActivity(in);
            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }
        else{
            finish();
            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.create_invoice, "CREATE INVOICE"));
        modalGridElements.add(getElement(R.drawable.create_proforma, "CREATE QUOTE"));
        modalGridElements.add(getElement(R.drawable.received_invoice_proforma, "RECEIVED INVOICE/QUOTE"));
        modalGridElements.add(getElement(R.drawable.sent_invoice_proforma, "SENT INVOICE/QUOTE"));
        modalGridElements.add(getElement(R.drawable.invoice_report, "INVOICE REPORT"));
        modalGridElements.add(getElement(R.drawable.supplier_customer_list, "SUPPLIER/CUSTOMER LIST"));

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Config.SCREEN_OPEN=3;
                        CommonUtility.InvoiceTitle = "1";
                        CommonUtility.INVOICE_TYPE = Invoice;
                        callNextIntent(InvoiceCustomerList.class, CreateInvoiceCustomer.class);
                        break;
                    case 1:
                        Config.SCREEN_OPEN=3;
                        CommonUtility.InvoiceTitle = "2";
                        CommonUtility.INVOICE_TYPE = Performa;
                        callNextIntent(InvoiceCustomerList.class, CreateInvoiceCustomer.class);
                        break;
                    case 2:
                        CommonUtility.Sent_Receiver = "1";
                        callSupplierIntent(ReceivedInvoicePerformaActivity.class);
                        break;
                    case 3:
                        CommonUtility.Sent_Receiver = "0";
                        callSupplierIntent(SentInvoicePerformaActivity.class);
                        break;
                    case 4:
                        Config.SCREEN_OPEN=3;
                        callSupplierIntent(ReportSectionActivity.class);

                        break;
                    case 5:
                        Config.SCREEN_OPEN=3;
                        callSupplierIntent(SupplierCustomerListActivity.class);
                        break;
                }

            }
        });
    }

    private void callNextIntent(final Class c, final Class targetClass) {

        CommonDialog.DialogToCreateuser(InvoiceDashBoard.this, "Is this for a new Customer?", new ButtonYesNoListner() {
            @Override
            public void YesButton() {
                Yes_NoClickIntent(targetClass);
            }

            @Override
            public void NoButton() {
                Config.SCREEN_OPEN=3;
                Yes_NoClickIntent(c);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtility.isInvoicedashBoard = true;
    }

    public void Yes_NoClickIntent(final Class c) {
        Intent i = new Intent();
        i.setClass(InvoiceDashBoard.this, c);
       // i.putExtra("roleId",Customer);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void callSupplierIntent(Class c) {
        Intent i = new Intent();
        i.setClass(InvoiceDashBoard.this, c);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {


    }
}
