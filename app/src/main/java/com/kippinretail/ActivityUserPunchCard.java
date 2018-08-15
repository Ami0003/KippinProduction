package com.kippinretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

// USER CAN SEE ITS LOYALITY CARD
// USER CAN SEE ITS PUNCH CARD
// USER CAN CREATE NEW PUNCH CARD
public class ActivityUserPunchCard extends SuperActivity implements View.OnClickListener{

    private RelativeLayout layout_myPointCards,layout_myPunchCards,layout_getNewPunchCards;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_user_punch_card);
        initUI();
        updateUI();
        addListener();
    }
    private void initUI(){
        layout_myPointCards = (RelativeLayout)findViewById(R.id.layout_myPointCards);
        layout_myPunchCards = (RelativeLayout)findViewById(R.id.layout_myPunchCards);
        layout_getNewPunchCards = (RelativeLayout)findViewById(R.id.layout_getNewPunchCards);
    }
    private void updateUI(){
        generateActionBar(R.string.title_user_points,true,true,false);
    }
    private void addListener(){
        layout_myPointCards.setOnClickListener(this);
        layout_myPunchCards.setOnClickListener(this);
        layout_getNewPunchCards.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_myPointCards:
                getAllMyLoyalityCards();// MY POINT CARDS
                break;
            case R.id.layout_myPunchCards:
                getAllMyPunchCard();
                break;
            case R.id.layout_getNewPunchCards:
                createNewPunchCard();
                break;
        }
    }
    private void getAllMyLoyalityCards(){
        Intent i = new Intent();
        i.setClass(ActivityUserPunchCard.this,MerchantsOfPurchasedGiftCardsActivity.class);
        i.putExtra("parentButton", "myPoint");
        i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.LOYALITY_CARD );
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void getAllMyPunchCard(){

        Intent i = new Intent();
        i.setClass(ActivityUserPunchCard.this,MerchantsOfPurchasedGiftCardsActivity.class);
        i.putExtra("parentButton", "MyPunchCards");
        i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.PUNCH_CARD);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
    private void createNewPunchCard(){
        Intent i = new Intent();
        i.setClass(ActivityUserPunchCard.this,SelectCountryActivity.class);
        i.putExtra("parentButton", "GetNewPunchCard");
        i.putExtra(MyPoint_FolderListActivity.OPERATION, MyPoint_FolderListActivity.PUNCH_CARD);
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
}
