package com.markduenas.librarybuilder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.markduenas.librarybuilder.db.GenericDBHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides the DAOs used by the other classes.
 */
public class LibraryDBHelper extends GenericDBHelper {

    private static final String TAG = "LibraryDBHelper";

    private static LibraryDBHelper libraryDBHelper = null;
    /**
     * The Constant DATABASE_NAME. Overrides the GenericDBHelper names
     */
    private static final String DATABASE_NAME = "LibraryDB.sqlite";
    /**
     * The Constant DATABASE_VERSION. Overrides the GenericDBHelper names
     */
    private static final int DATABASE_VERSION = 1;

    public static LibraryDBHelper createInstance(Context context) {

        if (libraryDBHelper == null) {
            libraryDBHelper = new LibraryDBHelper(context);
        }
        return libraryDBHelper;
    }

    public static void closeDb() {

        if (libraryDBHelper != null) {
            libraryDBHelper.close();
            libraryDBHelper = null;
        }
    }

    /**
     * Instantiates a new scan series db helper.
     *
     * @param context the context
     */
    private LibraryDBHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);

        // Initalize list of classes/tables to be stored in this database
        tableList.add(UserSession.class);
        tableList.add(UserToken.class);
    }

    public String getDatabasePath() {

        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        return sqLiteDatabase.getPath();
    }

    public <T> Dao<T, Integer> getListDao(Class<T> typeClass) throws SQLException {
        return getDao(typeClass);
    }

    public <T> List<T> getFormList(Class<T> annotationType) {

        List<T> data = null;
        try {
            Dao<T, Integer> ssDao = getObjectDao(annotationType);
            QueryBuilder<T, Integer> queryBuilder = ssDao.queryBuilder();
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            data = ssDao.query(preparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }

        return data;
    }

    public <T> List<T> getDatabaseListFiltered(Class<T> annotationType, String columnName, String value) {
        List<T> data = null;
        try {
            Dao<T, Integer> ssDao = getObjectDao(annotationType);
            QueryBuilder<T, Integer> queryBuilder = ssDao.queryBuilder();
            Where<T, Integer> where = queryBuilder.where();
            where.eq(columnName, value);
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            data = ssDao.query(preparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage() + "");
        }

        return data;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }
}
