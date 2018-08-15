package com.kippin.connection.banks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.kippin.connection.Banks;
import com.kippin.kippin.R;
import com.kippin.superviews.listeners.CommonCallbacks;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by gaganpreet.singh on 3/15/2016.
 */
public class FragmentBanks extends Fragment implements CommonCallbacks{

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_banks, container, false);

        initialiseUI();

        setUpListeners();

        return view;
    }

    @Override
    public void initialiseUI() {
    }

    @Override
    public void setUpListeners() {
    }


}
