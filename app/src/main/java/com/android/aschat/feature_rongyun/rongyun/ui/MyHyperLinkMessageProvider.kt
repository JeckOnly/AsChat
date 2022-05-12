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
import com.android.aschat.R
import com.android.aschat.common.Constants
import com.android.aschat.feature_rongyun.MyConversationActivity
import com.android.aschat.feature_rongyun.rongyun.custom_message_kind.HyperLinkMessage
import com.android.aschat.feature_rongyun.rongyun.model.ExtraInfo
import com.android.aschat.util.JsonUtil
import com.android.aschat.util.LogUtil
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
import java.util.*

class MyHyperLinkMessageProvider: BaseMessageItemProvider<HyperLinkMessage>() {

    override fun getSummarySpannable(p0: Context?, message: HyperLinkMessage?): Spannable {
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

    override fun onCreateMessageContentViewHolder(parent: ViewGroup?, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent!!.context)
            .inflate(R.layout.rc_hyperlink_message_item_custom, parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun bindMessageContentViewHolder(
        holder: ViewHolder?,
        parentHolder: ViewHolder?,
        message: HyperLinkMessage?,
        uiMessage: UiMessage?,
        position: Int,
        list: MutableList<UiMessage>?,
        listener: IViewProviderListener<UiMessage>?
    ) {
        val contentTv = holder!!.getView<TextView>(R.id.rc_hyper_text)
        if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
            contentTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        contentTv.tag = uiMessage?.getMessageId()
        if (uiMessage?.getContentSpannable() == null) {
            val spannable = TextViewUtils.getSpannable(
                message?.content
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
        contentTv.setOnClickListener {
            // NOTE 充值逻辑
            LogUtil.d("点击充值")
            if (message == null) return@setOnClickListener
            if (message.contentType == Constants.Recharge_Link) {
                val extraInfo = JsonUtil.json2Any(message.extraInfo, ExtraInfo::class.java)
                LogUtil.d(it.context::class.java.simpleName)
                val activity = it.context as MyConversationActivity
                activity.whenClickRechargeLink(extraInfo = extraInfo)
            }
        }
        contentTv.setOnLongClickListener { contentTv ->
            val parent = contentTv.parent
            if (parent is View) (parent as View).performLongClick() else false
        }
    }

    override fun onItemClick(
        holder: ViewHolder?,
        message: HyperLinkMessage?,
        uiMessage: UiMessage?,
        position: Int,
        list: MutableList<UiMessage>?,
        listener: IViewProviderListener<UiMessage>?
    ): Boolean {
        return true
    }

    override fun isMessageViewType(messageContent: MessageContent?): Boolean {
        return messageContent is HyperLinkMessage
    }
}