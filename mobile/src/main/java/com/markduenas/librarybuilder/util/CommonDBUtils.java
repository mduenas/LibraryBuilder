package com.markduenas.librarybuilder.util;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.markduenas.librarybuilder.db.GenericDBHelper;
import com.markduenas.librarybuilder.db.LibraryDBHelper;
import com.markduenas.librarybuilder.db.UserSession;
import com.markduenas.librarybuilder.db.UserToken;
import com.markduenas.librarybuilder.db.queryraw;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * The Class CommonDBUtils.
 */
public class CommonDBUtils {

    /**
     * The Constant DB_APPID.
     */
    public static final String DB_APPID = "appid";
    private static final String TAG = "CommonDBUtils";

    /**
     * The Constant DB_FOREIGN_APPID.
     */
    public static final String DB_FOREIGN_APPID = "applicationid";

    public static String parseDBDate(Context context, String s) {

        String value = s;
        if (value != null && CommonUtils.isLong(value)) {

            Date d = new Date(Long.parseLong(value) * 1000);
            value = CommonUtils.formatDate(context, d);
        } else {
            value = "";
        }
        return value;
    }

    public static String getSessionfromDB(Context ctx) {

        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        // get the current user (determined by a login within the last 5
        // minutes)
        // get our query builder from the DAO
        try {
            Dao<UserSession, Integer> daoUser = ssDB.getDao(UserSession.class);
            QueryBuilder<UserSession, Integer> queryBuilder = daoUser.queryBuilder();
            Where<UserSession, Integer> where = queryBuilder.where();
            where.eq("userid", "user");
            PreparedQuery<UserSession> preparedQuery = queryBuilder.prepare();
            List<UserSession> userList = daoUser.query(preparedQuery);
            Log.v("getSessionfromDB", String.format("Attempting to query user table with: %s", preparedQuery));
            if (userList.size() > 0) {
                if (userList.get(0).jsessionid.equals("1234")) {
                    // This is a first run
                    return null;
                }
                // we've got a sessionid grab the first one
                return userList.get(0).jsessionid;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }

        return null;
    }

    public static UserToken getTokenfromDB(Context ctx, String userid) {

        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        // get the current user (determined by a login within the last 5
        // minutes)
        // get our query builder from the DAO
        try {

            Dao<UserToken, Integer> daoUser = ssDB.getDao(UserToken.class);
            QueryBuilder<UserToken, Integer> queryBuilder = daoUser.queryBuilder();
            Where<UserToken, Integer> where = queryBuilder.where();
            where.eq("userid", userid);
            PreparedQuery<UserToken> preparedQuery = queryBuilder.prepare();
            List<UserToken> userList = daoUser.query(preparedQuery);
            Log.v("getTokenfromDB", String.format("Attempting to query user table with: %s", preparedQuery));
            if (userList.size() > 0) {

                return userList.get(0);
            }
        } catch (SQLException e) {

            Log.e(TAG, e.getMessage() + "");
        }

        return null;
    }

    public static void saveSessionToDB(Context ctx, String jsessionid) {
        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        try {
            Dao<UserSession, Integer> daoUser = ssDB.getDao(UserSession.class);
            if (!daoUser.isTableExists()) {
                ssDB.createTable(UserSession.class);
                daoUser.create(createUserSession("user", jsessionid, System.currentTimeMillis()));
            } else {
                daoUser.createOrUpdate(createUserSession("user", jsessionid, System.currentTimeMillis()));
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static void removeSession(Context ctx, String userid) {
        List<UserSession> data = null;
        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        try {
            Dao<UserSession, Integer> daoSession = ssDB.getDao(UserSession.class);
            QueryBuilder<UserSession, Integer> queryBuilder = daoSession.queryBuilder();
            Where<UserSession, Integer> where = queryBuilder.where();
            where.eq("userid", userid);
            PreparedQuery<UserSession> preparedQuery = queryBuilder.prepare();
            data = daoSession.query(preparedQuery);
            if (data != null && data.size() > 0) {
                for (UserSession u : data)
                    daoSession.delete(u);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static void saveCloudTokenToDB(Context ctx, String userid, String cloudtoken) {
        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        try {
            Dao<UserToken, Integer> daoUser = ssDB.getDao(UserToken.class);
            if (!daoUser.isTableExists()) {
                ssDB.createTable(UserToken.class);
                daoUser.create(createUserToken(userid, cloudtoken, System.currentTimeMillis()));
            } else {
                daoUser.createOrUpdate(createUserToken(userid, cloudtoken, System.currentTimeMillis()));
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static void removeCloudToken(Context ctx, String userid) {

        List<UserToken> data = null;
        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        try {
            Dao<UserToken, Integer> daoUser = ssDB.getDao(UserToken.class);
            QueryBuilder<UserToken, Integer> queryBuilder = daoUser.queryBuilder();
            Where<UserToken, Integer> where = queryBuilder.where();
            where.eq("userid", userid);
            PreparedQuery<UserToken> preparedQuery = queryBuilder.prepare();
            data = daoUser.query(preparedQuery);
            if (data != null && data.size() > 0) {
                for (UserToken u : data)
                    daoUser.delete(u);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static void expireSession(Context ctx) {
        LibraryDBHelper ssDB = LibraryDBHelper.createInstance(ctx);
        try {
            Dao<UserSession, Integer> daoUser = ssDB.getDao(UserSession.class);
            if (!daoUser.isTableExists()) {
                daoUser.create(createUserSession("user", "", System.currentTimeMillis()));
            } else {
                daoUser.createOrUpdate(createUserSession("user", "", System.currentTimeMillis()));
            }
            //CommonUtils.makeShortToast(ctx, "Session cleared");
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static UserSession createUserSession(String userid, String jsessionid, long currentTimeMillis) {
        UserSession u = new UserSession();
        u.userid = userid;
        u.jsessionid = jsessionid;
        u.lastlogin = currentTimeMillis;
        return u;

    }

    public static UserToken createUserToken(String userid, String cloudtoken, long currentTimeMillis) {
        UserToken u = new UserToken();
        u.userid = userid;
        u.cloudtoken = cloudtoken;
        u.lastlogin = currentTimeMillis;
        return u;

    }

    /**
     * Addquote.
     *
     * @param currentScanNumber the current scan number
     * @return the string
     */
    public static String addquote(String currentScanNumber) {
        return "'" + currentScanNumber + "'";
    }

    /**
     * Fire scan table exists.
     *
     * @param dbHelper  the ctx
     * @param tableName the table name
     * @return true, if successful
     */
    public static boolean tableExists(GenericDBHelper dbHelper, String tableName) {
        try {
            GenericRawResults<String[]> genResult = dbHelper.queryRaw(queryraw.class, String.format("SELECT name FROM sqlite_master WHERE type='table' AND name='%s'", tableName));
            if (genResult != null) {
                List<String[]> results = genResult.getResults();
                if (results.size() > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }
        return false;
    }
}
