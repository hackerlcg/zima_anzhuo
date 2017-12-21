package com.beihui.market.view.busineesrel;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beihui.market.R;

public class RateView extends LinearLayout {

    private ImageView[] stars = new ImageView[5];

    public RateView(Context context) {
        this(context, null);
    }

    public RateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        inflate(context, R.layout.layout_rate_view, this);
        stars[0] = (ImageView) findViewById(R.id.star_1);
        stars[1] = (ImageView) findViewById(R.id.star_2);
        stars[2] = (ImageView) findViewById(R.id.star_3);
        stars[3] = (ImageView) findViewById(R.id.star_4);
        stars[4] = (ImageView) findViewById(R.id.star_5);

    }

    public void setRate(int star, boolean half) {
        switch (star) {
            case 5:
                stars[4].setImageResource(R.mipmap.star_full);
            case 4:
                stars[3].setImageResource(R.mipmap.star_full);
            case 3:
                stars[2].setImageResource(R.mipmap.star_full);
            case 2:
                stars[1].setImageResource(R.mipmap.star_full);
            case 1:
                stars[0].setImageResource(R.mipmap.star_full);
                break;
            default:
                break;
        }
        if (half && star >= 1 && star <= 5) {
            stars[star - 1].setImageResource(R.mipmap.star_half);
        }
    }
}
