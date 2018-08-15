package com.kippin.webclient;


import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.automatic.ModalAutomaticStatementAccountList;

public interface WS_Interface {

	void on_Result(int requestCode, ModalAutomaticStatementAccountList modalAutomaticStatement_AccountList);


}
