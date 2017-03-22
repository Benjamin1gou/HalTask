package local.hal.st32.android.mylibrary40024;

/**
 * Created by Tester on 16/07/08.
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
        String sql = "SELECT _id, name, category, season, flag, purchase FROM clothes";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    /**
     * 主キーによる検索
     * @param context コンテキスト
     * @param id 主キー値
     * @return 主キーに対応するデータを格納したclotheオブジェクト。対応するデータが存在しない場合はnull
     */
    public static clothes findByPK(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        clothes result = null;
        String sql = "SELECT _id, name, category, season, flag, purchase FROM clothes WHERE _id = " + id;

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxName = cursor.getColumnIndex("name");
                int idxCategory = cursor.getColumnIndex("category");
                int idxSeason = cursor.getColumnIndex("season");
                int idxFlag = cursor.getColumnIndex("flag");
                int idxPurchase = cursor.getColumnIndex("purchase");

                String name = cursor.getString(idxName);
                int category = cursor.getInt(idxCategory);
                int season = cursor.getInt(idxSeason);
                int flag = cursor.getInt(idxFlag);
                String purchase = cursor.getString(idxPurchase);



                result = new clothes();
                result.set_id(id);
                result.set_name(name);
                result.set_category(category);
                result.set_season(season);
                result.set_flag(flag);
                result.set_purchase(purchase);

            }

        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        return result;
    }

    /**
     * タスクの新規登録を行うメソッド
     * @param context コンテキスト
     * @param name タスク名
     * @param category 部類
     * @param season 季節
     * @param purchase 登録日
     */
    public static void insert (Context context, String name, int category, int season, String purchase){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //SQL作成
        String sql = "INSERT INTO clothes(name, category, season, flag, purchase) VALUES(?,?,?,0,?)";

        //SQLないの？の部分の置き換え
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindLong(2, category);
        stmt.bindLong(3,season);
        stmt.bindString(4,purchase);

        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch (Exception ex){
            Log.e("ERROR", ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }

    }

    /**
     * タスクの変更を行うメソッド
     * @param context コンテキスト
     * @param id ID
     * @param name タスク名
     * @param category 部類
     * @param season 季節
     * @param purchase 登録日
     */
    public static void update(Context context,int id, String name, int category, int season, String purchase){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "UPDATE clothes SET name = ?, category = ?, season = ?, purchase = ? WHERE _id = ?";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindLong(2, category);
        stmt.bindLong(3,season);
        stmt.bindString(4,purchase);
        stmt.bindLong(5,id);

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
     * タスクの削除を行うメソッド
     * @param context コンテキスト
     * @param id 主キー
     */
    public static void delete(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "DELETE FROM clothes WHERE _id = ?";

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


    /**
     *
     */
    public static Cursor findByDone(Context context, int done){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";

        sql = "SELECT _id, name, category, season, flag, purchase FROM clothes WHERE flag = " + done + "";

        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }


    /**
     * 2016/05/21 追加分
     */
    public static void doneUpdate(Context context, int id, int done){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE clothes SET flag = ? WHERE _id = ?";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1,done);
        stmt.bindLong(2,id);
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
     * カテゴリー抽出用
     */
    public static String findByCategory(Context context, int category){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        String strCategory = "";
        String sql = "SELECT name FROM categorys WHERE _id = " + category;

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxName = cursor.getColumnIndex("name");


                strCategory = cursor.getString(idxName);

            }

        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        return strCategory;
    }

    /**
     * カテゴリー登録
     */
    public static void categoryInsert(Context context, String name){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //SQL作成
        String sql = "INSERT INTO categorys(name) VALUES(?)";

        //SQLないの？の部分の置き換え
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);

        Log.e("カテゴリインサート" , sql);

        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch (Exception ex){
            Log.e("ERROR", ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * カテゴリ抽出
     */
    public static Cursor categoryAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM categorys ";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    public static Cursor seasonAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM season ";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    public static String ketuWashed(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT COUNT(*) FROM clothes WHERE flag = 0";
        Cursor cursor = null;
        String kensu = "";
        try{

            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxCount = cursor.getColumnIndex("COUNT(*)");
                kensu = cursor.getString(idxCount);
                Log.e("件数", kensu+"件");
            }
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        return kensu;
    }

    public static void updateFlag (Context context, int upflag, int flag){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE clothes SET flag = "+upflag+"  WHERE flag = "+flag+"";

        SQLiteStatement stmt = db.compileStatement(sql);
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

    public static void oneUpdateFlag (Context context, int upflag, int flag , int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE clothes SET flag = "+upflag+"  WHERE flag = "+flag+" AND _id = "+id+"";

        SQLiteStatement stmt = db.compileStatement(sql);
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
