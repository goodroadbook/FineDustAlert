package com.goodroadbook.finedustsmartalert.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goodroadbook.finedustsmartalert.R;
import com.goodroadbook.finedustsmartalert.server.airkorea.AirKoreaHelper;
import com.goodroadbook.finedustsmartalert.server.airkorea.AirSidoDataAsynTask;
import com.goodroadbook.finedustsmartalert.common.ActivityHandler;
import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.goodroadbook.finedustsmartalert.init.InitStepHandler;
import com.goodroadbook.finedustsmartalert.init.PermissionActivity;
import com.goodroadbook.finedustsmartalert.location.AddressTranslation;
import com.goodroadbook.finedustsmartalert.location.LoacationHandler;
import com.goodroadbook.finedustsmartalert.server.airkorea.OnAriKoreaCompleteListener;
import com.goodroadbook.finedustsmartalert.server.analytics.AnalyticsHelper;
import com.goodroadbook.finedustsmartalert.server.firestore.FireStoreHelper;
import com.goodroadbook.finedustsmartalert.server.firestore.OnFireStoreCompleteListener;
import com.goodroadbook.finedustsmartalert.server.remoteconfig.RemoteConfigHelper;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnAriKoreaCompleteListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.MainActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remote Config 정보를 가져온다.
        RemoteConfigHelper remoteConfigHelper = new RemoteConfigHelper(this);
        remoteConfigHelper.init();
        remoteConfigHelper.remoteConfigfetch();

        ActivityHandler.OpenActivity(this, InitStepHandler.getCurrentInitState(this));
        if(InitStepHandler.getCurrentInitState(this) == DefineValue.INIT_STATE_FINISH)
        {
            //Analytics 수집
            AnalyticsHelper analyticsHelper = new AnalyticsHelper();
            analyticsHelper.initFirebaseAnalytics(this);

            if(!PermissionActivity.isLocationServicesStatus(this) ||
                    !PermissionActivity.isLocationPermissins(this))
            {
                ActivityHandler.OpenActivity(this, DefineValue.INIT_STATE_PERMISSION);
                analyticsHelper.setUserProperty(DefineValue.FA_USER_PROPERTY_PERMISSION,
                        DefineValue.FA_USER_PROPERTY_PERMISSION_NO);
                return;
            }
            analyticsHelper.setUserProperty(DefineValue.FA_USER_PROPERTY_PERMISSION,
                    DefineValue.FA_USER_PROPERTY_PERMISSION_OK);

            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case DefineValue.REQ_GUIDE:
                InitStepHandler.nextInitState(this, DefineValue.INIT_STATE_PERMISSION);
                break;
            case DefineValue.REQ_PERMISSION:
                if(resultCode == Activity.RESULT_CANCELED)
                {
                    finish();
                }
                else
                {
                    init();
                    InitStepHandler.nextInitState(this, DefineValue.INIT_STATE_FINISH);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.mapicon:
                // 현재 주소를조회한다.
                setCuurentLocationData();

                // 조회된 주소의 미세먼지 데이터를 조회한다.
                getAirKoreaData();

                updateMainView();

                //Analytics 수집
                AnalyticsHelper analyticsHelper = new AnalyticsHelper();
                analyticsHelper.initFirebaseAnalytics(this);
                analyticsHelper.sendLogEvent(DefineValue.FA_EVENT_MY_GPS, "test", "test");
                break;
            case R.id.infoicon:
                break;
            default:
                break;
        }
    }

    @Override
    public void onAriKoreaComplete(int result)
    {
        updateMainView();
    }

    private void init()
    {
        ImageView mapbtn = (ImageView) findViewById(R.id.mapicon);
        mapbtn.setOnClickListener(this);

        ImageView infobtn = (ImageView) findViewById(R.id.infoicon);
        infobtn.setOnClickListener(this);

        // 현재 주소를조회한다.
        setCuurentLocationData();

        // 조회된 주소의 미세먼지 데이터를 조회한다.
        getAirKoreaData();

        updateMainView();
    }

    private void setCuurentLocationData()
    {
        LoacationHandler loacationHandler = new LoacationHandler(this);
        Address address = loacationHandler.getCurrentAddress();
        String add[] = address.getAddressLine(0).split(" ");
        String addressline = add[1] + " " + add[2];
        Log.d("namjinha", "addressline = " + addressline);

        String adminarea = AddressTranslation.convertAddr(address.getAdminArea());
        Log.d("namjinha", "adminarea = " + adminarea);

        try
        {
            PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_DO_NAME, adminarea);
            PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_SI_NAME, add[2]);
            PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_ADMIN_AREA, URLEncoder.encode(adminarea, "utf-8"));
            PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_ADDRESS_LINE, addressline);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        TextView addresstxt = (TextView) findViewById(R.id.addresstxt);
        addresstxt.setText(addressline);

        TextView timetxt = (TextView) findViewById(R.id.timetxt);
        timetxt.setText(getDisplayTime(System.currentTimeMillis()));
    }

    private String getDisplayTime(long timeValue)
    {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        return simpleDateFormat.format(timeValue);
    }

    private void getAirKoreaData()
    {
        String doname = PreferenceManager.getStringPreference(this, DefineValue.PREFERENCE_DO_NAME, null);
        String siname = PreferenceManager.getStringPreference(this, DefineValue.PREFERENCE_SI_NAME, null);
        Log.d("namjinha", "do name = " + doname);
        Log.d("namjinha", "si name = " + siname);

        final AnalyticsHelper analyticsHelper = new AnalyticsHelper();
        analyticsHelper.initFirebaseAnalytics(this);
        FireStoreHelper fireStoreHelper = new FireStoreHelper(this);
        fireStoreHelper.setFireStoreCompleteListener(new OnFireStoreCompleteListener()
        {
            @Override
            public void onComplete(int result, boolean state)
            {
                if(!state)
                {
                    AirSidoDataAsynTask getDataAsynTask = new AirSidoDataAsynTask(MainActivity.this, MainActivity.this);
                    getDataAsynTask.execute();

                    //AirKorea 요청 정보 수집
                    analyticsHelper.sendLogEvent(DefineValue.FA_EVENT_AIRKOREA_QUERY, null, null);
                }
                else
                {
                    updateMainView();

                    //Firestore 요청 정보 수집
                    analyticsHelper.sendLogEvent(DefineValue.FA_EVENT_FIRESTORE_QUERY, null, null);
                }
            }
        });
        fireStoreHelper.getLatestDataData(doname, siname);
    }

    private void updateMainView()
    {
        String fineDustValue = PreferenceManager.getStringPreference(this, DefineValue.PREFERENCE_FINE_DUST_VALUE, null);
        String ultrafineDustValue = PreferenceManager.getStringPreference(this, DefineValue.PREFERENCE_ULTRAFINE_DUST_VALUE, null);
        if(null == fineDustValue || null == ultrafineDustValue)
        {
            return;
        }

        int fineDustValueInt = Integer.parseInt(fineDustValue);
        int ultralfineDustValueInt = Integer.parseInt(ultrafineDustValue);
        int state = AirKoreaHelper.currentState(fineDustValueInt, ultralfineDustValueInt);

        UpdateMainView updateMainView = new UpdateMainView(MainActivity.this);
        updateMainView.initView();
        updateMainView.updateBottomView(fineDustValue, ultrafineDustValue);
        updateMainView.updateView(state);
    }
}