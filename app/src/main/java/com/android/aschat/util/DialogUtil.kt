package com.android.aschat.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.android.aschat.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtil {
    /**
     * 一级举报弹窗
     */
    fun createFirstBlockDialog(
        context: Context,
        isFollow: Boolean,
        isBlocked: Boolean,
        clickFollow: (dialog: Dialog) -> Unit,
        clickBlock: (dialog: Dialog) -> Unit,
        clickReport: (dialog: Dialog) -> Unit,
        clickCancel: (dialog: Dialog) -> Unit,
    ): Dialog {
        val dialog: BottomSheetDialog = BottomSheetDialog(context)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        adaptBottomSheetDialogShow(dialog)
        // contentView
        val contentView = View.inflate(context, R.layout.dialog_block_first, null)
        dialog.setContentView(contentView)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        if (isFollow) {
            contentView.findViewById<TextView>(R.id.dialog_block_first_follow_text).text = context.getString(R.string.unfollow)
        }else {
            contentView.findViewById<TextView>(R.id.dialog_block_first_follow_text).text = context.getString(R.string.Follow)
        }
        if (isBlocked) {
            contentView.findViewById<TextView>(R.id.dialog_block_first_block_text).text = context.getString(R.string.unBlock)
        }else {
            contentView.findViewById<TextView>(R.id.dialog_block_first_block_text).text = context.getString(R.string.Block)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_first_follow).setOnClickListener {
            clickFollow(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_first_block).setOnClickListener {
            clickBlock(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_first_report).setOnClickListener {
            clickReport(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_first_cancel).setOnClickListener {
            clickCancel(dialog)
        }
        return dialog
    }

    /**
     * 二级举报弹窗
     */
    fun createSecondBlockDialog(
        context: Context,
        isBlocked: Boolean,
        clickPorngraphic: (dialog: Dialog) -> Unit,
        clickFalsegender: (dialog: Dialog) -> Unit,
        clickFraud: (dialog: Dialog) -> Unit,
        clickBlock: (dialog: Dialog) -> Unit,
        clickReport: (dialog: Dialog) -> Unit,
        clickCancel: (dialog: Dialog) -> Unit,
    ): Dialog {
        val dialog: BottomSheetDialog = BottomSheetDialog(context)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        adaptBottomSheetDialogShow(dialog)
        // contentView
        val contentView = View.inflate(context, R.layout.dialog_block_second, null)
        dialog.setContentView(contentView)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        if (isBlocked) {
            contentView.findViewById<TextView>(R.id.dialog_block_second_Block_text).text = context.getString(R.string.unBlock)
        }else {
            contentView.findViewById<TextView>(R.id.dialog_block_second_Block_text).text = context.getString(R.string.Block)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_Porngraphic).setOnClickListener {
            clickPorngraphic(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_Falsegenderblock).setOnClickListener {
            clickFalsegender(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_Fraud).setOnClickListener {
            clickFraud(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_Block).setOnClickListener {
            clickBlock(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_Report).setOnClickListener {
            clickReport(dialog)
        }
        contentView.findViewById<LinearLayout>(R.id.dialog_block_second_cancel).setOnClickListener {
            clickCancel(dialog)
        }
        return dialog
    }

    /**
     * 适配BottomSheetDialog显示不全的问题
     *
     * @param dialog
     */
    private fun adaptBottomSheetDialogShow(dialog: BottomSheetDialog) {
        dialog.setOnShowListener { dialog1: DialogInterface ->
            val d = dialog1 as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                    ?: return@setOnShowListener
            bottomSheet.setBackgroundResource(android.R.color.transparent)
            val coordinatorLayout = bottomSheet.parent as CoordinatorLayout
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = bottomSheet.height
            coordinatorLayout.parent.requestLayout()
        }
    }

    /**
     * loading--dialog
     *
     * @param acty
     * @return
     */
    fun createLoadingDialog(context: Context): Dialog {
        val dialog: Dialog = Dialog(context)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val contentView = View.inflate(context, R.layout.dialog_loading, null)
        dialog.setContentView(contentView)

        val progressBar = contentView.findViewById<ProgressBar>(R.id.dialog_loading_progress)

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}