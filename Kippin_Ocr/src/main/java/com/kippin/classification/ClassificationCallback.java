package com.kippin.classification;

import com.kippin.webclient.model.ModelClassification;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public interface ClassificationCallback {
    public void onClassificatonSelected(ModelClassification modelClassification, int position);
}
