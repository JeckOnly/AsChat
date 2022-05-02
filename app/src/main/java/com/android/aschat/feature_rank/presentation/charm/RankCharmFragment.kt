package com.android.aschat.feature_rank.presentation.charm

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.aschat.R
import com.android.aschat.feature_rank.domain.model.charm.RankCharmItem
import com.android.aschat.feature_rank.presentation.RankEvents
import com.android.aschat.feature_rank.presentation.RankViewModel
import com.android.aschat.util.DensityUtil
import com.android.aschat.util.FontUtil
import com.android.aschat.util.equilibriumAssignmentOfLinear
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup

class RankCharmFragment : Fragment() {

    private val mViewModel: RankViewModel by activityViewModels()
    private lateinit var mRv: RecyclerView
    private lateinit var mAdapter: BindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel.onEvent(RankEvents.LoadRankCharmData)
        return inflater.inflate(R.layout.rank_charm_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidget()
    }

    private fun initWidget() {
        mRv = requireView().findViewById(R.id.charm_rv)
        mAdapter = mRv.linear().apply {
            addItemDecoration(object :RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    equilibriumAssignmentOfLinear(DensityUtil.dip2px(5f, requireContext()), outRect, view, parent)
                }
            })
        }.setup {
            addType<RankCharmItem>(R.layout.rank_rv_item)
            onBind {
                val model = getModel<RankCharmItem>()
                findView<TextView>(R.id.rank_sort).apply {
                    text = model.sort.toString()
                    if (model.sort <= 3) {
                        setTextColor(resources.getColor(R.color.purple, null))
                    }else {
                        setTextColor(resources.getColor(R.color.black_3, null))
                    }
                    typeface = FontUtil.getTypeface(requireContext())
                }

                findView<ImageView>(R.id.rank_avatar).apply {
                    load(model.avatar)
                }

                findView<TextView>(R.id.rank_nickname).apply {
                    text = model.nickname
                }
            }
        }

        mViewModel.rankCharmData.observe(viewLifecycleOwner) {
            mAdapter.models = it.rankCharmItems
        }
    }
}