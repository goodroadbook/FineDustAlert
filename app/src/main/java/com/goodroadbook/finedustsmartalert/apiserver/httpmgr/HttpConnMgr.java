package com.goodroadbook.finedustsmartalert.apiserver.httpmgr;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnMgr {

    public static String requestHttpApiServer(String urldata)
    {
        HttpURLConnection hconn         = null;
        OutputStream os                 = null;
        InputStream is                  = null;
        ByteArrayOutputStream baos      = null;
        String resdata                  = null;
        URL url                         = null;

        if(null == urldata) {
            return null;
        }

        try {
            url = new URL(urldata);
            hconn = (HttpURLConnection)url.openConnection();
            hconn.setConnectTimeout(30 * 1000);
            hconn.setReadTimeout(30 * 1000);
            hconn.setRequestMethod("GET");

            int rescode = hconn.getResponseCode();
            String resmsg = hconn.getResponseMessage();

            if(rescode == HttpURLConnection.HTTP_OK) {
                is = hconn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }

                byteData = baos.toByteArray();
                resdata = new String(byteData, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(null != hconn) {
                hconn.disconnect();
            }

            if(null != os) {
                try{os.close();} catch (Exception e) {e.printStackTrace();}
            }

            if(null != is) {
                try {is.close();} catch (Exception e) {e.printStackTrace();}
            }

            if(null != baos) {
                try{baos.close();} catch (Exception e) {e.printStackTrace();}
            }
        }

        return resdata;
    }
}
