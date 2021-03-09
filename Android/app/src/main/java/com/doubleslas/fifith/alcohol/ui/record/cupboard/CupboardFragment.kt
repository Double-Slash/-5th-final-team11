package com.doubleslas.fifith.alcohol.ui.record.cupboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.doubleslas.fifith.alcohol.databinding.RecyclerviewBinding
import com.doubleslas.fifith.alcohol.model.base.ApiStatus
import com.doubleslas.fifith.alcohol.ui.common.LoadingRecyclerViewAdapter
import com.doubleslas.fifith.alcohol.ui.common.LoadingRecyclerViewAdapter.Companion.VIEW_TYPE_LOADING
import com.doubleslas.fifith.alcohol.ui.common.base.BaseFragment

class CupboardFragment : BaseFragment<RecyclerviewBinding>() {
    private val viewModel by lazy { ViewModelProvider(this).get(CupboardViewModel::class.java) }
    private val adapter by lazy { CupboardAdapter() }
    private val loadingAdapter by lazy { LoadingRecyclerViewAdapter(adapter) }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): RecyclerviewBinding {
        return RecyclerviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let {
            val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (loadingAdapter.getItemViewType(position) == VIEW_TYPE_LOADING) {
                        return layoutManager.spanCount
                    }
                    return 1
                }
            }

            it.recyclerview.layoutManager = layoutManager
            it.recyclerview.adapter = loadingAdapter
        }

        loadingAdapter.setOnBindLoadingListener {
            viewModel.loadCupboardList()
        }


        viewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiStatus.Success -> {
//                    loadingAdapter.setVisibleLoading(!viewModel.isFinish())
                    adapter.setData(it.data)
                    loadingAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}