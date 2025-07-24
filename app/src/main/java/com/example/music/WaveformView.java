package com.example.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {

    private byte[] waveform;
    private Paint paint = new Paint();

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0xFFFF362E);
        paint.setStrokeWidth(4f);
    }

    public void updateWaveform(byte[] waveform) {
        this.waveform = waveform;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (waveform == null || waveform.length == 0) return;

        int width = getWidth();
        int height = getHeight();

        int numberOfBars = 64;
        int step = waveform.length / numberOfBars;

        float barSpacing = (float) width / numberOfBars;

        for (int i = 0; i < numberOfBars; i++) {
            int index = i * step;
            int value = waveform[index];

            int barHeight = (int) ((value + 128) * (height / 2.0f) / 128);
            float x = i * barSpacing + (barSpacing / 2f);

            canvas.drawLine(x, height / 2f, x, height / 2f - barHeight, paint);
        }
    }
}
