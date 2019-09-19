package com.goodroadbook.finedustsmartalert.server.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsHelper
{
    private FirebaseAnalytics mFirebaseAnalytics = null;

    public AnalyticsHelper()
    {
        ;
    }

    public void initFirebaseAnalytics(Context context)
    {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void sendLogEvent(String eventName, String aItemId, String aItemName)
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, aItemId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, aItemName);

        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public void setUserProperty(String key, String value)
    {
        mFirebaseAnalytics.setUserProperty(key, value);
    }
}
