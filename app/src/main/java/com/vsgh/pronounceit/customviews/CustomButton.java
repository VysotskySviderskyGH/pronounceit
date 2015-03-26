package com.vsgh.pronounceit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.singletones.FontContainer;

/**
 * Created by Slawa on 2/1/2015.
 */
public class CustomButton extends Button {

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.BackgroundCustomBtn, 0, 0);
        Drawable backgroundShape = attributes.getDrawable(R.styleable.BackgroundCustomBtn_cv_background);
        if (backgroundShape != null) {
            setBackground(backgroundShape);
        }
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(FontContainer.lanenar);
    }
}
