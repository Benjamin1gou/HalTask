package local.hal.st21.android.saigoku3340024;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

/**
 * データベースのヘルパークラス
 * @author ohs40024 .
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    /**
     * データベースファイル名の定数フィールド
     */
    private static final String DATABASE_NAME = "temple.db";

    /**
     * バージョン情報の定数フィールド
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //CREATE文の作成
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE temples (");
        sb.append("_id INTEGER, ");
        sb.append("name TEXT NOT NULL,");
        sb.append("honzon TEXT,");
        sb.append("shushi TEXT,");
        sb.append("address TEXT,");
        sb.append("url TEXT,");
        sb.append("note TEXT,");
        sb.append("PRIMARY KEY (_id));");
        String sql = sb.toString();

        //CREATEの実行
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}