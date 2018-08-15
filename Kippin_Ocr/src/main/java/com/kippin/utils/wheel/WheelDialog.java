package com.kippin.utils.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kippin.WheelView.WheelView;

import java.util.ArrayList;

/**
 * Created by gaganpreet.singh on 1/28/2016.
 */
public class WheelDialog {

    public static void showWheelDialog(Context context , final ArrayList<String> PLANETS,int selection, final WheelView.OnWheelViewListener onWheelViewListener){

        ListView wv  = new ListView(context);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,PLANETS);
        wv.setAdapter(stringArrayAdapter);


        final AlertDialog alertDialog = new AlertDialog.Builder(context)
//                .setTitle("WheelView in Dialog")
                .setView(wv)
//                .setPositiveButton("OK", null)
                .show();

        wv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onWheelViewListener.onSelected(position, PLANETS.get(position));
                alertDialog.dismiss();
            }
        });


    }





}
