package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.view.IndicatorView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.LinkedList;

public class WelcomeActivity extends BaseActivity {

    private boolean fromAboutUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        IndicatorView indicatorView = (IndicatorView) findViewById(R.id.indicator_view);
        viewPager.setAdapter(new WelcomeAdapter());
        indicatorView.setupWithViewPager(viewPager);

        ImmersionBar.with(this).init();

        fromAboutUs = getIntent().getBooleanExtra("fromAboutUs", false);
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }

    private class WelcomeAdapter extends PagerAdapter {

        private LinkedList<View> cachedView = new LinkedList<>();
        private int[] imageId = {R.mipmap.page_1, R.mipmap.page_2, R.mipmap.page_3};

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            ViewHolder holder;
            if (cachedView.size() > 0) {
                view = cachedView.pollFirst();
                holder = (ViewHolder) view.getTag();
            } else {
                view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.viewpager_item_welcom, container, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.startView = (ImageView) view.findViewById(R.id.start_now);
                holder.startView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fromAboutUs) {
                            finish();
                        } else {
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
                view.setTag(holder);
            }

            holder.imageView.setImageResource(imageId[position]);
            holder.startView.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            cachedView.add(view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class ViewHolder {
        ImageView imageView;
        ImageView startView;

    }
}
