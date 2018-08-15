package com.kippinretail.Modal.webclient.model;

import android.widget.EditText;

import com.google.gson.Gson;

import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class ArrayListPost extends ArrayList<TemplatePost> {

    String json = null;

    public void setJson(String json){
        this.json = json;
    }


    public void setJson(Object json){
        Gson gson = new Gson();
        this.json = gson.toJson(json) ;
    }

    public boolean add(String key , EditText value) {
        return this.add(key,value.getText().toString());
    }

    public boolean add(String key , String value) {
        TemplatePost templatePost = new TemplatePost();
        templatePost.key = key;
        templatePost.value = value;
        return this.add(templatePost);
    }
    public boolean addInt(String key , int value) {
        TemplatePost templatePost = new TemplatePost();
        templatePost.key = key;
        templatePost.values = value;


        return this.add(templatePost);
    }

    public void add(int index, String key , EditText value) {
        this.add(index,key,value.getText().toString());
    }

    public void add(int index, String key , String value) {
        TemplatePost templatePost = new TemplatePost();
        templatePost.key = key;
        templatePost.value = value;
        this.add(index,templatePost);
    }

    public String getJson(){


        if(json!=null){
            return json ;
        }

    try{
        StringWriter stringBuilder = new StringWriter();
        stringBuilder.append("{");
        for(int i =0 ;i<size();i++){
            String data= "\"" +get(i).key  +"\"" +":" +"\"" +get(i).value  +"\"" ;
            stringBuilder.append(data);
            if(i<size()-1) stringBuilder.append(",");
        }

        stringBuilder.append("}");

        json = stringBuilder.toString();

        return json;

    }catch (Exception e){
        e.printStackTrace();
    }
    catch (Error e){
    e.printStackTrace();
    }
        return  "null-ArrayListPost.getJson()";
    }

}
