package com.goodroadbook.finedustsmartalert.server.airkorea;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.goodroadbook.finedustsmartalert.server.HttpResData;
import com.goodroadbook.finedustsmartalert.server.firestore.FireStoreHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AirSidoDataAsynTask extends AsyncTask<Integer , Integer , Integer>
{
    private Context mContext;
    private OnAriKoreaCompleteListener mListener;

    public AirSidoDataAsynTask(Context context, OnAriKoreaCompleteListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected Integer doInBackground(Integer... integers)
    {
        String adminarea = PreferenceManager.getStringPreference(mContext, DefineValue.PREFERENCE_ADMIN_AREA, null);
        String siName = PreferenceManager.getStringPreference(mContext, DefineValue.PREFERENCE_SI_NAME, null);
        if(null == adminarea || null == siName)
        {
            return -1;
        }

        HttpResData resData = requestFineDust(FireStoreHelper.NUMOFROW, FireStoreHelper.PAGE_NO, adminarea);

        if(null == resData)
        {
            return -1;
        }

        FireStoreHelper fireStoreHelper = new FireStoreHelper(mContext);
        fireStoreHelper.addData(resData.getParm().getSidoName(), siName, resData);

        return 0;
    }

    protected void onPostExecute(Integer result)
    {
        if(null != mListener)
        {
            mListener.onAriKoreaComplete(result);
        }
    }

    private HttpResData requestFineDust(String numOfRows, String pageNo, String sidoName)
    {
        String sidoListServerUrl = PreferenceManager.getStringPreference(mContext,
                DefineValue.PREFERENCE_SIDO_LIST_SERVER_URL, null);
        String airKoreaAuthKey = PreferenceManager.getStringPreference(mContext,
                DefineValue.PREFERENCE_AIRKOREA_AUTH_KEY, null);

        String resdata = AirKoreaHelper.requestSyncMesureSidoLIst(sidoListServerUrl, airKoreaAuthKey, numOfRows, pageNo, sidoName);
        Log.d("namjinha", "Res Data = " + resdata);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        HttpResData resData = gson.fromJson(resdata, HttpResData.class);

        return resData;
    }
}
