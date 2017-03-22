package local.hal.st21.android.favoriteshops40024;

/**
 * Created by ohs40024 on 2016/02/11.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;



public class DataAccess {
    /**
     * 全件データ検索メソッド
     *
     * @param context コンテキスト
     * @return 検索結果のCursorオブジェクト
     */
    public static Cursor findAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT _id, name, tel, url, note FROM shops ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    /**
     * 主キーによる検索
     * @param context コンテキスト
     * @param id 主キー値
     * @return 主キーに対応するデータを格納したShopオブジェクト。対応するデータが存在しない場合はnull
     */
    public static Shop findByPK(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        Shop result = null;
        String sql = "SELECT _id, name, tel, url, note FROM shops WHERE _id = " + id;

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxName = cursor.getColumnIndex("name");
                int idxTel = cursor.getColumnIndex("tel");
                int idxUrl = cursor.getColumnIndex("url");
                int idxNote = cursor.getColumnIndex("note");
                String name = cursor.getString(idxName);
                String tel = cursor.getString(idxTel);
                String url = cursor.getString(idxUrl);
                String note = cursor.getString(idxNote);


                result = new Shop();
                result.setId(id);
                result.setName(name);
                result.setTel(tel);
                result.setUrl(url);
                result.setNote(note);

            }

        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        return result;
    }

    /**
     * 店情報を更新するメソッド
     * @param context コンテキスト
     * @param id 主キー値
     * @param name 店名
     * @param tel 電話番号
     * @param url URL
     * @param note メモ
     */
    public static void update(Context context, int id, String name, String tel, String url, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "UPDATE shops SET name = ?, tel = ?, url = ?, note = ? WHERE _id = ?";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, tel);
        stmt.bindString(3, url);
        stmt.bindString(4, note);
        stmt.bindLong(5, id);

        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e("ERROR",ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 店情報を新規登録するメソッド
     * @param context コンテキスト
     * @param name 店名
     * @param tel 電話番号
     * @param url URL
     * @param note メモ
     */
    public static void insert(Context context, String name, String tel, String url, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "INSERT INTO shops(name,tel,url,note) VALUES (?,?,?,?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, tel);
        stmt.bindString(3, url);
        stmt.bindString(4, note);

        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e("ERROR",ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 店情報を削除するメソッド
     * @param context コンテキスト
     * @param id 主キー値
     */
    public static void delete(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "DELETE FROM shops WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1,id);

        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e("ERROR",ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }
}