package skytechhub.myaccounts.AppController;//package com.sawasdeethaispa.sawasdeefamilythaispav10.AppController;
//
///**
// * Created by Asiya A Khatib on 10/6/2015.
// */
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.healthcare.domain.UserIndentifier;
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//
//import java.sql.SQLException;
//
//public final class DataBaseHandler extends OrmLiteSqliteOpenHelper {
//    private static final String DATABASE_NAME = "ReceiptSaverDB";
//    private static final int DATABASE_VERSION = 1;
//    private ConnectionSource connectionSource;
//    private static DataBaseHandler databaseHandler;
//
//    public DataBaseHandler(Context context, String dataBaseFolder) {
//        super(context, dataBaseFolder + DATABASE_NAME, null, DATABASE_VERSION);
//        this.getWritableDatabase();
//        this.connectionSource = getConnectionSource();
//    }
//
//    public static DataBaseHandler getDatabaseHandler(Context context, String dataBaseFolderName) {
//        if (databaseHandler == null) {
//            databaseHandler = new DataBaseHandler(context, dataBaseFolderName);
//        }
//        return databaseHandler;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
//        try {
//            this.connectionSource = getConnectionSource();
//            createOrDrop(connectionSource, "create");
//
//        } catch (SQLException e) {
//        }
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
//        try {
//            createOrDrop(connectionSource, "drop");
//            onCreate(sqliteDatabase, connectionSource);
//        } catch (SQLException e) {
//
//        }
//    }
//
//    public <T> void clearTable(Class<T> entityClass) {
//        try {
//            if (connectionSource == null) {
//                connectionSource = getConnectionSource();
//            }
//
//            TableUtils.clearTable(connectionSource, entityClass);
//
//        } catch (SQLException e) {
//            Log.e("ERROR", e.getMessage(), e);
//        }
//
//    }
//
//    public void createOrDrop(ConnectionSource connectionSource, final String createOrDrop) throws SQLException {
//
//
//        if (createOrDrop.equals("create")) {
//            TableUtils.createTableIfNotExists(connectionSource, UserIndentifier.class);
//
//        } else {
//
//        }
//    }
//
//    public ConnectionSource getConnectionsource() {
//        return connectionSource;
//    }
//
//    public void setConnectionsource(ConnectionSource connectionsource) {
//        this.connectionSource = connectionsource;
//    }
//
//
//}
