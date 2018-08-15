package com.kippin.connection.connectionOption;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kippin.connection.ActivityConnection;
import com.kippin.connection.connector.OnConnectionOptionSelected;
import com.kippin.kippin.R;
import com.kippin.superviews.listeners.CommonCallbacks;

/**
 * Created by gaganpreet.singh on 3/15/2016.
 */
public class FragmentConnectionOptions extends Fragment implements CommonCallbacks, View.OnClickListener {

    View view;

    ImageView ivDownload, ivUpload;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_connection_options, container, false);

        initialiseUI();

        setUpListeners();

        return view;
    }

    @Override
    public void initialiseUI() {

        ivDownload = (ImageView) view.findViewById(R.id.ivDownload);

        ivUpload = (ImageView) view.findViewById(R.id.ivUpload);

    }

    @Override
    public void setUpListeners() {

        ivUpload.setOnClickListener(this);
        ivDownload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        OnConnectionOptionSelected option = (ActivityConnection) getActivity();
        int id = v.getId();
        if(id==R.id.ivUpload){
            option.onUploadSelected();
        }else if(id == R.id.ivDownload){
            option.onDownloadSelected();
        }

        /*switch (v.getId()) {
            case R.id.ivUpload:
                option.onUploadSelected();
                break;
            case R.id.ivDownload:
                option.onDownloadSelected();
                break;

        }*/
    }
}
