package com.uxi.bambupay.view.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.dialog_message_result.*

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class SuccessDialog(
    private val ctx: Context,
    private val message: String?,
    private val amount: String?,
    private val date: String?) : Dialog(ctx) {

    private lateinit var clickListener: OnSuccessDialogClickListener

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_message_result)
        initViews()
    }

    private fun initViews() {
        message?.let {
            text_success_message.text = it
        }

        amount?.let {
            text_amount.text = ctx.getString(R.string.amount_value, it)
        }

        date?.let {
            text_date.text = it
        }

        btn_new_transaction.setOnClickListener {
            if (this::clickListener.isInitialized) {
                dismiss()
                clickListener.onNewClicked()
            }
        }

        btn_okay.setOnClickListener {
            if (this::clickListener.isInitialized) {
                dismiss()
                clickListener.onDashBoardClicked()
            }
        }
    }

    interface OnSuccessDialogClickListener {
        fun onDashBoardClicked()
        fun onNewClicked()
    }

    fun setOnSuccessDialogClickListener(listener: OnSuccessDialogClickListener) {
        clickListener = listener
    }

}