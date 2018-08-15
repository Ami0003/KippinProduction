package com.kippin.webclient.model;

import java.io.Serializable;

/**
 * Created by gaganpreet.singh on 4/2/2016.
 */
public class ModelDebitCredit implements Serializable{
    private String ClassificationType;

    private double Credit;

    private double Debit;

    public String getClassificationType ()
    {
        return ClassificationType;
    }

    public void setClassificationType (String ClassificationType)
    {
        this.ClassificationType = ClassificationType;
    }

    public double getCredit ()
    {
        return Credit;
    }

    public void setCredit (double Credit)
    {
        this.Credit = Credit;
    }

    public double getDebit ()
    {
        return Debit;
    }

    public void setDebit (double Debit)
    {
        this.Debit = Debit;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ClassificationType = "+ClassificationType+", Credit = "+Credit+", Debit = "+Debit+"]";
    }
}
