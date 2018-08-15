package com.kippinretail.Modal.webclient;

import android.content.Context;

import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.Modal.webclient.model.TemplateData;


/**
 * Created by gaganpreet.singh on 2/4/2016.
 */
public class WSUtils {


    public static void hitService(Context context, String url , ArrayListPost templatePosts ,  WSMethods methods,Class<?extends TemplateData> datatype, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.methods = methods;
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.aClass = datatype;
        wsTemplate.url = url;
        WSHandler wsHandler = new WSHandler(wsTemplate);
        wsHandler.execute();
    }

    public static void hitServiceGet(Context context, String url , ArrayListPost templatePosts ,  Class<?extends TemplateData> datatype, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.methods = WSMethods.GET;
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.aClass = datatype;
        wsTemplate.url = url;
        WSHandler wsHandler = new WSHandler(wsTemplate);
        wsHandler.execute();
    }

    public static void hitService(Context context, String url , boolean isFormData,ArrayListPost templatePosts ,  WSMethods methods,Class<?extends TemplateData> datatype, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.methods = methods;
        wsTemplate.isFormData = isFormData;
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.aClass = datatype;
        wsTemplate.url = url;
        WSHandler wsHandler = new WSHandler(wsTemplate);
        wsHandler.execute();
    }
}
