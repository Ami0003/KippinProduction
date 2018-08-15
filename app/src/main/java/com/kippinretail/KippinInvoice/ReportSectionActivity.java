package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.MyAbstractGridActivity;
import com.kippinretail.R;

import java.util.ArrayList;

/**
 * Created by Codewix Dev on 6/5/2017.
 */

public class ReportSectionActivity extends MyAbstractGridActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        BackWithHOME(getString(R.string.report_section), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.finishActivity(ReportSectionActivity.this);
            }
        });
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.desktop_sent, "SENT"));
        modalGridElements.add(getElement(R.drawable.received, "RECEIVED"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        i.setClass(ReportSectionActivity.this, InvoiceReportActivity.class);
                        i.putExtra("roleId",Customer);
                        i.putExtra("reportId",1);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:
                        i.setClass(ReportSectionActivity.this, InvoiceReportActivity.class);
                        i.putExtra("roleId",Supplier);
                        i.putExtra("reportId",2);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;

                }

            }
        });
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }
}