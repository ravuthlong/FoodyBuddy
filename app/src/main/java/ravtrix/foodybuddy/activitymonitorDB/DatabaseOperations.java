package ravtrix.foodybuddy.activitymonitorDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rlong on 11/8/17.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String CREATE_QUERY = "CREATE TABLE " + ActivityData.TableInfo.DB_TABLE + "("
            + ActivityData.TableInfo.ACTIVITY_TIME + " TEXT," + ActivityData.TableInfo.ACTIVITY_DETAIL + " TEXT);";

    private static final String TAG = "DatabaseOperations";

    public DatabaseOperations(Context context) {
        super(context, ActivityData.TableInfo.DB_NAME, null, DB_VERSION);
        Log.d(TAG, "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "CREATE QUERY: " + CREATE_QUERY);
        db.execSQL(CREATE_QUERY);
        Log.d(TAG, "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertActivity(DatabaseOperations dop, String time, String detail) {
        SQLiteDatabase db = dop.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActivityData.TableInfo.ACTIVITY_TIME, time);
        contentValues.put(ActivityData.TableInfo.ACTIVITY_DETAIL, detail);
        long success = db.insert(ActivityData.TableInfo.DB_TABLE, null, contentValues);
        Log.d(TAG, "Table insertion success: " + success);
    }

    public Cursor getActivities(DatabaseOperations dop) {
        SQLiteDatabase db = dop.getReadableDatabase();
        String[] columns = {ActivityData.TableInfo.ACTIVITY_TIME, ActivityData.TableInfo.ACTIVITY_DETAIL};
        return db.query(ActivityData.TableInfo.DB_TABLE, columns, null, null, null, null, ActivityData.TableInfo.ACTIVITY_TIME + " DESC");
    }
}
