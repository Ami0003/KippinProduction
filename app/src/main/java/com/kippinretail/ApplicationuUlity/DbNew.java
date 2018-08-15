package com.kippinretail.ApplicationuUlity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kippinretail.Modal.ResponseToGetLoyaltyCardLocalModel;
import com.google.gson.Gson;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.GiftCardMerchantList.MerchantDetail;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.internal.FacebookRequestErrorClassification.KEY_NAME;


public class DbNew {

    /*************
     * Variables of Table
     *****************/

    public static final String KEY_ROWID_TABLE = "row_id";

    public static final String KEY_NON_LOYALITY_CARD_ID = "non_kippin_card_id";
    public static final String KEY_NON_LOYALITY_CARD = "Non_Loyality_card";


    public static final String KEY_LOYALITY_CARD = "Loyality_Card";
    public static final String KEY_LOYALITY_CARD_ID = "Loyality_Card_Id";
    public static final String KEY_LOYALITY_PROFILE_IMAGE ="Profile_Image";


    public static final String KEY_NON_GIFT_CARD_ID = "Gift_Card";
    public static final String KEY_NON_GIFT_CARD = "Gift_Card_Id";


    public static final String KEY_MERCHANT_GIFT_CARD_ID = "merchant_gift_id";
    public static final String KEY_MERCHANT_DATA = "merechant_gift_data";

    public static final String KEY_PURCHASED_CARD_MERCHANTID = "purchased_merchant_id";
    public static final String KEY_PURCHASED_CARD_DATA = "purchased_data";

    /***************
     * TABLE NAME
     *******************/
    public static final String TABLE_NON_LOYALITY = "Table_Non_Loyality";
    public static final String TABLE_LOYALITY = "Table_Loyality";
    public static final String TABLE_MERCHANT_GIFTCARD = "Table_GiftMerchant";
    public static final String TABLE_NON_GIFTCARD = "Table_Non_Gift_Card";
    public static final String TABLE_PURCHASED_GIFTCARD = "Table_Purchased_Card";

    /*******************
     * DATABASE NAME
     *************************/
    public static final String DATABASE_NAME = "Kippin_Retail";
    public static final int DATABASE_VERSION = 2;
    static DbNew dbNew = new DbNew();
    SQLiteDatabase db;
    private Context ourcontext;
    private DbHelper ourhelper1;
    private long lastId;

    public static DbNew getInstance(Context context) {
        dbNew.ourcontext = context;
        dbNew.open();
        return getInstance();
    }

    public static DbNew getInstance() {
        if (dbNew == null) {
            dbNew = new DbNew();
        }
        return dbNew;
    }

    // open database to perform operation
    public DbNew open() {
        ourhelper1 = new DbHelper(ourcontext);
        db = ourhelper1.getWritableDatabase();
        return DbNew.this;
    }

    // close the data base after performing any database operation
    public void close() {
        // TODO Auto-generated method stub
        ourhelper1.close();
    }

    /*******
     * ALL NON KIPPIN CARD ENTRY
     */

    public long CreateNonKippinCard(String uniqueId, String data) {
        return db.insert(
                TABLE_NON_LOYALITY,
                null,
                createContentValues(uniqueId, data));
    }

    private ContentValues createContentValues(String uniqueId, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_NON_LOYALITY_CARD_ID, uniqueId);
        cv2.put(KEY_NON_LOYALITY_CARD, data);

