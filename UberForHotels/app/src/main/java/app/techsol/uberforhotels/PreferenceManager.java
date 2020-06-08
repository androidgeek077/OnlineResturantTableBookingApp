package app.techsol.uberforhotels;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {

    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String FIL_ENAME = "Login File";
    private static final String USER_LOGIN_STATE = "login_state";
    private static final String USER_TYPE = "user type";


    private static final int PRIVATE_MODE = 0;

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(FIL_ENAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setUserLog(String UserType, boolean isLogged) {

        editor.putBoolean(USER_LOGIN_STATE, isLogged);
        editor.putString(USER_TYPE, UserType);
        editor.commit();
    }





    public boolean userIsLogged() {
        return sharedPreferences.getBoolean(USER_LOGIN_STATE, false);

    }
    public boolean setUserLogout() {
        return sharedPreferences.getBoolean(USER_LOGIN_STATE, false);

    }

    public String getUserType(){
        return sharedPreferences.getString(USER_TYPE, "");

    }






    public void logoutUser(Context context) {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}
