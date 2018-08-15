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
 * Created by kamaljeet.singh on 11/7/2016.
 */
public class SentInvoicePerformaActivity extends MyAbstractGridActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
        changeLogo();
    }
    @Override
    public void changeLogo() {
        super.changeLogo();
        iv_logo.setBackgroundResource(R.drawable.kippin_invoicing);
    }

    @Override
    public void updateToolbar() {
        BackWithHOME(getString(R.string.sent_section), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.finishActivity(SentInvoicePerformaActivity.this);
            }
        });
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.sent_invoice, "SENT INVOICE"));
        modalGridElements.add(getElement(R.drawable.desktop_sent_quote, "SENT QUOTE"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        callNextIntent(Invoice);
                        break;
                    case 1:
                        callNextIntent(Performa);
                        break;

                }

            }
        });
    }

    private void callNextIntent(String roldeID) {
        Intent i = new Intent();
        i.setClass(SentInvoicePerformaActivity.this, SentInvoiceActivity.class);
        i.putExtra("roleId", roldeID);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);

    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }
}