        return cv2;

    }


    // Create Non -- Giftcard

    public long CreateNonGiftCard(String uniqueId, String data) {
        return db.insert(
                TABLE_NON_GIFTCARD,
                null,
                createContentValuesNonGiftCard(uniqueId, data));
    }

    private ContentValues createContentValuesNonGiftCard(String uniqueId, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_NON_GIFT_CARD_ID, uniqueId);
        cv2.put(KEY_NON_GIFT_CARD, data);
        return cv2;

    }

    // purchased card

    public long CreatePurchasedCard(String uniqueId, String data) {
        return db.insert(
                TABLE_PURCHASED_GIFTCARD,
                null,
                createContentValuesPurchasedCard(uniqueId, data));
    }

    private ContentValues createContentValuesPurchasedCard(String uniqueId, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_PURCHASED_CARD_MERCHANTID, uniqueId);
        cv2.put(KEY_PURCHASED_CARD_DATA, data);
        return cv2;

    }


    ///  DATA ENTRY FOR LOYALITY CARD
    public long CreateLoyalityCardEntry(String loyalityCardId, String data,byte[] profileImage) {
        return db.insert(
                TABLE_LOYALITY,
                null,
                createLoyalityCard(loyalityCardId, data,profileImage));
    }

    private ContentValues createLoyalityCard(String loyalityCardId, String data,byte[] profileImage) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_LOYALITY_CARD_ID, loyalityCardId);
        cv2.put(KEY_LOYALITY_PROFILE_IMAGE,profileImage);
        cv2.put(KEY_LOYALITY_CARD, data);
        return cv2;
    }

    // Create GiftMerchant card

    public long CreateMerchantGiftCard(String id, String s) {
        return db.insert(
                TABLE_MERCHANT_GIFTCARD,
                null,
                createMerchantGiftCard(id, s));
    }

    private ContentValues createMerchantGiftCard(String id, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_MERCHANT_GIFT_CARD_ID, id);
        cv2.put(KEY_MERCHANT_DATA, data);
        return cv2;
    }

    ////// ------------------ GET ALL DATA -------------------/////////

    // Loyality card Entry
    public List<Merchant> getLoyalityCard() {

        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_LOYALITY_CARD_ID,KEY_LOYALITY_PROFILE_IMAGE ,KEY_LOYALITY_CARD};
        List<Merchant> merchants = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_LOYALITY, columns, null, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_LOYALITY_CARD);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            Merchant CE = gson.fromJson(result2, Merchant.class);
            merchants.add(CE);
        }
        Log.e("SIZE_ ", " = =" + merchants.size());
        c.close();
        c.requery();
       // LoadingBox.dismissLoadingDialog();
        return merchants;

    }

    // gets all the images
    public ArrayList<Bitmap> getbitmaps() {
        // TODO Auto-generated method stub
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_LOYALITY_CARD_ID,KEY_LOYALITY_PROFILE_IMAGE ,KEY_LOYALITY_CARD};
        Cursor c = db.query(TABLE_LOYALITY, columns, null, null, null, null,
                null);

       // int count = 0;

        int in1 = c.getColumnIndex(KEY_LOYALITY_PROFILE_IMAGE);
        int count_len = c.getCount();
        Bitmap[] bitmapArray = new Bitmap[count_len];

        byte[] imageByteArray = null;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            imageByteArray = c.getBlob(in1);

            // the cursor is not needed anymore so release it
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    imageByteArray);

            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            bitmaps.add(theImage);
           // bitmapArray[count] = theImage;
           // count++;
        }

        c.close();
        c.requery();

        return bitmaps;

    }


    // Get BITMAP
