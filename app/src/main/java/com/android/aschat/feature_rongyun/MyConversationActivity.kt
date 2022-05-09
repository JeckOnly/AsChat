package com.android.aschat.feature_rongyun

import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.android.aschat.R
import com.android.aschat.common.BaseActivity
import com.android.aschat.util.StatusBarUtil
import io.rong.imkit.conversation.ConversationFragment
import io.rong.imkit.conversation.ConversationViewModel
import io.rong.imkit.model.TypingInfo.TypingUserInfo
import io.rong.imkit.userinfo.RongUserInfoManager
import io.rong.imlib.model.Conversation
import java.util.*

/**
 * 自定义的ConversationActivity会话界面
 */
class MyConversationActivity : BaseActivity() {
    private var mTargetId: String? = null
    private var mConversationType: Conversation.ConversationType? = null
    private var mConversationFragment: ConversationFragment? = null
    private var conversationViewModel: ConversationViewModel? = null

    private lateinit var mBackImg: ImageView
    private lateinit var mTitleTv: TextView
    private lateinit var mTypingTv: TextView
    private lateinit var mVideoImg: ImageView
    private lateinit var mMoreImg: ImageView

    override fun provideLayoutId() = R.layout.rongyun_conversation_acty

    override fun init() {
        // 设置状态栏纯色
        StatusBarUtil.setColorNoTranslucent(this, resources.getColor(R.color.pink3))
        if (this.intent != null) {
            mTargetId = this.intent.getStringExtra("targetId")
            val type = this.intent.getStringExtra("ConversationType")
            if (TextUtils.isEmpty(type)) {
                return
            }
            mConversationType = Conversation.ConversationType.valueOf(type!!.toUpperCase(Locale.US))
        }
        loadFragment()
        setCustomTitleBar()
        initViewModel()
    }


    private fun initViewModel() {
        conversationViewModel = ViewModelProvider(this).get(
            ConversationViewModel::class.java
        )
        conversationViewModel!!.typingStatusInfo.observe(
            this
        ) { typingInfo ->
            if (typingInfo != null) {
                if (typingInfo.conversationType == mConversationType && mTargetId == typingInfo.targetId) {
                    if (typingInfo.typingList == null) {
                        mTitleTv.visibility = View.VISIBLE
                        mTypingTv.visibility = View.GONE
                    } else {
                        mTitleTv.visibility = View.GONE
                        mTypingTv.visibility = View.VISIBLE
                        val typing =
                            typingInfo.typingList[typingInfo.typingList.size - 1] as TypingUserInfo
                        if (typing.type == TypingUserInfo.Type.text) {
                            mTypingTv.text = getString(R.string.is_typing)
                        } else if (typing.type == TypingUserInfo.Type.voice) {
                            mTypingTv.text = getString(R.string.is_speaking)
                        }
                    }
                }
            }
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == event.keyCode && mConversationFragment != null && !mConversationFragment!!.onBackPressed()) {
            finish()
        }
        return false
    }

    /**
     * 自定义titlebar的相关设置
     */
    private fun setCustomTitleBar() {
        mBackImg = findViewById(R.id.conversation_back)
        mTitleTv = findViewById(R.id.conversation_title)
        mTypingTv = findViewById(R.id.conversation_title_typing)
        mVideoImg = findViewById(R.id.conversation_title_video)
        mMoreImg = findViewById(R.id.conversation_title_more)

        mBackImg.apply {
            setOnClickListener {
                if (mConversationFragment != null && !mConversationFragment!!.onBackPressed()) {
                    finish()
                }
            }
        }

        mTitleTv.apply {
            if (!TextUtils.isEmpty(mTargetId) && mConversationType == Conversation.ConversationType.GROUP) {
                val group = RongUserInfoManager.getInstance().getGroupInfo(mTargetId)
                text = (if (group == null) mTargetId else group.name)
            } else {
                val userInfo = RongUserInfoManager.getInstance().getUserInfo(mTargetId)
                text = (if (userInfo == null) mTargetId else userInfo.name)
            }
        }

        mVideoImg.setOnClickListener {

        }

        mMoreImg.setOnClickListener {

        }

    }

    private fun loadFragment() {
        mConversationFragment = ConversationFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.rongyun_conversation_fragment, mConversationFragment!!, mConversationFragment!!::class.java.simpleName)
            .addToBackStack(mConversationFragment!!::class.java.simpleName)
            .commitAllowingStateLoss()
    }
}