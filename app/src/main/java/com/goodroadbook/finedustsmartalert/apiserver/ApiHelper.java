package com.goodroadbook.finedustsmartalert.apiserver;

import android.util.Log;

import com.goodroadbook.finedustsmartalert.apiserver.httpmgr.HttpConnMgr;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ApiHelper {

    private static final String AIRKOREA_DEV_URL = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
    private static final String SERVICE_KEY = "serviceKey=XPqwATtX23hPwOON2DsBnloJDHi%2FH%2FbLEtG8un%2FWLIgWCkWhmfEpsvgFzHlZ1vF%2BGRVT4impI%2BtyYO3vv7m0sg%3D%3D";
    private static final String NUMOFROWS = "numOfRows=";
    private static final String PAGE_NO = "pageNo=";
    private static final String SIDO_NAME = "sidoName=";
    private static final String VER_NAME = "ver=1.3";
    private static final String RETURN_TYPE = "_returnType=json";

    public static String requestSync(String numOfRows, String pageNo, String sidoName) {

        String urldata = AIRKOREA_DEV_URL + "?" + SERVICE_KEY + "&" +
                NUMOFROWS + numOfRows + "&" +
                PAGE_NO + pageNo + "&" +
                SIDO_NAME + sidoName + "&" +
                VER_NAME + "&" + RETURN_TYPE;

        Log.d("namjinha", "urldata = " + urldata);

        return HttpConnMgr.requestHttpApiServer(urldata);
    }
}
