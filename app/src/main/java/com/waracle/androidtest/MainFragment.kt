package com.waracle.androidtest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var adapter: CakeAdapter
    private lateinit var viewModel: CakeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CakeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager((activity))
            addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CakeAdapter().apply {
            setHasStableIds(true)
        }

        main_recyclerview.adapter = adapter

        viewModel.getCakes()?.observe(viewLifecycleOwner, Observer {
            it?.run {
                adapter.data = this.toMutableList()
            }
        })
    }
}