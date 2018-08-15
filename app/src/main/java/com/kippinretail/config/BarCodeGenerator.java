package com.kippinretail.config;

import java.util.Random;

/**
 * Created by sandeep.saini on 4/1/2016.
 */
public class BarCodeGenerator
{
    public static String generateBarCode(String merchantId,String customerId,String giftcardId){

        String barcode="";
        Random random = new Random();
        String randomNo = String.valueOf (100000 + random.nextInt(900000));
        barcode = merchantId+customerId+giftcardId+randomNo;
        return barcode;
    }
    public static String generateLoyaltyBarcode(String phoneNo){
        String loyaltyBarcode = "";
        return loyaltyBarcode;
    }
}
