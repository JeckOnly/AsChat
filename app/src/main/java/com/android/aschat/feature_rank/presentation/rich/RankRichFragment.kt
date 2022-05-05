package com.android.aschat.feature_rank.presentation.rich

import android.graphics.Rect
import android.media.Image
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
import com.android.aschat.feature_rank.domain.model.rich.RankRichItem
import com.android.aschat.feature_rank.presentation.RankEvents
import com.android.aschat.feature_rank.presentation.RankViewModel
import com.android.aschat.util.DensityUtil
import com.android.aschat.util.FontUtil
import com.android.aschat.util.equilibriumAssignmentOfLinear
import com.android.aschat.util.setImageUrl
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup

class RankRichFragment : Fragment() {

    private val mViewModel: RankViewModel by activityViewModels()
    private lateinit var mRv: RecyclerView
    private lateinit var mAdapter: BindingAdapter
    private lateinit var mRankMeSort: TextView
    private lateinit var mRankMeAvatar: ImageView
    private lateinit var mRankMeNickname: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel.onEvent(RankEvents.LoadRankRichData)
        return inflater.inflate(R.layout.rank_rich_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidget()
    }

    private fun initWidget() {
        mRv = requireView().findViewById(R.id.rich_rv)
        mRankMeSort = requireView().findViewById(R.id.rank_me_sort)
        mRankMeAvatar = requireView().findViewById(R.id.rank_me_avatar)
        mRankMeNickname = requireView().findViewById(R.id.rank_me_nickname)

        mAdapter = mRv.linear().apply {
            addItemDecoration(object : RecyclerView.ItemDecoration() {
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
            addType<RankRichItem>(R.layout.rank_rv_item)
            onBind {
                val model = getModel<RankRichItem>()
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

        mViewModel.apply {
            rankRichData.observe(viewLifecycleOwner) {
                mAdapter.models = it.rankData
                if (it.sortNo == "" || it.sortNo.toLong() > 50) {
                    mRankMeSort.text = "50+"
                }else {
                    mRankMeSort.text = it.sortNo
                    if (it.sortNo.toLong() <= 3) {
                        mRankMeSort.setTextColor(resources.getColor(R.color.purple, null))
                    }else {
                        mRankMeSort.setTextColor(resources.getColor(R.color.black_3, null))
                    }
                }
                mRankMeSort.typeface = FontUtil.getTypeface(requireContext())
            }

            userInfoMoreDetailed.observe(viewLifecycleOwner) {
                setImageUrl(mRankMeAvatar, it.avatarUrl)
                mRankMeNickname.text = it.nickname
            }
        }
    }
}