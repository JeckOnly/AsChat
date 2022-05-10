package com.android.aschat.feature_rongyun.rongyun.ui

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.TextUtilsCompat
import androidx.lifecycle.MutableLiveData
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.common.MyApplication
import com.android.aschat.common.network.translate.Translate
import com.android.aschat.feature_rongyun.rongyun.common.NowHost
import com.android.aschat.util.LogUtil
import com.android.aschat.util.SpConstants
import com.android.aschat.util.SpUtil
import io.rong.imkit.config.RongConfigCenter
import io.rong.imkit.conversation.extension.component.emoticon.AndroidEmoji
import io.rong.imkit.conversation.messgelist.provider.BaseMessageItemProvider
import io.rong.imkit.model.UiMessage
import io.rong.imkit.utils.RouteUtils
import io.rong.imkit.utils.TextViewUtils
import io.rong.imkit.widget.LinkTextViewMovementMethod
import io.rong.imkit.widget.adapter.IViewProviderListener
import io.rong.imkit.widget.adapter.ViewHolder
import io.rong.imlib.model.MessageContent
import io.rong.message.TextMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import kotlin.collections.LinkedHashMap

class MyTextMessageProvider:  BaseMessageItemProvider<TextMessage>(){

    private val mHasTranslateMap: LinkedHashMap<String, String> by lazy {
        LinkedHashMap(50)
    }

    override fun getSummarySpannable(p0: Context?, message: TextMessage?): Spannable {
        return if (message != null && !TextUtils.isEmpty(message.content)) {
            var content: String = message.content
            content = content.replace("\n", " ")
            if (content.length > 100) {
                content = content.substring(0, 100)
            }
            SpannableString(AndroidEmoji.ensure(content))
        } else {
            SpannableString("")
        }
    }

    override fun onCreateMessageContentViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent!!.context)
            .inflate(R.layout.rc_text_message_item_custom, parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun bindMessageContentViewHolder(
        holder: ViewHolder?,
        parentHolder: ViewHolder?,
        message: TextMessage?,
        uiMessage: UiMessage?,
        position: Int,
        list: MutableList<UiMessage>?,
        listener: IViewProviderListener<UiMessage>?
    ) {
        LogUtil.d("position $position")
        val contentTv = holder!!.getView<TextView>(R.id.rc_text)
        val translateTv = holder.getView<TextView>(R.id.translate_tv)
        if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
            contentTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }

        contentTv.tag = uiMessage?.getMessageId()
        if (uiMessage?.getContentSpannable() == null) {
            val spannable = TextViewUtils.getSpannable(
                message?.getContent()
            ) { spannable ->
                uiMessage?.setContentSpannable(spannable)
                if (contentTv.tag == uiMessage?.getMessageId()) {
                    contentTv.post { contentTv.setText(uiMessage?.getContentSpannable()) }
                }
            }
            uiMessage?.setContentSpannable(spannable)
        }

        contentTv.setText(uiMessage?.getContentSpannable())
        contentTv.movementMethod = LinkTextViewMovementMethod { link ->
            var result = false
            if (RongConfigCenter.conversationConfig().conversationClickListener != null) {
                result =
                    RongConfigCenter.conversationConfig().conversationClickListener.onMessageLinkClick(
                        holder.getContext(),
                        link,
                        uiMessage?.getMessage()
                    )
            }
            if (result) {
                true
            } else {
                val str = link.toLowerCase()
                if (str.startsWith("http") || str.startsWith("https")) {
                    RouteUtils.routeToWebActivity(contentTv.context, link)
                    result = true
                }
                result
            }
        }
        contentTv.setOnClickListener { contentTv ->
            val parent = contentTv.parent
            if (parent is View) {
                (parent as View).performClick()
            }
        }
        contentTv.setOnLongClickListener { contentTv ->
            val parent = contentTv.parent
            if (parent is View) (parent as View).performLongClick() else false
        }
        setTranslateTv(translateTv, contentTv, uiMessage?.userInfo?.userId?:"")
    }

    override fun onItemClick(
        p0: ViewHolder?,
        p1: TextMessage?,
        p2: UiMessage?,
        p3: Int,
        p4: MutableList<UiMessage>?,
        p5: IViewProviderListener<UiMessage>?
    ): Boolean {
        LogUtil.d("click message")
        return false
    }

    override fun isMessageViewType(messageContent: MessageContent?): Boolean {
        return messageContent is TextMessage && !messageContent.isDestruct()
    }

    private fun setTranslateTv(translateTv: TextView, contentTv: TextView, userId: String) {
        val originalText = contentTv.text.toString()
        if (userId == NowHost.hostId) {
            // 主播的信息
            translateTv.visibility = View.VISIBLE
            if (mNeedAutoTranslate) {
                // 自动翻译
                CoroutineScope(Dispatchers.Main).launch {
                    translateTv.visibility = View.GONE
                    val translatedText = withContext(Dispatchers.IO) {
                        try {
                            return@withContext Translate.getTrans(contentTv.text.toString())
                        }catch (e: Exception) {
                            return@withContext contentTv.text.toString()
                        }
                    }
                    contentTv.text = translatedText
                }
            }else {
                // 不自动翻译
                if (mHasTranslateMap.containsKey(originalText)) {
                    // 已经翻译过
                    CoroutineScope(Dispatchers.Main).launch {
                        translateTv.visibility = View.GONE
                        val translatedText = withContext(Dispatchers.IO) {
                            try {
                                return@withContext Translate.getTrans(contentTv.text.toString())
                            }catch (e: Exception) {
                                return@withContext contentTv.text.toString()
                            }
                        }
                        contentTv.text = translatedText
                    }
                }else {
                    // 没有翻译过
                    translateTv.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            val translatedText = withContext(Dispatchers.IO) {
                                try {
                                    return@withContext Translate.getTrans(contentTv.text.toString())
                                }catch (e: Exception) {
                                    return@withContext contentTv.text.toString()
                                }
                            }
                            translateTv.visibility = View.GONE
                            contentTv.text = translatedText
                            }
                        }
                    }
                }
        }else {
            // 自己的信息
            translateTv.visibility = View.GONE
        }
    }

    companion object {
        private var mNeedAutoTranslate = SpUtil.get(MyApplication.application, SpConstants.Auto_Translate, false) as Boolean

        fun setNeedAutoTranslate(needAuto: Boolean) {
            mNeedAutoTranslate = needAuto
        }
    }
}