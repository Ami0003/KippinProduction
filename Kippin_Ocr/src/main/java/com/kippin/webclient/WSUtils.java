package com.kippin.webclient;

import android.content.Context;
import android.util.Log;

import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.TemplateData;

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

    public static void hit_Service(Context context, String url , ArrayListPost templatePosts ,  WSMethods methods,Class<?extends TemplateData> datatype, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.methods = methods;
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.aClass = datatype;
        wsTemplate.url = url;

        Log.e("URL:",""+wsTemplate.url);

       // WSHandler wsHandler = new WSHandler(wsTemplate);

       // wsHandler.execute();
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

    public static void hitServiceGetAuto(Context context, String url  ,  Class<?extends TemplateData> datatype, WS_Interface wsInterface) {
        Log.e("url:",""+url);
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.ws_interface= wsInterface;
        wsTemplate.methods = WSMethods.GET;
        //wsTemplate.templatePosts = templatePosts;
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

    public static void hitServiceGetXML(Context context, String url , ArrayListPost templatePosts ,  Class<?extends TemplateData> datatype, WSInterface wsInterface) {
        WSTemplate wsTemplate = new WSTemplate();
        wsTemplate.context = context;
        wsTemplate.wsInterface = wsInterface;
        wsTemplate.methods = WSMethods.XML_PARSING;
        wsTemplate.templatePosts = templatePosts;
        wsTemplate.aClass = datatype;
        wsTemplate.url = url;
        WSHandler wsHandler = new WSHandler(wsTemplate);
        wsHandler.execute();
    }
}
