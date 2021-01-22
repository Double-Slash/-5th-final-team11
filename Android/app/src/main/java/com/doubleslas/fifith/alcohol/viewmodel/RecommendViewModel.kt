package com.doubleslas.fifith.alcohol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doubleslas.fifith.alcohol.enum.RecommendSortType
import com.doubleslas.fifith.alcohol.model.network.base.ApiLiveData
import com.doubleslas.fifith.alcohol.model.network.dto.AlcoholSimpleData
import com.doubleslas.fifith.alcohol.model.repository.RecommendRepository
import com.doubleslas.fifith.alcohol.viewmodel.base.PageLoader

class RecommendViewModel(private val category: String) : ViewModel() {
    private val repository = RecommendRepository()
    private val pageLoader = PageLoader<AlcoholSimpleData>()
    val listLiveData: ApiLiveData<List<AlcoholSimpleData>> = pageLoader.liveData

    var sort = globalSortType
        private set

    fun initList(): Boolean {
        if (pageLoader.isInit() || sort != globalSortType) {
            loadList()
            return true
        }
        return false
    }

    fun loadList() {
        if (sort != globalSortType) {
            setSort(globalSortType)
            return
        }
        if (!pageLoader.canLoadList()) return

        val liveData = repository.getList(category, sort)
        pageLoader.addObserve(liveData)
    }

    fun setSort(sortType: RecommendSortType) {
        if (sort == sortType) return
        sort = sortType
        globalSortType = sortType
        pageLoader.reset()
        loadList()
    }

    class Factory(private val param: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(RecommendViewModel::class.java)) {
                RecommendViewModel(param) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    companion object {
        private var globalSortType: RecommendSortType = RecommendSortType.Recommend
    }
}