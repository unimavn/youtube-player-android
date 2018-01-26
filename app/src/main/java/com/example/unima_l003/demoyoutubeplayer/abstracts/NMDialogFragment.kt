package com.example.unima_l003.demoyoutubeplayer.abstracts

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

/**
 * Striker
 * Created by vinhn_000 on 2/25/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
abstract class NMDialogFragment : DialogFragment() {

    protected abstract fun initData(savedInstanceState: Bundle?)

    protected abstract fun initRootView(inflater: LayoutInflater?,
                                        container: ViewGroup?, savedInstanceState: Bundle?): View

    protected abstract fun initUI(view: View?, savedInstanceState: Bundle?)

    protected abstract fun loadData(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            initRootView(inflater, container, savedInstanceState)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadData(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    fun show(context: Context) {
        if (context is AppCompatActivity) {
            show(context.supportFragmentManager, null)
        }
    }
}
