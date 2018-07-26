package com.topotek.libs.libs0.android.view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleCanvasView<T> extends View {

    protected Draw baseDraw;
    protected Draw mDraw;
    private Collection strings;
    private List<String> list;
    private ArrayList<String> l;

    public SimpleCanvasView(Context context){
        super(context);

        for (String s : list){

        }
    }

    public SimpleCanvasView(Context context, Draw baseDraw) {
        super(context);

        this.baseDraw = baseDraw;
    }

    public SimpleCanvasView(Context context, Draw baseDraw, Draw draw) {
        super(context);

        this.baseDraw = baseDraw;
        mDraw = draw;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(baseDraw != null)
            baseDraw.onDraw(canvas);
        if(mDraw != null)
            mDraw.onDraw(canvas);
    }

    public void setBaseDraw(Draw baseDraw) {
        this.baseDraw = baseDraw;
        invalidate();
    }

    public void setDraw(Draw draw) {
        this.mDraw = draw;
        invalidate();
    }

    public interface Draw{
        void onDraw(Canvas canvas);
    }
}
