package com.goodroadbook.finedustsmartalert.init;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goodroadbook.finedustsmartalert.R;

public class PermissionActivity extends AppCompatActivity
{
    private int DENIED_NOSHOW = -2;
    private int DENIED  = PackageManager.PERMISSION_DENIED;
    private int GRANTED = PackageManager.PERMISSION_GRANTED;

    private static final int REQ_LOCATION = 1001;
    private static final int REQ_LOCATION_PERMISSIONS = 100;

    private String[] REQUIRED_PERMISSIONS  =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        // 기기의 위치 서비스가 켜져 있는지 확인
        if (!isLocationServicesStatus(this))
        {
            locationServiceSetting();
            return;
        }

        // 위치 권한 획득 유무를 확인한다.
        if(!isLocationPermissins(this))
        {
            checkRunTimePermission();
            return;
        }

        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_LOCATION:
                if (isLocationServicesStatus(this))
                {
                    //기기 위치 권한 ON 상태, 앱 위치 권한 조회 권한을 사용자에게 받을 수 있도록 한다.
                    checkRunTimePermission();
                    return;
                }
                else
                {
                    //기기 위치 권한 OFF 상태
                    // 퍼미션 액티비티를 종료한다.
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults)
    {
        int result = permissionState(permissions, grandResults);
        Log.d("namjinha", "permsRequestCode = " + permsRequestCode);
        Log.d("namjinha", "result = " + result);

        if (PackageManager.PERMISSION_GRANTED == result)
        {
            switch (permsRequestCode)
            {
                case REQ_LOCATION_PERMISSIONS:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
        else if (result == DENIED_NOSHOW)
        {
            //사용자가 위치 권한 허용을 거부한 경우 앱 설정에서 다시 설정이 가능하다.
            openPermissionSettingActivity();
        }
        else
        {
            switch (permsRequestCode)
            {
                case REQ_LOCATION_PERMISSIONS:
                    Toast.makeText(this,
                            "퍼미션 허용되지 않았습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                            Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    break;
            }
        }
    }

    public static boolean isLocationServicesStatus(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static  boolean isLocationPermissins(Context context)
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void locationServiceSetting()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 활성화 되도록 설정되어야 합니다. 위치 서비스를 활성화 하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("활성화", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQ_LOCATION);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        builder.create().show();
    }

    private void checkRunTimePermission()
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(REQUIRED_PERMISSIONS, REQ_LOCATION_PERMISSIONS);
        }
    }

    private int permissionState(String permissions[], int[] grantResults)
    {
        boolean granted = true;
        for(int i = 0; i<grantResults.length; i++)
        {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
            {
                granted &= true;
            }
            else
            {
                granted &= false;

                if(!shouldShowRequestPermissionRationale(permissions[i]))
                {
                    return DENIED_NOSHOW;
                }
            }
        }

        if(granted)
        {
            return GRANTED;
        }

        return DENIED;
    }

    private void openPermissionSettingActivity()
    {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", packageURI);
        startActivity(intent);
    }
}
