package com.uxi.bambupay.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText
import com.uxi.bambupay.R

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class PinTextView(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {

    private var mSpace = 24f //24 dp by default, space between the lines
    private var mCharSize = 0f
    private var mNumChars = 4f
    private var mLineSpacing = 8f //8dp by default, height of the text from our lines
    private var mMaxLength = 4
    private var mClickListener: OnClickListener? = null
    private var mLineStroke = 2f //1dp by default
    private var mLineStrokeSelected = 2f //2dp by default
    private var mLinesPaint: Paint? = null
    private var mContext: Context? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PinTextView)

        mContext = context
        val multi = context.resources.displayMetrics.density
        mLineStroke = multi * mLineStroke
        mLineStrokeSelected = multi * mLineStrokeSelected
        mLinesPaint = Paint(paint)
        mLinesPaint!!.strokeWidth = mLineStroke
        setBackgroundResource(0)
        mSpace = multi * mSpace //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing //convert to pixels for our density
        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4)
        mNumChars = mMaxLength.toFloat()

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onCreateActionMode(
                mode: ActionMode,
                menu: Menu
            ): Boolean {
                return false
            }

            override fun onPrepareActionMode(
                mode: ActionMode,
                menu: Menu
            ): Boolean {
                return false
            }

            override fun onActionItemClicked(
                mode: ActionMode,
                item: MenuItem
            ): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        })
        // When tapped, move cursor to end of text.
        super.setOnClickListener { v ->
            setSelection(text!!.length)
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }

        attributes.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas?) {
        val availableWidth = width - paddingRight - paddingLeft
        mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }
        var startX = paddingLeft
        val bottom = height - paddingBottom

        //Text Width
        val text = text
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(getText(), 0, textLength, textWidths)
        paint.color = mContext!!.resources.getColor(R.color.black) // Change the TextColor
        var i = 0
        while (i < mNumChars) {
            updateColorForLines(i == textLength, textLength, i)
            canvas?.drawLine(
                startX.toFloat(),
                bottom.toFloat(),
                startX + mCharSize,
                bottom.toFloat(),
                mLinesPaint!!
            )
            if (getText()!!.length > i) {
                val middle = startX + mCharSize / 2
                canvas?.drawText(
                    text,
                    i,
                    i + 1,
                    middle - textWidths[0] / 2,
                    bottom - mLineSpacing,
                    paint
                )
            }
            if (mSpace < 0) {
                startX += mCharSize.toInt() * 2
            } else {
                startX += mCharSize.toInt() + mSpace.toInt()
            }
            i++
        }
    }

    /**
     * @param isCurrent Is the current char the next character to be input?
     */
    private fun updateColorForLines(
        isCurrent: Boolean,
        textLength: Int,
        index: Int
    ) {
        if (isFocused) {
            mLinesPaint!!.strokeWidth = mLineStrokeSelected
            mLinesPaint!!.color = mContext!!.resources.getColor(R.color.ash_grey)
            if (isCurrent) {
                mLinesPaint!!.color = mContext!!.resources.getColor(R.color.ash_grey)
            } else {
                if (index == 0 && textLength > 0) {
                    mLinesPaint!!.color = mContext!!.resources.getColor(R.color.colorPrimaryDark)
                } else if (index == 1 && textLength > 1) {
                    mLinesPaint!!.color = mContext!!.resources.getColor(R.color.colorPrimaryDark)
                } else if (index == 2 && textLength > 2) {
                    mLinesPaint!!.color = mContext!!.resources.getColor(R.color.colorPrimaryDark)
                } else if (index == 3 && textLength > 3) {
                    mLinesPaint!!.color = mContext!!.resources.getColor(R.color.colorPrimaryDark)
                }
            }
        } else {
            mLinesPaint!!.strokeWidth = mLineStroke
            mLinesPaint!!.color = mContext!!.resources.getColor(R.color.ash_grey)
        }
    }

    companion object {
        const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
    }

}