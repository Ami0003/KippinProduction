package com.kippin.webclient.model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by karan.nassa on 15/6/2016.
 */
public class ModalRevenueExpense implements Serializable {
    private Integer ClassificationChartAccountNumber;
    private String ClassificationName;
    private String ClassificationType;
    private double GrossTotal;
    private boolean isSectionItem;
    public int color = 0;

    public ModalRevenueExpense(Integer classificationChartAccountNumber, String classificationName, String classificationType, double grossTotal, boolean isSectionItem, String titleOfSection, int color) {
        ClassificationChartAccountNumber = classificationChartAccountNumber;
        ClassificationName = classificationName;
        ClassificationType = classificationType;
        GrossTotal = grossTotal;
        this.isSectionItem = isSectionItem;
        this.titleOfSection = titleOfSection;
        this.color = color;
    }

    public ModalRevenueExpense(Integer classificationChartAccountNumber, String classificationName, String classificationType, double grossTotal, boolean isSectionItem, String titleOfSection) {
        ClassificationChartAccountNumber = classificationChartAccountNumber;
        ClassificationName = classificationName;
        ClassificationType = classificationType;
        GrossTotal = grossTotal;
        this.isSectionItem = isSectionItem;
        this.titleOfSection = titleOfSection;
    }

    public ModalRevenueExpense() {

    }

    public int getColor() {
        if(color == 0){
            return Color.parseColor("#296FC0");
        }else{
            return color;
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSectionItem() {
        return isSectionItem;
    }

    public void setSectionItem(boolean sectionItem) {
        isSectionItem = sectionItem;
    }

    public String getTitleOfSection() {
        return titleOfSection;
    }

    public void setTitleOfSection(String titleOfSection) {
        this.titleOfSection = titleOfSection;
    }

    private String titleOfSection;


    public Integer getClassificationChartAccountNumber() {
        return ClassificationChartAccountNumber;
    }

    public void setClassificationChartAccountNumber(Integer classificationChartAccountNumber) {
        ClassificationChartAccountNumber = classificationChartAccountNumber;
    }

    public String getClassificationName() {
        return ClassificationName;
    }

    public void setClassificationName(String classificationName) {
        ClassificationName = classificationName;
    }

    public String getClassificationType() {
        return ClassificationType;
    }

    public void setClassificationType(String classificationType) {
        ClassificationType = classificationType;
    }

    public double getGrossTotal() {
        return GrossTotal;
    }

    public void setGrossTotal(double grossTotal) {
        GrossTotal = grossTotal;
    }

    /*public int compare(ModalRevenueExpense left, ModalRevenueExpense right) {
        return left.ClassificationChartAccountNumber.compareTo(right.ClassificationChartAccountNumber);
    }*/
}
