package com.goodroadbook.finedustsmartalert.server.airkorea;

import android.util.Log;

import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.goodroadbook.finedustsmartalert.server.airkorea.HttpConnMgr;

public class AirKoreaHelper
{
    private static final String SERVICE_KEY = "serviceKey=";
    private static final String NUMOFROWS = "numOfRows=";
    private static final String PAGE_NO = "pageNo=";
    private static final String SIDO_NAME = "sidoName=";
    private static final String VER_NAME = "ver=1.3";
    private static final String SEARCH_CONDITION = "searchCondition=HOUR";
    private static final String RETURN_TYPE = "_returnType=json";

    public static String requestSyncMesureSidoLIst(String serverUrl, String airKoreaAuthKey, String numOfRows, String pageNo, String sidoName)
    {
        String urldata = serverUrl + "?" +
                SERVICE_KEY + airKoreaAuthKey + "&" +
                NUMOFROWS + numOfRows + "&" +
                PAGE_NO + pageNo + "&" +
                SIDO_NAME + sidoName + "&" +
                SEARCH_CONDITION + "&" +
                VER_NAME + "&" + RETURN_TYPE;

        Log.d("namjinha", "sido list urldata = " + urldata);

        return HttpConnMgr.requestHttpApiServer(urldata);
    }

    public static int currentState(int fineDustValue, int ultrafineDustValue)
    {
        int state = DefineValue.STATE_GOOD;

        if((fineDustValue >= 151 && fineDustValue <= 600) ||
                (ultrafineDustValue >= 76 && ultrafineDustValue <= 500))
        {
            state = DefineValue.STATE_VERY_UNHEALTHY;
        }
        else if ((fineDustValue >= 81 && fineDustValue <= 150) ||
                (ultrafineDustValue >= 36 && ultrafineDustValue <= 75))
        {
            state = DefineValue.STATE_UNHEALTHY;
        }
        else if ((fineDustValue >= 31 && fineDustValue <= 80) ||
                (ultrafineDustValue > 16 && ultrafineDustValue <= 35))
        {
            state = DefineValue.STATE_MODERATE;
        }
        else
        {
            state = DefineValue.STATE_GOOD;
        }

        return state;
    }

    public static int currentFineDustState(int fineDustValue)
    {
        int state = DefineValue.STATE_GOOD;

        if(fineDustValue >= 151 && fineDustValue <= 600)
        {
            state = DefineValue.STATE_VERY_UNHEALTHY;
        }
        else if (fineDustValue >= 81 && fineDustValue <= 150)
        {
            state = DefineValue.STATE_UNHEALTHY;
        }
        else if (fineDustValue >= 31 && fineDustValue <= 80)
        {
            state = DefineValue.STATE_MODERATE;
        }
        else
        {
            state = DefineValue.STATE_GOOD;
        }

        return state;
    }

    public static int currentUltrafineDustState(int ultrafineDustValue)
    {
        int state = DefineValue.STATE_GOOD;

        if(ultrafineDustValue >= 76 && ultrafineDustValue <= 500)
        {
            state = DefineValue.STATE_VERY_UNHEALTHY;
        }
        else if (ultrafineDustValue >= 36 && ultrafineDustValue <= 75)
        {
            state = DefineValue.STATE_UNHEALTHY;
        }
        else if (ultrafineDustValue > 16 && ultrafineDustValue <= 35)
        {
            state = DefineValue.STATE_MODERATE;
        }
        else
        {
            state = DefineValue.STATE_GOOD;
        }

        return state;
    }
}
