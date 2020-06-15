package com.braintreepayments.cardform.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;

/**
 * A {@link android.text.style.ReplacementSpan} used for spacing in {@link android.widget.EditText}
 * to space things out. Adds ' 's
 */
public class SpaceSpan extends ReplacementSpan {

    private String separator = " - ";

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
        float padding = paint.measureText(separator, 0, separator.length());
        float textSize = paint.measureText(text, start, end);
        return (int) (padding + textSize);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
            int bottom, Paint paint) {
        canvas.drawText(text.subSequence(start, end) + separator, x, y, paint);
    }
}
