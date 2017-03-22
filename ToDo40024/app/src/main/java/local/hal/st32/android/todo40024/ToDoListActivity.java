package local.hal.st32.android.todo40024;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 2016/05/09 課題No2　追加インポート
 */
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;

/**
 * 2016/05/21 課題No3　追加インポート
 */
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * 2016/06/15 課題No5　追加インポート
 */
import java.util.GregorianCalendar;
import java.util.Calendar;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;




public class ToDoListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    ListView listView = null;

    /**
     * プレファレンスファイル名を表す定数フィールド
     */
    private static final String PREFS_NAME = "PSPrefsFile";

    /**
     * 新規登録定数フィールド
     */
    static final int MODE_INSERT = 1;

    /**
     * 更新定数フィールド
     */
    static final int MODE_EDIT = 2;

    Cursor cursor = null;

    private TextToSpeech tts;

    Calendar ca = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int reference = settings.getInt("referenceMode",0);
        switch (reference){
            case 0:
                cursor = DataAccess.findAll(ToDoListActivity.this);
                break;
            case 1:
                cursor = DataAccess.findByDone(ToDoListActivity.this, 1);
                break;
            case 2:
                cursor = DataAccess.findByDone2(ToDoListActivity.this, 0);
                break;
        }

        String[] from = {"done","name","deadline"};
        int[] to = {R.id.rowCompletion, R.id.rowTitle, R.id.rowLimit};
        listView = (ListView) findViewById(R.id.listView);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(ToDoListActivity.this, R.layout.row, cursor, from, to, 0);
        adapter.setViewBinder(new CustomViewBinder());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new listViewOnClickListener());
        registerForContextMenu(listView);

        tts = new TextToSpeech(this,this);

    }

    @Override
    protected void onDestroy(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int ketu = settings.getInt("ketu",0);
        if(ketu == 0) {
            tts.speak("おつ", TextToSpeech.QUEUE_FLUSH, null);
        }
//        if(null != tts){
//            tts.shutdown();
//        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            //リファレンスから値の取得
            int ketu = settings.getInt("ketu",0);
            if(ketu == 0) {
                String goodMoning = DataAccess.ketuGoodMoning(ToDoListActivity.this);

                tts.speak(goodMoning, TextToSpeech.QUEUE_FLUSH, null);
//                goodMoning = "今日は" + (ca.get(Calendar.MONTH) + 1) + "月" + ca.get(Calendar.DAY_OF_MONTH) + "日Death";
//                tts.speak(goodMoning, TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            System.out.println("Oops!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_to_do_menu,menu);
        return true;
    }

    /**
     　リスト表示部分
     */
    @Override
    public void onResume(){
        super.onResume();
        setNewCursor();

    }

    /**
     * 2016/06/14 追加分
     * リストビューのカスタムビューバインダークラス
     */
    private class CustomViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex){
            int viewId = view.getId();
            switch (viewId){
                case R.id.rowCompletion:
                    TextView tvCompletion = (TextView) view;
                    int Completion = cursor.getInt(columnIndex);
                    switch (Completion){
                        case 1:
                            tvCompletion.setText("完");
                            tvCompletion.setTextColor(Color.GRAY);
                            break;

                        default:
                            tvCompletion.setText("未");
                            tvCompletion.setTextColor(Color.RED);
                            break;
                    }
                    return true;
                case R.id.rowTitle:
                    TextView tvTitle = (TextView) view;
                    String Title = cursor.getString(columnIndex);
                    tvTitle.setText(Title);
                    return true;

                case R.id.rowLimit:
                    TextView tvLimit = (TextView) view;
                    String strLimit = cursor.getString(columnIndex);
                    String[]  Limit = strLimit.split("/");
                    int Year = Integer.parseInt(Limit[0]);
                    int Month = Integer.parseInt(Limit[1]);
                    int DayOfMonth = Integer.parseInt(Limit[2]);
                    if(Year == ca.get(Calendar.YEAR) && Month == ca.get(Calendar.MONTH)+1 && DayOfMonth == ca.get(Calendar.DAY_OF_MONTH)){
                        tvLimit.setText("期限：今日やで");
                        tvLimit.setTextColor(Color.RED);
                    }else{
                        tvLimit.setText("期限："+Year+"年"+Month+"月"+DayOfMonth+"日");
                        tvLimit.setTextColor(Color.GRAY);
                    }
                    return true;
            }
            return false;

        }
    }

    /**
     * 2016/06/06 追加分
     * オプションメニューが押された時の処理
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int reference = settings.getInt("referenceMode",0);
        MenuItem menuListOptionTitle = menu.findItem(R.id.menuListOptionTitle);
        MenuItem ketuItem = menu.findItem(R.id.menuKetu);
        int ketu = settings.getInt("ketu", 0);
        switch (reference){
            case 0:
                menuListOptionTitle.setTitle(R.string.menu_all);
                break;
            case 1:
                menuListOptionTitle.setTitle(R.string.menu_completion);
                break;
            case 2:
                menuListOptionTitle.setTitle(R.string.menu_not_completion);
                break;
        }
        switch (ketu){
            case 0:
                ketuItem.setTitle("けつON");
                break;
            case 1:
                ketuItem.setTitle("けつOFF");
                break;
        }
        return true;
    }

    /**
     * 2016/05/10 追加部分
     * オプションメニューを選択した時の処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //選択されたメニューのID取得
        int itemId = item.getItemId();

        switch (itemId){
            //全件出力の時
            case R.id.menuAll:
                editor.putInt("referenceMode", 0);
                cursor = DataAccess.findAll(ToDoListActivity.this);
                break;

            //完了済み表示の時
            case R.id.menuCompletion:
                editor.putInt("referenceMode", 1);
                cursor = DataAccess.findByDone(ToDoListActivity.this, 1);
                break;

            //未完了表示の時
            case R.id.menuNotCompletion:
                editor.putInt("referenceMode", 2);
                cursor = DataAccess.findByDone2(ToDoListActivity.this, 0);
                break;

            case R.id.menuKetuOn:
                editor.putInt("ketu", 0);
                break;
            case R.id.menuKetuOff:
                editor.putInt("ketu", 1);
                break;

            case R.id.ketuSet:
                //遷移先の設定
                Intent intent = new Intent(ToDoListActivity.this, KetuActivity.class);

                //移動処理
                startActivity(intent);
                break;


        }
        editor.commit();
        invalidateOptionsMenu();
        onResume();

        return super.onOptionsItemSelected(item);
    }


    /**
     * リストのアイテムが押された時の処理
     */
    private class listViewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView list = (ListView) parent;
            Cursor item = (Cursor) list.getItemAtPosition(position);

            int idxId = item.getColumnIndex("_id");
            int idNo = item.getInt(idxId);

            //遷移先の設定
            Intent intent = new Intent(ToDoListActivity.this, ToDoEditActivity.class);

            //遷移先に送る値の格納
            intent.putExtra("mode", MODE_EDIT);
            intent.putExtra("idNo", idNo);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            //リファレンスから値の取得
            int ketu = settings.getInt("ketu",0);
            intent.putExtra("ketu", ketu);

            //移動処理
            startActivity(intent);
        }
    }
    /**
     * 新規ボタンを押された時の処理
     */
    public void onNewButtonClick(View view){
        //遷移先の設定
        Intent intent = new Intent(ToDoListActivity.this, ToDoEditActivity.class);

        //遷移先に送る値の設定
        intent.putExtra("mode", MODE_INSERT);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int ketu = settings.getInt("ketu",0);
        intent.putExtra("ketu", ketu);

        //移動処理
        startActivity(intent);
    }

    /**
     * 2016/05/21 追加分 コンテキストメニュー表示用
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("完了？未完了？そ・れ・と・も編集？");
        menu.setHeaderIcon(android.R.drawable.ic_dialog_alert);
    }

    /**
     * コンテキストメニューのアイテム選択時の処理
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        //表示されているリストのポジションを取得
        int listPosition = info.position;

        Cursor item2 = (Cursor) listView.getItemAtPosition(listPosition);
        int idxId = item2.getColumnIndex("_id");
        int idNo = item2.getInt(idxId);
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menuContextCompletion:
                DataAccess.doneUpdate(ToDoListActivity.this, idNo, 1);
                String voiceTitle = DataAccess.ketuLot(ToDoListActivity.this);
                if(!("".equals(voiceTitle))){
                    Toast.makeText(this, voiceTitle, Toast.LENGTH_SHORT).show();
                }
                onResume();
                break;
            case R.id.menuContextNotCompletion:
                DataAccess.doneUpdate(ToDoListActivity.this, idNo, 0);
                onResume();
                break;
            case R.id.menuContextOther:
                //遷移先の設定
                Intent intent = new Intent(ToDoListActivity.this, ToDoEditActivity.class);

                //遷移先に送る値の格納
                intent.putExtra("mode", MODE_EDIT);
                intent.putExtra("idNo", idNo);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                //リファレンスから値の取得
                int ketu = settings.getInt("ketu",0);
                intent.putExtra("ketu", ketu);

                //移動処理
                startActivity(intent);
                break;
        }

        return super.onContextItemSelected(item);

    }

    private void setNewCursor(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int reference = settings.getInt("referenceMode",0);
        switch (reference){
            case 0:
                cursor = DataAccess.findAll(ToDoListActivity.this);
                break;
            case 1:
                cursor = DataAccess.findByDone(ToDoListActivity.this, 1);
                break;
            case 2:
                cursor = DataAccess.findByDone2(ToDoListActivity.this, 0);
                break;
        }
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        adapter.changeCursor(cursor);
    }


}
