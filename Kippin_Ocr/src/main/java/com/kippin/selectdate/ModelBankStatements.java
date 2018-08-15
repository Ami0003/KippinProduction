package com.kippin.selectdate;

import com.kippin.webclient.model.TemplateData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/28/2016.
 */
public class ModelBankStatements extends TemplateData implements Serializable{

    public ArrayList<ModelBankStatement> modelBankStatements = new ArrayList<>();

}

