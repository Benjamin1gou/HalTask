package local.hal.st32.android.mylibrary40024;

/**
 * Created by Tester on 16/07/08.
 */
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    /**
     * データベースファイル名の定数フィールド
     */
    private static final String DATABASE_NAME = "library.db";

    /**
     * バージョン情報の定数フィールド
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ
     */
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * メモ
     * _id ID
     * name　服名
     * bdata 画像情報
     * category 洗濯物種類
     * season 季節
     * flag 洗濯フラグ
     * purchase 購入日
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE clothes (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL,");
        sb.append("category INTEGER,");
        sb.append("season INTEGER,");
        sb.append("flag INTEGER,");
        sb.append("purchase DATE");
        sb.append(");");
        String sql = sb.toString();
        db.execSQL(sql);

        sb = new StringBuffer();
        sb.append("CREATE TABLE categorys (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL");
        sb.append(");");
        sql = sb.toString();
        db.execSQL(sql);

        sb = new StringBuffer();
        sb.append("CREATE TABLE season(");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL");
        sb.append(");");
        sql = sb.toString();
        db.execSQL(sql);

        ArrayList<String> rec = new ArrayList<String>();
        rec.add("INSERT INTO season (name) VALUES('春')");
        rec.add("INSERT INTO season (name) VALUES('夏')");
        rec.add("INSERT INTO season (name) VALUES('秋')");
        rec.add("INSERT INTO season (name) VALUES('冬')");
        SQLiteStatement stmt;
        for (String date: rec){
            stmt = db.compileStatement(date);
            stmt.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
