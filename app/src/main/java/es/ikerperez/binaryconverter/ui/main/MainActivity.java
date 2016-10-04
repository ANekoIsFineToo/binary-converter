package es.ikerperez.binaryconverter.ui.main;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.ikerperez.binaryconverter.R;
import es.ikerperez.binaryconverter.ui.base.BaseActivity;
import es.ikerperez.binaryconverter.utils.ViewUtil;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabs;
    @BindView(R.id.ad_view) AdView mAdView;

    private Activity mActivity;
    private int mLastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mActivity = this;

        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);

        mTabs.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                ViewUtil.hideKeyboard(mActivity);
                changeToolbarColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                mAdView.setVisibility(View.GONE);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ConverterFragment.newInstance(), getString(R.string.main_tab_converter));
        adapter.addFragment(RealTimeFragment.newInstance(), getString(R.string.main_tab_real_time));
        adapter.addFragment(BaseNFragment.newInstance(), getString(R.string.main_tab_base_n));
        viewPager.setAdapter(adapter);
    }

    private void changeToolbarColor(int position) {
        String preColorFrom = null;
        String preColorDarkFrom = null;
        String preColorTo = null;
        String preColorDarkTo = null;

        switch (mLastPosition) {
            case 0:
                preColorFrom = fetchColor(R.color.colorPrimary);
                preColorDarkFrom = fetchColor(R.color.colorPrimaryDark);
                break;
            case 1:
                preColorFrom = fetchColor(R.color.alternate_2);
                preColorDarkFrom = fetchColor(R.color.alternate_dark_2);
                break;
            case 2:
                preColorFrom = fetchColor(R.color.alternate_3);
                preColorDarkFrom = fetchColor(R.color.alternate_dark_3);
        }

        switch (position) {
            case 0:
                preColorTo = fetchColor(R.color.colorPrimary);
                preColorDarkTo = fetchColor(R.color.colorPrimaryDark);
                break;
            case 1:
                preColorTo = fetchColor(R.color.alternate_2);
                preColorDarkTo = fetchColor(R.color.alternate_dark_2);
                break;
            case 2:
                preColorTo = fetchColor(R.color.alternate_3);
                preColorDarkTo = fetchColor(R.color.alternate_dark_3);
        }

        if (preColorFrom == null || preColorDarkFrom == null ||
                preColorTo == null || preColorDarkTo == null) {
            return;
        }

        Integer colorFrom = Color.parseColor(preColorFrom);
        Integer colorTo = Color.parseColor(preColorTo);
        Integer colorStatusFrom = Color.parseColor(preColorDarkFrom);
        Integer colorStatusTo = Color.parseColor(preColorDarkTo);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(), colorFrom, colorTo);
        ValueAnimator colorTabsAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(), colorFrom, colorTo);
        ValueAnimator colorStatusAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(), colorStatusFrom, colorStatusTo);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });

        colorTabsAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTabs.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        colorStatusAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mActivity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                }
            }
        });

        colorAnimation.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
        colorTabsAnimation.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        colorTabsAnimation.setStartDelay(0);
        colorTabsAnimation.start();
        colorStatusAnimation.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        colorStatusAnimation.setStartDelay(0);
        colorStatusAnimation.start();

        mLastPosition = position;
    }

    private String fetchColor(@ColorRes int color) {
        return String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(this, color)));
    }
}
