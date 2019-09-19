package com.goodroadbook.finedustsmartalert.init;

import android.content.Context;

import com.goodroadbook.finedustsmartalert.common.ActivityHandler;
import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;

public class InitStepHandler
{
    public InitStepHandler()
    {
        ;
    }

    public static int getCurrentInitState(Context context)
    {
        return PreferenceManager.getIntPreference(context,
                DefineValue.PREFERENCE_MAIN_INIT_STATE,
                DefineValue.INIT_STATE_GUIDE);
    }

    public static void nextInitState(Context context, int initState)
    {
        if(PreferenceManager.getIntPreference(context,
                DefineValue.PREFERENCE_MAIN_INIT_STATE,
                DefineValue.INIT_STATE_GUIDE)
            == DefineValue.INIT_STATE_FINISH)
        {
            return;
        }

        PreferenceManager.setIntPreference(context, DefineValue.PREFERENCE_MAIN_INIT_STATE, initState);

        ActivityHandler.OpenActivity(context, initState);
    }
}
