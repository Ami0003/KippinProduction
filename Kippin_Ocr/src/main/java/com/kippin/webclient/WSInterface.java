package com.kippin.webclient;


import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.automatic.ModalAutomaticStatementAccountList;

public interface WSInterface {
	void onResult(int requestCode, TemplateData data);

}
