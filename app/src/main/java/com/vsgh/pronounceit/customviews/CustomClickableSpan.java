package com.vsgh.pronounceit.customviews;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Slawa on 4/3/2015.
 */
public class CustomClickableSpan extends ClickableSpan{
    private TextPaint textpaint;
    public boolean shouldHilightWord = false;
    public CustomClickableSpan(int anID, int selected) {
        int id = anID;
        if(selected == id)  {
            shouldHilightWord = true;
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        textpaint = ds;
        ds.setColor(ds.linkColor);
        if(shouldHilightWord){
            textpaint.bgColor = Color.GRAY;
            textpaint.setARGB(255, 255, 255, 255);
        }
        ds.setUnderlineText(false);
    }

    public void changeSpanBgColor(View widget){
        shouldHilightWord = true;
        updateDrawState(textpaint);
        widget.invalidate();
    }

    @Override
    public void onClick(View widget) {

    }
}
