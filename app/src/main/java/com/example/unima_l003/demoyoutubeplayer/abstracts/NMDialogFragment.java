package com.example.unima_l003.demoyoutubeplayer.abstracts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Striker
 * Created by vinhn_000 on 2/25/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
public abstract class NMDialogFragment extends DialogFragment {

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract View initRootView(LayoutInflater inflater,
                                         ViewGroup container, Bundle savedInstanceState);

    protected abstract void initUI(View view, Bundle savedInstanceState);

    protected abstract void loadData(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initRootView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void show(Context context) {
        if (context instanceof AppCompatActivity) {
            show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }
    }
}
