package local.hal.st32.android.todo40024;

/**
 * Created by Tester on 16/04/13.
 * DabaseHelperClass
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    /**
     * データベースファイル名の定数フィールド
     */
    private static final String DATABASE_NAME = "todo.db";

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

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE tasks (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL,");
        sb.append("deadline DATE,");
        sb.append("done INTEGER DEFAULT 0,");
        sb.append("note TEXT");
        sb.append(");");

        String sql = sb.toString();

        db.execSQL(sql);

        StringBuffer sb2 = new StringBuffer();
        sb2.append("CREATE TABLE voice(");
        sb2.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb2.append("voice_title TEXT NOT NULL,");
        sb2.append("voice TEXT NOT NULL,");
        sb2.append("voice2 TEXT NOT NULL,");
        sb2.append("voice3 TEXT NOT NULL,");
        sb2.append("release INTEGER DEFAULT 0");
        sb2.append(");");
        sql = sb2.toString();

        db.execSQL(sql);

        StringBuffer sb3 = new StringBuffer();
        sb3.append("CREATE TABLE preferences(");
        sb3.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb3.append("number INTEGER");
        sb3.append(");");
        sql = sb3.toString();

        db.execSQL(sql);

        //preferences初期データ
        sql = "INSERT INTO preferences (number) VALUES(1);";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.executeInsert();

        sql = "INSERT INTO preferences (number) VALUES(0);";
        stmt = db.compileStatement(sql);
        stmt.executeInsert();

        //voice初期データ
        ArrayList<String> rec = new ArrayList<String>();
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3, release) VALUES('デフォルト','おかえりなさいませご主人様。現在未完了のタスクは','件です','かしこまりー',1)");

        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('たまき風','ひーーーーろーーーーまだ未完了のタスクは','件やで','わかったぁー')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('妹風','おかえりおにいちゃん今残ってるお仕事は','件だよ','わかったよー')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('嫁風','おかえりなさいあなた今残ってるお仕事は','件よ','わかったわ')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('女忍者風','おかえりでござる今残ってる仕事は','件でござる','ぎょい')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('ツンデレ風','あんたなんて帰えって来なくたっていいんだからね今残ってる仕事は','件なんだから','しょうがないわね')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('ヤンデレ風','やっと帰ってきたのねずっと待ってたのよ今残ってる仕事は','件よ','あなたのためにおぼえておくわね')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('娘風','ぱぱおかえりー今残ってるお仕事わねー','件だよー','ぱぱがんばりやさんだね')");
        rec.add("INSERT INTO voice (voice_title, voice, voice2, voice3) VALUES('厨二風','よくぞ戻った我が眷属よ今宵ぬしの残した罪は','件のこっている','ぬしはまた罪を増やすのだな')");




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
