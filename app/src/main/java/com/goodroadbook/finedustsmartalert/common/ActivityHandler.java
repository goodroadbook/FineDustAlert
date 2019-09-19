package com.goodroadbook.finedustsmartalert.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.goodroadbook.finedustsmartalert.init.GuideActivity;
import com.goodroadbook.finedustsmartalert.init.PermissionActivity;

public class ActivityHandler
{
    public static void OpenActivity(Context context, int initState)
    {
        Intent intent = null;
        int reqValue = DefineValue.REQ_GUIDE;

        switch (initState)
        {
            case DefineValue.INIT_STATE_GUIDE:
                reqValue = DefineValue.REQ_GUIDE;
                intent = new Intent(context, GuideActivity.class);
                break;
            case DefineValue.INIT_STATE_PERMISSION:
                reqValue = DefineValue.REQ_PERMISSION;
                intent = new Intent(context, PermissionActivity.class);
                break;
            case DefineValue.INIT_STATE_FINISH:
            default:
                break;
        }

        if(null != intent)
        {
            ((Activity)context).startActivityForResult(intent, reqValue);
        }
    }
}
