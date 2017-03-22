package local.hal.st21.android.saigoku3340024;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
/**
 * データベース上のデータを処理するクラス
 * @author ohs40024
 */
public class DataAccess {

    /**
     * 主キーによる寺情報検索メソッド
     * @param context コンテキスト
     * @param id 主キー。
     * @return 主キーに対応するcontentカラムの値
     */
    public static Temple findByPK(Context context, int id ){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        Temple result = null;
        //SQLの作成
        String sql = "SELECT name,honzon,shushi,address,url,note FROM temples WHERE _id = " + id;

        try{
            //SQLの実行
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                //各カラムのインデックス番号の取得
                int idxName = cursor.getColumnIndex("name");
                int idxHonzon = cursor.getColumnIndex("honzon");
                int idxShushi = cursor.getColumnIndex("shushi");
                int idxAddress = cursor.getColumnIndex("address");
                int idxUrl = cursor.getColumnIndex("url");
                int idxNote = cursor.getColumnIndex("note");

                //インデックス番号を使用し、値を取得する
                String name = cursor.getString(idxName);
                String honzon = cursor.getString(idxHonzon);
                String shushi = cursor.getString(idxShushi);
                String address = cursor.getString(idxAddress);
                String url = cursor.getString(idxUrl);
                String note = cursor.getString(idxNote);

                //Templeに値を格納していく
                result = new Temple();
                result.setId(id);
                result.setName(name);
                result.setHonzon(honzon);
                result.setShushi(shushi);
                result.setAddress(address);
                result.setUrl(url);
                result.setNote(note);

            }
        }catch(Exception ex){
            Log.e("ERROR" ,ex.toString());
        }finally{
            db.close();
        }
        return result;
    }

    /**
     * 主キーによるレコード存在チェックメソッド
     * @param  context コンテキスト
     * @param id 主キー値
     * @return 主キーに対応するcontentカラムの値
     */
    public static boolean findRowByPK(Context context ,int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        boolean result = false;
        //SQLの作成
        String sql = "SELECT COUNT(*) AS count FROM temples WHERE _id = " + id;

        try{
            //SQLの実行
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxCount = cursor.getColumnIndex("count");
                int count = cursor.getInt(idxCount);
                if(count >= 1){
                    result = true;
                }
            }
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
//        System.out.println("result内"+result);
        return result;
    }

    /**
     * 寺情報を更新するメソッド
     * @param context コンテキスト
     * @param id 主キーの値
     * @param name 寺名
     * @param honzon　本尊名
     * @param shushi　宗旨
     * @param address　所在地
     * @param url　URL
     * @param note　感想
     */
    public static void update(Context context, int id , String name, String honzon, String shushi, String address, String url, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //SQLの作成
        String sql = "UPDATE temples SET name = ?, honzon = ? , shushi = ? , address = ? , url = ? , note = ? WHERE _id = ? ";
        SQLiteStatement stmt = db.compileStatement(sql);
        //?と値を入れ替え
        stmt.bindString(1, name);
        stmt.bindString(2, honzon);
        stmt.bindString(3, shushi);
        stmt.bindString(4, address);
        stmt.bindString(5, url);
        stmt.bindString(6, note);
        stmt.bindLong(7,id);

        //ﾄﾗﾝｻﾞｸｼｮﾝの開始
        db.beginTransaction();
        try{
            //SQLの実行
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            //ﾄﾗﾝｻﾞｸｼｮﾝ終了
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 寺情報を新規登録するメソッド
     * @param context コンテキスト。
     * @param id 主キーの値
     * @param name 寺名
     * @param honzon　本尊名
     * @param shushi　宗旨
     * @param address　所在地
     * @param url　URL
     * @param note　感想
     */

    public static void insert(Context context, int id , String name, String honzon, String shushi, String address, String url, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        //SQLの作成
        String sql = "INSERT INTO temples(_id,name,honzon,shushi,address,url,note) VALUES(?,?,?,?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        //？と値の入れ替え
        stmt.bindLong(1,id);
        stmt.bindString(2, name);
        stmt.bindString(3, honzon);
        stmt.bindString(4, shushi);
        stmt.bindString(5, address);
        stmt.bindString(6, url);
        stmt.bindString(7, note);

        //ﾄﾗﾝｻﾞｸｼｮﾝの開始
        db.beginTransaction();
        try{
            //SQLの実行
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            //ﾄﾗﾝｻﾞｸｼｮﾝの終了
            db.endTransaction();
            db.close();
        }

    }
}
