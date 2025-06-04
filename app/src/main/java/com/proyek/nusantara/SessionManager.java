package com.proyek.nusantara;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionManager handles user session state using SharedPreferences.
 */
public class SessionManager {
    private static final String PREF_NAME = "AppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_TOKEN = "userToken";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * Create a new session by storing user details.
     * @param userId    ID of the logged-in user
     * @param userName  Name of the user
     * @param token     Authentication token from backend (optional)
     */
    public void createSession(String userId, String userName, String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_TOKEN, token);
        editor.apply();
    }

    /**
     * Check whether a user session is active.
     * @return true if user is logged in
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Get stored user ID.
     * @return user ID or null
     */
    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    /**
     * Get stored user name.
     * @return user name or null
     */
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    /**
     * Get stored authentication token.
     * @return user token or null
     */
    public String getToken() {
        return prefs.getString(KEY_USER_TOKEN, null);
    }

    /**
     * Clear session data and logout user.
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
