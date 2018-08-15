package com.kippinretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.Modal.webclient.Utility;

import java.util.ArrayList;

public class ActivityMyMerchant extends MyAbstractGridActivity{

    private Intent intent;
    private String parentButton;
    private TextView textView_giftCard,textView_punchCard;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToolbar();
        updateUI();
    }


    @Override
    public void updateToolbar() {
        generateActionBar(R.string.user_ActivityMyMerchant, true, true, false);
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.scan_gift_card , "Scan Gift Card")) ;
        modalGridElements.add(getElement(R.drawable.scan_punch_card , "Scan Punch Card")) ;
        modalGridElements.add(getElement(R.drawable.scan_loyalty_card , "Scan Loyalty Card")) ;

        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch(position){
                    case 0:
                        i.setClass(ActivityMyMerchant.this, EmployeeMechantListActivity.class);
                        i.putExtra("CardType", ScanCardType.GIFT);
                        break;
                    case 1:
                        // MerchantsOfPurchasedGiftCardsActivity
                        i.setClass(ActivityMyMerchant.this , EmployeeMechantListActivity.class);
                        i.putExtra("CardType", ScanCardType.PUNCH);
                        break;
                    case 2:
                        i.setClass(ActivityMyMerchant.this, EmployeeMechantListActivity.class);
                        i.putExtra("CardType", ScanCardType.LOYALTY);
                        break;
                   
                }
                startActivity(i);
                overridePendingTransition(R.anim.animation_from_right,R.anim.animation_to_left);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtility.TemplateImagePath = null;
        intent = getIntent();
//        if(intent!=null){
//            parentButton = intent.getStringExtra("parentButton");
//            if(parentButton.equals("MyMerchant")){
//                textView_giftCard.setText("Scan Gift Card");
//                textView_punchCard.setText("Scan Punch Card");
//            }
//            else if(parentButton.equals("GiftCard")){
//                textView_giftCard.setText("Create Gift Card");
//                textView_punchCard.setText("Create Punch Card");
//            }
//        }
    }

    public void  onScanGiftCard(View v){
        if(CommonData.getUserData(this).getRoleId()== Integer.parseInt(Utility.ROLE_ID_MERCHANT )){
            intent.setClass(this, GiftCardsMerchantActivity.class );
            intent.putExtra(GiftCardsMerchantActivity.OPERATION, GiftCardsMerchantActivity.GIFTCARD);
        }else{
            intent.setClass(this,MerchantsOfPurchasedGiftCardsActivity.class);
            intent.putExtra("parentButton", "ActivityMyMerchant");
            intent.putExtra("operation", "GiftCard");
        }
        startActivity(intent);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }


    public void  onScanPunchCard(View v){
        Intent intent = getIntent();


        if(CommonData.getUserData(this).getRoleId()== Integer.parseInt(Utility.ROLE_ID_MERCHANT )){

            intent.setClass(this, GiftCardsMerchantActivity.class);
            intent.putExtra(GiftCardsMerchantActivity.OPERATION, GiftCardsMerchantActivity.PUNCHCARD);
        }else{
            intent.setClass(this,MerchantsOfPurchasedGiftCardsActivity.class);
            intent.putExtra("parentButton", "ActivityMyMerchant");
            intent.putExtra("operation", "PunchCard");
        }

        startActivity(intent);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }

    public enum ScanCardType{
        GIFT,PUNCH,LOYALTY;
    }
}
