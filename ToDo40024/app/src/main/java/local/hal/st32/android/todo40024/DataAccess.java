package local.hal.st32.android.todo40024;

/**
 * Created by Tester on 16/04/13.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.Date;
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
        String sql = "SELECT _id, name, deadline, done, note FROM tasks ORDER BY deadline desc";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    /**
     * 主キーによる検索
     * @param context コンテキスト
     * @param id 主キー値
     * @return 主キーに対応するデータを格納したShopオブジェクト。対応するデータが存在しない場合はnull
     */
    public static Task findByPK(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        Task result = null;
        String sql = "SELECT _id, name, deadline, done, note FROM tasks WHERE _id = " + id;

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxName = cursor.getColumnIndex("name");
                int idxDeadLine = cursor.getColumnIndex("deadline");
                int idxDone = cursor.getColumnIndex("done");
                int idxNote = cursor.getColumnIndex("note");
                String name = cursor.getString(idxName);
                String deadline = cursor.getString(idxDeadLine);
                int done = cursor.getInt(idxDone);
                String note = cursor.getString(idxNote);


                result = new Task();
                result.setId(id);
                result.setName(name);
                result.setDeadline(deadline);
                result.setDone(done);
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
     * タスクの新規登録を行うメソッド
     * @param context コンテキスト
     * @param name タスク名
     * @param deadline 期限
     * @param done 完了状態
     * @param note 詳細
     */
    public static void insert (Context context, String name, String deadline, int done, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //SQL作成
        String sql = "INSERT INTO tasks(name, deadline, done, note) VALUES(?,?,?,?)";

        //SQLないの？の部分の置き換え
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, deadline);
        stmt.bindLong(3,done);
        stmt.bindString(4,note);

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
     * @param id 主キー
     * @param name タスク名
     * @param deadline 期限
     * @param done 完了状態
     * @param note 詳細
     */
    public static void update(Context context, int id, String name, String deadline, int done, String note){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "UPDATE tasks SET name = ?, deadline = ?, done = ?, note = ? WHERE _id = ?";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, deadline);
        stmt.bindLong(3,done);
        stmt.bindString(4,note);
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

        String sql = "DELETE FROM tasks WHERE _id = ?";

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
     * 2016/05/10 追加分
     */
    public static Cursor findByDone(Context context, int done){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT _id, name, deadline, done, note FROM tasks WHERE done = "+done+" ORDER BY deadline desc";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    public static Cursor findByDone2(Context context, int done){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT _id, name, deadline, done, note FROM tasks WHERE done = "+done+" ORDER BY deadline ";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    /**
     * 2016/05/21 追加分
     */
    public static void doneUpdate(Context context, int id, int done){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE tasks SET done = ? WHERE _id = ?";

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
     * 2016/06/16 追加分 非推奨
     * ケツ用
     */
    public static String ketuFindByNotComplation(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT COUNT(*) FROM tasks WHERE done = 0";
        Cursor cursor;
        String kensu = "";
        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxCount = cursor.getColumnIndex("COUNT(*)");
                kensu = cursor.getString(idxCount);
            }
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }

        return kensu;
    }

    /**
     * 2016/06/28　追加分
     * ケツよう 挨拶
     */
    public static String ketuGoodMoning(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT number FROM preferences WHERE _id = 1";
        int number = 0;
        Cursor cursor;
        String voice = "";
        String voice2 = "";
        String kensu = "";

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxNumber = cursor.getColumnIndex("number");
                number = cursor.getInt(idxNumber);
            }

            sql = "SELECT voice, voice2 FROM voice WHERE _id = "+number+"";

            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxVoice = cursor.getColumnIndex("voice");
                int idxVoice2 = cursor.getColumnIndex("voice2");
                voice = cursor.getString(idxVoice);
                voice2 = cursor.getString(idxVoice2);
            }


            sql = "SELECT COUNT(*) FROM tasks WHERE done = 0";

            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxCount = cursor.getColumnIndex("COUNT(*)");
                kensu = cursor.getString(idxCount);
            }
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }

        String goodMorning = voice + kensu + voice2;
        Log.e("ケツ挨拶",goodMorning);

        return goodMorning;
    }



    /**
     * ケツよう　完了
     */
    public static String ketuCompletion(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "SELECT number FROM preferences WHERE _id = 1";
        int number = 0;
        Cursor cursor;
        String voice3 = "";

        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxNumber = cursor.getColumnIndex("number");
                number = cursor.getInt(idxNumber);
            }

            sql = "SELECT voice3 FROM voice WHERE _id = "+number+"";

            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxVoice3 = cursor.getColumnIndex("voice3");
                voice3 = cursor.getString(idxVoice3);
            }

        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        Log.e("ケツ挨拶",voice3);
        return voice3;
    }

    /**
     * テーマ抽選よう
     */
    public static String ketuLot(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT COUNT(*) FROM tasks WHERE done = 1";
        Cursor cursor;
        String voiceTitle = "";
        int kensu = 0;
        int kensu2 = 0;
        int id = 0;
        try{
            cursor = db.rawQuery(sql,null);
            if(cursor != null && cursor.moveToFirst()){
                int idxCount = cursor.getColumnIndex("COUNT(*)");
                kensu = cursor.getInt(idxCount);
                Log.e("件数", kensu+"件");
            }
            kensu = kensu%5;
            Log.e("件数", kensu+"件");
            if(kensu == 0) {
                sql = "SELECT COUNT(*) FROM voice WHERE release = 0";
                cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int idxCount = cursor.getColumnIndex("COUNT(*)");
                    kensu2 = cursor.getInt(idxCount);
                    Log.e("のこり件数", kensu2 + "件");
                }
                if (kensu2 != 0) {
                    int i = 0;
                    while (i != 1) {
                        id = (int) (9 * Math.random() + 2);
                        Log.e("ID", "" + id);
                        sql = "SELECT release FROM voice WHERE _id =" + id;
                        cursor = db.rawQuery(sql, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int idxRelease = cursor.getColumnIndex("release");
                            int release = cursor.getInt(idxRelease);
                            Log.e("リリース", "_id" + release);
                            if (release == 0) {
                                i = 1;
                            }
                        }
                    }
                    sql = "UPDATE voice SET release = 1 WHERE _id = " + id;
                    SQLiteStatement stmt = db.compileStatement(sql);
                    db.beginTransaction();
                    try {
                        stmt.executeInsert();
                        db.setTransactionSuccessful();
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.toString());
                    } finally {
                        db.endTransaction();
                    }

                    sql = "SELECT voice_title FROM voice WHERE _id =" + id;
                    cursor = db.rawQuery(sql, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int idxVoiceTitle = cursor.getColumnIndex("voice_title");
                        voiceTitle = cursor.getString(idxVoiceTitle);
                        voiceTitle += "が解放されました";
                    }
                }else{
                    voiceTitle = "もう解放するものがないよアップデート待ってね";
                }
            }
        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        Log.e("テーマ",voiceTitle);
        return voiceTitle;
    }

    /**
     * ケツ設定画面一覧表示よう
     */
    public static Cursor ketuAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT _id, voice_title, voice, voice2, voice3 FROM voice WHERE release = 1";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    /**
     * ケツ設定画面テーマ変更用
     */
    public static void ketuSet(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE preferences SET number = "+id+" WHERE _id = 1";
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