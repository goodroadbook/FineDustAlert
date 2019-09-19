package com.goodroadbook.finedustsmartalert.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.goodroadbook.finedustsmartalert.R;
import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.goodroadbook.finedustsmartalert.server.airkorea.AirKoreaHelper;

public class UpdateMainView
{
    private final int STATE_GOOD = 0;
    private final int STATE_MODERATE = 1;
    private final int STATE_UNHEALTHY = 2;
    private final int STATE_VERY_UNHEALTHY = 3;

    private int mainBackgroundImgArray[] =
            {
                    R.drawable.bg_good,
                    R.drawable.bg_moderate,
                    R.drawable.bg_unhealthy,
                    R.drawable.bg_very_unhealthy
            };

    private int mainFineDustImgArray[] =
            {
                    R.drawable.main_icon_good,
                    R.drawable.main_icon_moderate,
                    R.drawable.main_icon_unhealthy,
                    R.drawable.main_icon_very_unhealthy
            };

    private int mainFineDustTextArray[] =
            {
                    R.string.finedust_good,
                    R.string.finedust_moderate,
                    R.string.finedust_unhealthy,
                    R.string.finedust_very_unhealthy
            };

    private int mainBottomFineDustImageArray[] =
            {
                    R.drawable.icon_good,
                    R.drawable.icon_moderate,
                    R.drawable.icon_unhealthy,
                    R.drawable.icon_very_unhealthy
            };

    private Activity mActivity;
    private ConstraintLayout mMainConstraintLayout;
    private ImageView mFineDustImageView;
    private ImageView mBottomFineDustImageView;
    private ImageView mBottomUltrafineDustImageView;
    private TextView mTimeText;
    private TextView mFineDustTextView;
    private TextView mFineDustValueTextView;
    private TextView mUltrafineDustValueTextView;

    public UpdateMainView(Activity activity)
    {
        this.mActivity = activity;
    }

    public void initView()
    {
        //Main Background
        mMainConstraintLayout = (ConstraintLayout)mActivity.findViewById(R.id.mainbglayout);

        //Fine Dust Image
        mFineDustImageView = (ImageView)mActivity.findViewById(R.id.finedustimg);

        //Bottom Fine Dust Image
        mBottomFineDustImageView = (ImageView)mActivity.findViewById(R.id.bottomfindustimg1);

        //Bottom Fine Dust Image
        mBottomUltrafineDustImageView = (ImageView)mActivity.findViewById(R.id.bottomfindustimg2);

        //Time Text
        mTimeText = (TextView)mActivity.findViewById(R.id.timetxt);

        //Fine Dust Text
        mFineDustTextView = (TextView)mActivity.findViewById(R.id.finedusttxt);

        //Fine Dust Value
        mFineDustValueTextView = (TextView)mActivity.findViewById(R.id.finedustvaluetxt1);

        //Ultrafine Dust Value
        mUltrafineDustValueTextView = (TextView)mActivity.findViewById(R.id.finedustvaluetxt2);
    }

    public void updateView(int state)
    {
        if(null != mMainConstraintLayout)
        {
            mMainConstraintLayout.setBackground(mActivity.getDrawable(mainBackgroundImgArray[state]));
        }

        if(null != mFineDustImageView)
        {
            mFineDustImageView.setImageDrawable(mActivity.getDrawable(mainFineDustImgArray[state]));
        }

        if(null != mFineDustTextView)
        {
            mFineDustTextView.setText(mainFineDustTextArray[state]);
        }

        if(null != mTimeText)
        {
            String dateTime = PreferenceManager.getStringPreference(mActivity,
                    DefineValue.PREFERENCE_MEASURE_DATETIME, null);
            mTimeText.setText(dateTime);
        }
    }

    public void updateBottomView(String fineDustValue, String ultrafineDustValue)
    {
        Log.d("namjinha", "fineDustValue = " + fineDustValue);
        Log.d("namjinha", "ultrafineDustValue = " + ultrafineDustValue);
        int state = DefineValue.STATE_GOOD;

        if(null != mFineDustValueTextView)
        {
            mFineDustValueTextView.setText(fineDustValue);
        }

        if(null != mUltrafineDustValueTextView)
        {
            mUltrafineDustValueTextView.setText(ultrafineDustValue);
        }

        if(null != mBottomFineDustImageView)
        {
            state = AirKoreaHelper.currentFineDustState(Integer.parseInt(fineDustValue));
            mBottomFineDustImageView.setImageDrawable(mActivity.getDrawable(mainBottomFineDustImageArray[state]));
        }

        if(null != mBottomUltrafineDustImageView)
        {
            state = AirKoreaHelper.currentUltrafineDustState(Integer.parseInt(ultrafineDustValue));
            mBottomUltrafineDustImageView.setImageDrawable(mActivity.getDrawable(mainBottomFineDustImageArray[state]));
        }
    }
}
