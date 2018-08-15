package com.kippin.bankstatement;

/**
 * Created by gaganpreet.singh on 2/19/2016.
 */
public interface BankStatementListener {

    public  void  onViewClick(int position);
    public  void  onEditClick(int position);
    public  void onDeleteClick(int position);
}
