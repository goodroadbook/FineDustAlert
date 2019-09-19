package com.goodroadbook.finedustsmartalert.server.remoteconfig;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.goodroadbook.finedustsmartalert.R;
import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class RemoteConfigHelper
{
    private final String RKEY_AIRKOREA_AUTH_KEY = "AirKoreaAuthKey";
    private final String RKEY_SIDO_LIST_SERVER_URL = "SidoListServerURL";

    private Context mActivity;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteConfigHelper(Activity activity)
    {
        this.mActivity = activity;
    }

    public void init()
    {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);
    }

    public void remoteConfigfetch()
    {
        long cacheExpiration = 3600; // 1 hour in seconds.

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled())
        {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Log.d("namjinha", "fetch onComplete");
                if (task.isSuccessful())
                {
                    mFirebaseRemoteConfig.activateFetched();
                    updateRemoteConfigValue();
                }
                else
                {
                    ;
                }
            }
        });
    }

    private void updateRemoteConfigValue()
    {
        String sidoListServerUrl = mFirebaseRemoteConfig.getString(RKEY_SIDO_LIST_SERVER_URL);
        String airKoreaAuthKey = mFirebaseRemoteConfig.getString(RKEY_AIRKOREA_AUTH_KEY);

        Log.d("namjinha", "sidoListServerUrl = " + sidoListServerUrl);
        Log.d("namjinha", "airKoreaAuthKey = " + airKoreaAuthKey);

        PreferenceManager.setStringPreference(mActivity, DefineValue.PREFERENCE_SIDO_LIST_SERVER_URL, sidoListServerUrl);
        PreferenceManager.setStringPreference(mActivity, DefineValue.PREFERENCE_AIRKOREA_AUTH_KEY, airKoreaAuthKey);
    }
}
