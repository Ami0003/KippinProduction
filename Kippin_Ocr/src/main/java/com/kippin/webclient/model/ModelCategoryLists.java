package com.kippin.webclient.model;

import android.content.Context;


import com.kippin.classification.AdapterCategory;
import com.kippin.utils.Url;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.category.OnCategoryLoaded;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class ModelCategoryLists extends TemplateData{

    private  static  ModelCategoryLists instance =null;

    private ModelCategoryLists(){
    }

    public static ModelCategoryLists getInstance(){
        if(instance==null){
         instance = new ModelCategoryLists();
        }
         return instance;
    }

    public ArrayList<ModelCategoryList> modelCategoryLists;

    public boolean isDataPresent(){
        return modelCategoryLists!=null;
    }

    public void loadData(final Context context , final OnCategoryLoaded onCategoryLoaded){
        if(modelCategoryLists!=null){
            onCategoryLoaded.onCategoryLoaded(modelCategoryLists);
        }else loadFreshData(context,onCategoryLoaded);
    }


    public void loadFreshData(final Context context , final OnCategoryLoaded onCategoryLoaded){
        WSUtils.hitServiceGet(context, Url.URL_CATEGORY, new ArrayListPost(), ModelCategoryLists.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {

                ModelCategoryList modelCategoryList = new ModelCategoryList();
                modelCategoryList.setCategoryType("Please Select");
                modelCategoryList.setId(1+"");

                modelCategoryLists = ((ModelCategoryLists) data.getData(ModelCategoryLists.class)).modelCategoryLists;

                modelCategoryLists.add(0,modelCategoryList);


                onCategoryLoaded.onCategoryLoaded(modelCategoryLists);
            }
        });
    }


}
