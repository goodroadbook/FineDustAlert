package com.goodroadbook.finedustsmartalert.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class LoacationHandler implements LocationListener
{
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private Context mContext;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    protected LocationManager locationManager;

    public LoacationHandler(Context context)
    {
        this.mContext = context;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d("namjinha", "Latitude = " + location.getLatitude());
        Log.d("namjinha", "Longitude = " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {
        ;
    }

    @Override
    public void onProviderEnabled(String s)
    {
        ;
    }

    @Override
    public void onProviderDisabled(String s)
    {
        ;
    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            if (locationManager == null)
            {
                return null;
            }

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled)
            {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                        hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
                {
                    return null;
                }

                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null)
                    {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }

                if (isGPSEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null)
                    {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mLocation;
    }

    public double getLatitude()
    {
        if(mLocation != null)
        {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public double getLongitude()
    {
        if(mLocation != null)
        {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    public Address getCurrentAddress()
    {
        getLocation();

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocation(
                    37.543158/*mLatitude*/,
                    126.992942/*mLongitude*/,
                    7);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        if (addresses == null || addresses.size() == 0)
        {
            return null;
        }

        Log.d("namjinha", "address 0 = " + addresses.get(0));

        Address address = addresses.get(0);
        return address;
    }
}
