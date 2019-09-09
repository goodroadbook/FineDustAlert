package com.goodroadbook.finedustsmartalert.apiserver;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class GetDataAsynTask extends AsyncTask<Integer , Integer , String>
{
    @Override
    protected String doInBackground(Integer... integers)
    {
        Map<String, HttpResData> locationMap = new HashMap<>();
        HttpResData resData = requestFineDust("50", "1", "%EA%B2%BD%EA%B8%B0");
        locationMap.put(resData.getParm().getPageNo(), resData);

        int startPageNo = Integer.valueOf(resData.getParm().getPageNo()) + 1;
        int endPageNo = 0;
        int count = Integer.valueOf(resData.getTotalCount()) - 50;
        if(count > 0)
        {
            endPageNo = (count / 50) + startPageNo + 1;
        }

        Log.d("namjinha", "startPageNo = " + startPageNo);
        Log.d("namjinha", "endPageNo = " + endPageNo);

        for(int i = startPageNo; i< endPageNo; i++)
        {
            resData = requestFineDust("50", String.valueOf(i), "%EA%B2%BD%EA%B8%B0");
            locationMap.put(resData.getParm().getPageNo(), resData);
        }

        addData(resData.getParm().getSidoName(), locationMap);

        return null;
    }

    protected void onPostExecute(String result) {

    }

    private HttpResData requestFineDust(String numOfRows, String pageNo, String sidoName)
    {
        String resdata = ApiHelper.requestSync(numOfRows, pageNo, sidoName);
        Log.d("namjinha", "Res Data = " + resdata);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        HttpResData resData = gson.fromJson(resdata, HttpResData.class);

        return resData;
    }

    private void addData(String sidoName, Map<String, HttpResData> locationMap)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("location")
                .document(sidoName)
                .set(locationMap)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Log.d("namjinha", "DocumentSnapshot successfully written!");
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
