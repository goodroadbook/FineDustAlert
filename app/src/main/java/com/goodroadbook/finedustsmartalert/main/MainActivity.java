package com.goodroadbook.finedustsmartalert.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goodroadbook.finedustsmartalert.R;
import com.goodroadbook.finedustsmartalert.apiserver.GetDataAsynTask;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private LocationService locationService;

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
        setContentView(R.layout.activity_main);

        Button showlocButton = (Button) findViewById(R.id.button);
        showlocButton.setOnClickListener(this);

        // 기기의 위치 서비스가 켜져 있는지 확인
        if (!checkLocationServicesStatus())
        {
            showDialogForLocationServiceSetting();
            finish();
            return;
        }

        // 위치 권한 획득 유무를 확인한다.
        if(!isLocationPermissins())
        {
            checkRunTimePermission();
            finish();
            return;
        }

        // 현재 주소를조회한다.

        // 조회된 주소의 미세먼지 데이터를 조회한다.
        GetDataAsynTask getDataAsynTask = new GetDataAsynTask();
        getDataAsynTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_LOCATION:
                if (checkLocationServicesStatus())
                {
                    if (checkLocationServicesStatus())
                    {
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button:
                locationService = new LocationService(MainActivity.this);
                locationService.getLocation();
                double latitude = locationService.getLatitude();
                double longitude = locationService.getLongitude();
                String address = getCurrentAddress(latitude, longitude);

                TextView textview_address = (TextView)findViewById(R.id.textview);
                textview_address.setText(address);
                Toast.makeText(MainActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults)
    {
        if ( permsRequestCode == REQ_LOCATION_PERMISSIONS &&
                grandResults.length == REQUIRED_PERMISSIONS.length)
        {
            boolean permissionstate = true;
            for (int result : grandResults)
            {
                if (result != PackageManager.PERMISSION_GRANTED)
                {
                    permissionstate = false;
                    break;
                }
            }

            if ( permissionstate )
            {
                ;
            }
            else
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1]))
                {
                    Toast.makeText(MainActivity.this,
                            "퍼미션 허용되지 않았습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,
                            "퍼미션이 허용되지 않았습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showDialogForLocationServiceSetting()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener()
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
            }
        });
        builder.create().show();
    }

    private String getCurrentAddress( double latitude, double longitude)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        }
        catch (Exception e)
        {
            return null;
        }

        if (addresses == null || addresses.size() == 0)
        {
            return null;
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0);
    }

    private  boolean isLocationPermissins()
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
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

    private void checkRunTimePermission()
    {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
        {
            ;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        REQ_LOCATION_PERMISSIONS);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        REQ_LOCATION_PERMISSIONS);
            }
        }
    }

    private boolean checkLocationServicesStatus()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}