package com.zhlt.g1app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhlt.g1app.R;

public class FrgResting extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View v = inflater.inflate(R.layout.act_conn_phone, container, false);
    	return v;
    }

}
