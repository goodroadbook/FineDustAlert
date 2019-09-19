package com.goodroadbook.finedustsmartalert.common;

public class DefineValue
{
    public final static String PREFERENCE_MAIN_INIT_STATE = "main_init_state";

    public static final int INIT_STATE_GUIDE = 0;
    public static final int INIT_STATE_PERMISSION = 1;
    public static final int INIT_STATE_FINISH = 2;

    public static final int REQ_GUIDE = 1000;
    public static final int REQ_PERMISSION = 1001;

    public static final String PREFERENCE_ADMIN_AREA = "preference_admin_area";
    public static final String PREFERENCE_ADDRESS_LINE = "preference_address_line";
    public static final String PREFERENCE_SI_NAME = "preference_si_name";
    public static final String PREFERENCE_DO_NAME = "preference_do_name";
    public static final String PREFERENCE_MEASURE_DATETIME = "preference_measure_datetime";

    public static final String PREFERENCE_FINE_DUST_VALUE = "preference_fine_dust_value";
    public static final String PREFERENCE_ULTRAFINE_DUST_VALUE = "preference_ultrafine_dust_value";

    public static final String PREFERENCE_AIRKOREA_AUTH_KEY = "preference_airkorea_auth_key";
    public static final String PREFERENCE_SIDO_LIST_SERVER_URL = "preference_sido_list_server_url";

    public static final String FA_EVENT_AIRKOREA_QUERY = "AirKoreaQuery";
    public static final String FA_EVENT_FIRESTORE_QUERY = "FirestoreQuery";
    public static final String FA_EVENT_MY_GPS = "MyGps";
    public static final String FA_USER_PROPERTY_PERMISSION = "userproperty_permission";
    public static final String FA_USER_PROPERTY_PERMISSION_OK = "1";
    public static final String FA_USER_PROPERTY_PERMISSION_NO = "0";

    public static final int STATE_GOOD = 0;
    public static final int STATE_MODERATE = 1;
    public static final int STATE_UNHEALTHY = 2;
    public static final int STATE_VERY_UNHEALTHY = 3;
}