/*    public ArrayList<String> getLoyalityProfileImage() {
        // TODO Auto-generated method stub
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_LOYALITY_CARD_ID, KEY_LOYALITY_CARD, KEY_LOYALITY_PROFILE_IMAGE, KEY_LOYALITY_PUNCH_CARD_IMAGE};
        ArrayList<String> localityImages = new ArrayList<>();
        Cursor c = db.query(TABLE_LOYALITY, columns, null, null, null, null,
                null);
        String result2 = "";
        int in2 = c.getColumnIndex(KEY_LOYALITY_PROFILE_IMAGE);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            localityImages.add(result2);
        }
        Log.e("SIZE_result2 ", " = =" + result2);

        c.close();
        c.requery();
        return localityImages;
    }

    // Get BITMAP
    public ArrayList<String> getLoyalityPunchCard() {
        // TODO Auto-generated method stub
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_LOYALITY_CARD_ID, KEY_LOYALITY_CARD, KEY_LOYALITY_PROFILE_IMAGE, KEY_LOYALITY_PUNCH_CARD_IMAGE};
        ArrayList<String> localityImages = new ArrayList<>();
        Cursor c = db.query(TABLE_LOYALITY, columns, null, null, null, null, null);
        String result2 = "";
        Gson gson = new Gson();
        int in2 = c.getColumnIndex(KEY_LOYALITY_PUNCH_CARD_IMAGE);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            localityImages.add(result2);
        }
        Log.e("SIZE_result2 ", " = =" + result2);
        c.close();
        c.requery();
        return localityImages;
    }*/

    // Get NON - KIPPIN Loyality Card

    // Non Loyality card Entry
    public List<ResponseToGetLoyaltyCard> getNonKippinData() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_NON_LOYALITY_CARD};
        List<ResponseToGetLoyaltyCard> responseToGetLoyaltyCards = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_LOYALITY, columns, null, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_NON_LOYALITY_CARD);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            ResponseToGetLoyaltyCard CE = gson.fromJson(result2, ResponseToGetLoyaltyCard.class);
            responseToGetLoyaltyCards.add(CE);
        }
        Log.e("SIZE_ ", " = =" + responseToGetLoyaltyCards.size());
        c.close();
        c.requery();
        return responseToGetLoyaltyCards;
    }

    public ResponseToGetLoyaltyCardLocalModel getParticularRecordGiftCard(String id) {
        String[] columns = new String[]{KEY_ROWID_TABLE,KEY_NON_GIFT_CARD_ID, KEY_NON_GIFT_CARD};
        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCards = new ResponseToGetLoyaltyCardLocalModel();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_GIFTCARD, columns, KEY_NON_GIFT_CARD_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_NON_GIFT_CARD);
        if (c != null) c.moveToFirst();
        Gson gson = new Gson();
        result2 = c.getString(in2);
        ResponseToGetLoyaltyCardLocalModel CE = gson.fromJson(result2, ResponseToGetLoyaltyCardLocalModel.class);
        responseToGetLoyaltyCards.setActualUserId(CE.getActualUserId());
        responseToGetLoyaltyCards.setBackImage(CE.getBackImage());
        responseToGetLoyaltyCards.setBarcode(CE.getBarcode());
        responseToGetLoyaltyCards.setCardId(CE.getCardId());
        responseToGetLoyaltyCards.setId(CE.getId());
        responseToGetLoyaltyCards.setFolderName(CE.getFolderName());
        responseToGetLoyaltyCards.setFrontImage(CE.getFrontImage());
        responseToGetLoyaltyCards.setLogoImage(CE.getLogoImage());
        responseToGetLoyaltyCards.setUserId(CE.getUserId());
        responseToGetLoyaltyCards.setCountry(CE.getCountry());
        c.close();
        return responseToGetLoyaltyCards;


    }
    public ResponseToGetLoyaltyCardLocalModel getParticularRecord(String id) {
        String[] columns = new String[]{KEY_ROWID_TABLE,KEY_NON_LOYALITY_CARD_ID, KEY_NON_LOYALITY_CARD};
        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCards = new ResponseToGetLoyaltyCardLocalModel();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_LOYALITY, columns, KEY_NON_LOYALITY_CARD_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_NON_LOYALITY_CARD);
        if (c != null) c.moveToFirst();
        Gson gson = new Gson();
        result2 = c.getString(in2);
        ResponseToGetLoyaltyCardLocalModel CE = gson.fromJson(result2, ResponseToGetLoyaltyCardLocalModel.class);
        responseToGetLoyaltyCards.setActualUserId(CE.getActualUserId());
        responseToGetLoyaltyCards.setBackImage(CE.getBackImage());
        responseToGetLoyaltyCards.setBarcode(CE.getBarcode());
        responseToGetLoyaltyCards.setCardId(CE.getCardId());
        responseToGetLoyaltyCards.setId(CE.getId());
        responseToGetLoyaltyCards.setFolderName(CE.getFolderName());
        responseToGetLoyaltyCards.setFrontImage(CE.getFrontImage());
        responseToGetLoyaltyCards.setLogoImage(CE.getLogoImage());
        responseToGetLoyaltyCards.setUserId(CE.getUserId());
        responseToGetLoyaltyCards.setCountry(CE.getCountry());
        c.close();
        return responseToGetLoyaltyCards;


    }
    public List<ResponseToGetLoyaltyCardLocalModel> get_NonKippinData() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_NON_LOYALITY_CARD};
        List<ResponseToGetLoyaltyCardLocalModel> responseToGetLoyaltyCards = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_LOYALITY, columns, null, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_NON_LOYALITY_CARD);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            ResponseToGetLoyaltyCardLocalModel CE = gson.fromJson(result2, ResponseToGetLoyaltyCardLocalModel.class);
            responseToGetLoyaltyCards.add(CE);
        }
        Log.e("SIZE_ ", " = =" + responseToGetLoyaltyCards.size());
        c.close();
        c.requery();
        return responseToGetLoyaltyCards;
    }



 /*   public List<NonKippinImages> getNonLKippinImages() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_NON_LOYALITY_CARD};
        List<NonKippinImages> nonKippinImages = new ArrayList<>();

        Cursor c = db.query(TABLE_NON_LOYALITY, columns, null, null, null, null,
                null);
        int in1 = c.getColumnIndex(KEY_NON_LOYALITY_CARD_BACK_IMAGE);
        int in2 = c.getColumnIndex(KEY_NON_LOYALITY_CARD_FRONT_IMAGE);
        int in3 = c.getColumnIndex(KEY_NON_LOYALITY_CARD_PROFILE_IMAGE);

        NonKippinImages nonKippinImages1 = new NonKippinImages();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String result1 = c.getString(in1);
            String result2 = c.getString(in2);
            String result3 = c.getString(in3);

            nonKippinImages1.setBackimage(result1);
            nonKippinImages1.setFrontImage(result2);
            nonKippinImages1.setProfileImage(result3);
            nonKippinImages.add(nonKippinImages1);

        }
        Log.e("SIZE_ ", " = =" + nonKippinImages.size());
        c.close();
        c.requery();
        return nonKippinImages;
    }*/

    //

    // GIFT card Data
    public List<ResponseToGetLoyaltyCard> getNonGiftCard() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_NON_GIFT_CARD_ID, KEY_NON_GIFT_CARD};
        List<ResponseToGetLoyaltyCard> merchants = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_GIFTCARD, columns, null, null, null, null,
                null);
        int in1 = c.getColumnIndex(KEY_NON_GIFT_CARD_ID);
        int in2 = c.getColumnIndex(KEY_NON_GIFT_CARD);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            ResponseToGetLoyaltyCard CE = gson.fromJson(result2, ResponseToGetLoyaltyCard.class);
            merchants.add(CE);
        }
        Log.e("SIZE_ ", " = =" + merchants.size());
        c.close();
        c.requery();
        return merchants;
    }

    public List<ResponseToGetLoyaltyCardLocalModel> getNon_GiftCard() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_NON_GIFT_CARD_ID, KEY_NON_GIFT_CARD};
        List<ResponseToGetLoyaltyCardLocalModel> merchants = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_NON_GIFTCARD, columns, null, null, null, null,
                null);
        int in1 = c.getColumnIndex(KEY_NON_GIFT_CARD_ID);
        int in2 = c.getColumnIndex(KEY_NON_GIFT_CARD);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            ResponseToGetLoyaltyCardLocalModel CE = gson.fromJson(result2, ResponseToGetLoyaltyCardLocalModel.class);
            merchants.add(CE);
        }
        Log.e("SIZE_ ", " = =" + merchants.size());
        c.close();
        c.requery();
        return merchants;
    }
    // Purchassd Card data

    // GIFT card Data
    public List<GiftCard> purchasedCardData(String merchantId) {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_PURCHASED_CARD_MERCHANTID, KEY_PURCHASED_CARD_DATA};
        List<GiftCard> giftCard = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_PURCHASED_GIFTCARD, columns, null, null, null, null,
                null);
     /*   Cursor c = db.query(TABLE_PURCHASED_GIFTCARD, columns, KEY_PURCHASED_CARD_MERCHANTID + "=?",
                new String[]{merchantId}, null, null, null);*/
        int in2 = c.getColumnIndex(KEY_PURCHASED_CARD_DATA);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            GiftCard CE = gson.fromJson(result2, GiftCard.class);
            giftCard.add(CE);
        }
        Log.e("SIZE_ ", " = =" + giftCard.size());
        c.close();
        c.requery();
        return giftCard;
    }

    // GIFT card Data
    public List<MerchantDetail> merchantGiftCard() {
        String[] columns = new String[]{KEY_ROWID_TABLE, KEY_MERCHANT_GIFT_CARD_ID, KEY_MERCHANT_DATA};
        List<MerchantDetail> merchants = new ArrayList<>();
        String result2 = "";
        Cursor c = db.query(TABLE_MERCHANT_GIFTCARD, columns, null, null, null, null,
                null);
        int in2 = c.getColumnIndex(KEY_MERCHANT_DATA);
        Gson gson = new Gson();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result2 = c.getString(in2);
            MerchantDetail CE = gson.fromJson(result2, MerchantDetail.class);
            merchants.add(CE);
        }
        Log.e("SIZE_ ", " = =" + merchants.size());
        c.close();
        c.requery();
        return merchants;
    }


    // get all Student and their Details of Student Table*****/

    /******************
     * these methods are using in Setting_Module if User will delete
     * Student,Actions,AllData
     ********************/
    public void deleteAllEntries() {
        db.delete(TABLE_NON_LOYALITY, null, null);
        db.delete(TABLE_NON_GIFTCARD, null, null);

    }


    // // Products Table....................

    public void deleteParticularInsertedItem(String id) {
        db.delete(TABLE_LOYALITY, KEY_LOYALITY_CARD_ID + "=?",
                new String[]{id});
    }

    // Delete Card Entry from Kippin Non Loyality card
    public void deleteNonLoyalitycard(String id) {
        db.delete(TABLE_LOYALITY, KEY_NON_LOYALITY_CARD_ID + "=?",
                new String[]{id});
    }

    // Delete All Merchant Gift
    public void DeleteAllGiftMerchant() {
        db.delete(TABLE_MERCHANT_GIFTCARD, null, null);
    }

    public void deleteParticularNonKippinGiftcard(String id) {
        db.delete(TABLE_NON_GIFTCARD, KEY_NON_GIFT_CARD_ID + "=?",
                new String[]{id});
    }
    public void deleteParticularNonLoyalitycard(String id) {
        db.delete(TABLE_NON_LOYALITY, KEY_NON_LOYALITY_CARD_ID + "=?",
                new String[]{id});
    }

    public void deleteMultipleGiftCard(String cardIds){
        String sql = "DELETE FROM "+TABLE_NON_GIFTCARD+" WHERE "+KEY_NON_GIFT_CARD_ID +" IN ("+cardIds+" )";

        db.execSQL(sql);
    }

    public void DeleteAllNonKippinGift() {
        db.delete(TABLE_NON_GIFTCARD, null, null);
    }

    // delete merchant gift card

    public void DeleteMerchantGiftCard(String id) {
        db.delete(TABLE_MERCHANT_GIFTCARD, KEY_MERCHANT_GIFT_CARD_ID + "=?",
                new String[]{id});
    }

    // Delete all Cards
    public void DeleteAllUser() {
        db.delete(TABLE_LOYALITY, null, null);
    }

    // Delete non kippin Table
    public void DeleteNonKippinLocal() {
        db.delete(TABLE_NON_LOYALITY, null, null);
    }

    // Delete purChased Card Table
    public void DeletePurchasedTable() {
        db.delete(TABLE_PURCHASED_GIFTCARD, null, null);
    }


     // Update Non Kippin DataBase



    public long updateNonKippinLoyalityCard(String id, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_NON_LOYALITY_CARD_ID, id);
        cv2.put(KEY_NON_LOYALITY_CARD, data);
        //db.update(TABLE_NON_LOYALITY, cv2, KEY_NON_LOYALITY_CARD_ID+"=?", new String[] { id });
        return db.update(TABLE_NON_LOYALITY, cv2, KEY_NON_LOYALITY_CARD_ID+"=?", new String[] { id });
    }
    public long updateNonKippinGiftCard(String id, String data) {
        ContentValues cv2 = new ContentValues();
        cv2.put(KEY_NON_GIFT_CARD_ID, id);
        cv2.put(KEY_NON_GIFT_CARD, data);
        return db.update(TABLE_NON_GIFTCARD, cv2, KEY_NON_GIFT_CARD_ID + "=?",
                new String[] { id });
    }




    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        // in this we execute query to create table

        /*******************
         * Declaration of Tables..
         *************/

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            // Table for loyality card
            db.execSQL(" CREATE TABLE " + TABLE_LOYALITY + " ("
                    + KEY_ROWID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + KEY_LOYALITY_CARD_ID + " TEXT ,"
                    + KEY_LOYALITY_PROFILE_IMAGE + " BLOB ,"
                    + KEY_LOYALITY_CARD + " TEXT);");


            // Table for Non Kippin card
            db.execSQL(" CREATE TABLE " + TABLE_NON_LOYALITY + " ("
                    + KEY_ROWID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + KEY_NON_LOYALITY_CARD_ID + " TEXT ,"
                    + KEY_NON_LOYALITY_CARD + " TEXT);");

            // Table for GiftCards
            db.execSQL(" CREATE TABLE " + TABLE_MERCHANT_GIFTCARD + " ("
                    + KEY_ROWID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_MERCHANT_GIFT_CARD_ID + " TEXT ," + KEY_MERCHANT_DATA
                    + " TEXT);");

            // Table for GiftCards
            db.execSQL(" CREATE TABLE " + TABLE_NON_GIFTCARD + " ("
                    + KEY_ROWID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NON_GIFT_CARD_ID + " TEXT ," + KEY_NON_GIFT_CARD
                    + " TEXT);");

            // Table for GiftCards
            db.execSQL(" CREATE TABLE " + TABLE_PURCHASED_GIFTCARD + " ("
                    + KEY_ROWID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_PURCHASED_CARD_MERCHANTID + " TEXT ," + KEY_PURCHASED_CARD_DATA
                    + " TEXT);");


        }

        // this method calls when the table name already exists
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NON_LOYALITY);
            onCreate(db);
        }

    }
}