package com.kippinretail.KippinInvoice.ModalInvoice;

import java.io.Serializable;

/**
 * Created by kamaljeet.singh on 11/23/2016.
 */
public class ItemModal implements Serializable {
    String Id;
    String Item;
    String ServiceProductType;
    String ServiceProductId;
    String Description;
    Double QuantityHours;
    Double Rate ;
    Double TotalAmount ;
    String Discount;
    String DiscountPercentage;
    int SpinnerItemPosition;
    String HST;
    String GST;
    String QST;
    String PST;
    String HSTPercentage;
    String GSTPercentage;
    String QSTPercentage;
    String PSTPercentage;

    String Tax;
    String TaxPercentage;
    String SubTotal;

    public String getHSTPercentage() {
        return HSTPercentage;
    }

    public void setHSTPercentage(String HSTPercentage) {
        this.HSTPercentage = HSTPercentage;
    }

    public String getGSTPercentage() {
        return GSTPercentage;
    }

    public void setGSTPercentage(String GSTPercentage) {
        this.GSTPercentage = GSTPercentage;
    }

    public String getQSTPercentage() {
        return QSTPercentage;
    }

    public void setQSTPercentage(String QSTPercentage) {
        this.QSTPercentage = QSTPercentage;
    }

    public String getPSTPercentage() {
        return PSTPercentage;
    }

    public void setPSTPercentage(String PSTPercentage) {
        this.PSTPercentage = PSTPercentage;
    }

    public String getTaxPercentage() {
        return TaxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public String getHST() {
        return HST;
    }

    public void setHST(String HST) {
        this.HST = HST;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getQST() {
        return QST;
    }

    public void setQST(String QST) {
        this.QST = QST;
    }

    public String getPST() {
        return PST;
    }

    public void setPST(String PST) {
        this.PST = PST;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subTotal) {
        SubTotal = subTotal;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getServiceProductId() {
        return ServiceProductId;
    }

    public void setServiceProductId(String serviceProductId) {
        ServiceProductId = serviceProductId;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getServiceProductType() {
        return ServiceProductType;
    }

    public void setServiceProductType(String serviceProductType) {
        ServiceProductType = serviceProductType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getQuantityHours() {
        return QuantityHours;
    }

    public void setQuantityHours(Double quantityHours) {
        QuantityHours = quantityHours;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public int getSpinnerItemPosition() {
        return SpinnerItemPosition;
    }

    public void setSpinnerItemPosition(int spinnerItemPosition) {
        SpinnerItemPosition = spinnerItemPosition;
    }
}
