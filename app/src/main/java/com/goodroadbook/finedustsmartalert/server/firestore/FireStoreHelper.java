package com.goodroadbook.finedustsmartalert.server.firestore;

import android.content.Context;
import android.util.Log;
import android.util.TimeUtils;

import androidx.annotation.NonNull;

import com.goodroadbook.finedustsmartalert.common.DateTimeUtils;
import com.goodroadbook.finedustsmartalert.common.DefineValue;
import com.goodroadbook.finedustsmartalert.common.PreferenceManager;
import com.goodroadbook.finedustsmartalert.server.HttpResData;
import com.goodroadbook.finedustsmartalert.server.list;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FireStoreHelper
{
    public static final String NUMOFROW = "50";
    public static final String PAGE_NO = "1";

    private static final long ONE_HOUR = 60 * 60 * 1000;
    private static boolean lateststate = false;
    private OnFireStoreCompleteListener mCompleteListener;
    private Context mContext;

    public FireStoreHelper(Context context)
    {
        this.mContext = context;
    }

    public void setFireStoreCompleteListener(OnFireStoreCompleteListener listener)
    {
        mCompleteListener = listener;
    }

    public void getLatestDataData(final String doName, final String siName)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(doName).document(siName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Log.d("namjinha", "doc data = " + document.getData());
                        list listData = document.toObject(list.class);
                        String datatime = listData.getDataTime();
                        long mesureTimeValue = DateTimeUtils.changeDateTime(datatime);
                        long currentTimeValue = DateTimeUtils.getKoreaTime();
                        long val = currentTimeValue - mesureTimeValue;

                        Log.d("namjinha", "val = " + val);
                        Log.d("namjinha", "mesureTimeValue = " + mesureTimeValue);
                        Log.d("namjinha", "currentTimeValue = " + currentTimeValue);
                        Log.d("namjinha", "currentTimeValue format = " + DateTimeUtils.changeLongTime(currentTimeValue));

                        if(val < ONE_HOUR)
                        {
                            lateststate = true;
                        }

                        //미세먼지 기록, 초미세먼지 기록
                        setFineDustValue(listData.getPm10Value(), listData.getPm25Value());

                        //측정 날짜 시간 기록
                        setAirKoreaMeasureDateTime(listData.getDataTime());
                    }
                    else
                    {
                        lateststate = false;
                    }
                }
                else
                {
                    lateststate = false;
                }

                mCompleteListener.onComplete(0, lateststate);
                Log.d("namjinha", "lateststate = " + lateststate);
            }
        });

        Log.d("namjinha", "lateststate = " + lateststate);
    }

    public void addData(String doName, String siName, HttpResData resData)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for(list item : resData.getList())
        {
            Log.d("namjinha", "si name " + item.getCityName());
            if(siName.equals(item.getCityName()))
            {
                setFineDustValue(item.getPm10Value(), item.getPm25Value());
                setAirKoreaMeasureDateTime(item.getDataTime());
            }

            db.collection(doName)
                    .document(item.getCityName())
                    .set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            ;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.d("namjinha", "Document Error!!");
                        }
                    });
        }
    }

    private void setFineDustValue(String fineDustValue, String ultrafineDustValue)
    {
        Log.d("namjinha", "fineDustValue = " + fineDustValue);
        Log.d("namjinha", "ultrafineDustValue = " + ultrafineDustValue);

        //미세먼지 기록
        PreferenceManager.setStringPreference(mContext,
                DefineValue.PREFERENCE_FINE_DUST_VALUE, fineDustValue);

        //초미세먼지 기록
        PreferenceManager.setStringPreference(mContext,
                DefineValue.PREFERENCE_ULTRAFINE_DUST_VALUE, ultrafineDustValue);
    }

    private void setAirKoreaMeasureDateTime(String dateTime)
    {
        if(null == dateTime)
        {
            return;
        }

        PreferenceManager.setStringPreference(mContext, DefineValue.PREFERENCE_MEASURE_DATETIME, dateTime);
    }
}
