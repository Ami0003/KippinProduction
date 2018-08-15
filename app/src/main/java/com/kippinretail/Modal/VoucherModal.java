package com.kippinretail.Modal;

import com.kippinretail.Interface.SuperModal;

/**
 * Created by sandeep.saini on 3/16/2016.
 */
public class VoucherModal implements SuperModal
{
    private String price;
    private String url;
    private boolean ischeched;
    private String message;
    private String giftId;
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean ischeched() {
        return ischeched;
    }

    public void setIscheched(boolean ischeched) {
        this.ischeched = ischeched;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
