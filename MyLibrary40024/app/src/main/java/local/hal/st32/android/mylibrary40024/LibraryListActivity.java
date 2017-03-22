package local.hal.st32.android.mylibrary40024;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.speech.tts.TextToSpeech;


public class LibraryListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int reference = settings.getInt("referenceMode",0);
        switch (reference){
            case 0:
                cursor = DataAccess.findAll(LibraryListActivity.this);
                break;
            case 1:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 0);
                break;
            case 2:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 1);
                break;
            case 3:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 2);
                break;
        }

        String[] from = {"flag","name","category"};
        int[] to = {R.id.rowFlag, R.id.rowTitle, R.id.rowCategory};
        listView = (ListView) findViewById(R.id.listView);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(LibraryListActivity.this, R.layout.row, cursor, from, to, 0);
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
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            //リファレンスから値の取得
            int ketu = settings.getInt("ketu",0);
            if(ketu == 0) {

                String kensu = DataAccess.ketuWashed(LibraryListActivity.this);
                int intKensu = Integer.parseInt(kensu);
                if(intKensu==0) {
                    tts.speak("着るものがないよせんたくしようよ", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } else {
            System.out.println("Oops!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_library_list_menu,menu);
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
     *
     * リストビューのカスタムビューバインダークラス
     */
    private class CustomViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex){
            int viewId = view.getId();
            switch (viewId){
                case R.id.rowFlag:
                    TextView tvCompletion = (TextView) view;
                    int Completion = cursor.getInt(columnIndex);
                    switch (Completion){
                        case 1:
                            tvCompletion.setText("洗");
                            tvCompletion.setTextColor(Color.BLUE);
                            break;

                        case 2:
                            tvCompletion.setText("カ");
                            tvCompletion.setTextColor(Color.RED);
                            break;

                        default:
                            tvCompletion.setText("済");
                            tvCompletion.setTextColor(Color.GRAY);
                            break;
                    }
                    return true;
                case R.id.rowTitle:
                    TextView tvTitle = (TextView) view;
                    String Title = cursor.getString(columnIndex);
                    tvTitle.setText(Title);
                    return true;

                case R.id.rowCategory:
                    TextView tvCategory = (TextView) view;
                    int intCategory = cursor.getInt(columnIndex)+1;

                    String Category = DataAccess.findByCategory(LibraryListActivity.this, intCategory);
                    tvCategory.setText(Category);
                    return true;
            }
            return false;

        }
    }

    /**
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
            case 1:
                menuListOptionTitle.setTitle(R.string.menu_keep_in);
                break;
            case 2:
                menuListOptionTitle.setTitle(R.string.menu_washed);
                break;
            case 3:
                menuListOptionTitle.setTitle(R.string.menu_washing_basket);
                break;

            case 0:
                menuListOptionTitle.setTitle("全て");
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
                cursor = DataAccess.findAll(LibraryListActivity.this);
                break;

            case R.id.menuKeepIn:
                editor.putInt("referenceMode", 1);
                cursor = DataAccess.findByDone(LibraryListActivity.this, 0);
                break;

            //完了済み表示の時
            case R.id.menuWashed:
                editor.putInt("referenceMode", 2);
                cursor = DataAccess.findByDone(LibraryListActivity.this, 1);
                break;

            //未完了表示の時
            case R.id.menuWashingBasket:
                editor.putInt("referenceMode", 3);
                cursor = DataAccess.findByDone(LibraryListActivity.this, 2);
                break;

            case R.id.menuKetuOn:
                editor.putInt("ketu", 0);
                break;
            case R.id.menuKetuOff:
                editor.putInt("ketu", 1);
                break;


            case R.id.categoryIntent:
                Intent intent1 = new Intent(LibraryListActivity.this, CategoryActivity.class);
                startActivity(intent1);
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

            DataAccess.oneUpdateFlag(LibraryListActivity.this, 2, 0, idNo);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            //リファレンスから値の取得
            int ketu = settings.getInt("ketu",0);
            if(ketu == 0) {
                tts.speak("ぽいっ", TextToSpeech.QUEUE_FLUSH, null);
            }
            onResume();
        }
    }
    /**
     * 新規ボタンを押された時の処理
     */
    public void onNewButtonClick(View view){
        //遷移先の設定
        Intent intent = new Intent(LibraryListActivity.this, LibraryEditActivity.class);

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
     * コンテキストメニュー表示用
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
            case R.id.menuContextKeepIn:
                DataAccess.doneUpdate(LibraryListActivity.this, idNo, 0);
                onResume();
                break;
            case R.id.menuContextWashed:
                DataAccess.doneUpdate(LibraryListActivity.this, idNo, 1);
                onResume();
                break;

            case R.id.menuContextWashingBasket:
                DataAccess.doneUpdate(LibraryListActivity.this, idNo, 2);
                onResume();
                break;

            case R.id.menuContextOther:
                //遷移先の設定
                Intent intent = new Intent(LibraryListActivity.this, LibraryEditActivity.class);

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
                cursor = DataAccess.findAll(LibraryListActivity.this);
                break;
            case 1:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 0);
                break;
            case 2:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 1);
                break;
            case 3:
                cursor = DataAccess.findByDone(LibraryListActivity.this, 2);
                break;
        }
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        adapter.changeCursor(cursor);
    }


    public void onWashedClick(View view){
        DataAccess.updateFlag(LibraryListActivity.this, 1, 2);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int ketu = settings.getInt("ketu",0);
        if(ketu == 0) {
            tts.speak("洗濯したよ", TextToSpeech.QUEUE_FLUSH, null);
        }
        onResume();

    }

    public void onKeepInClick(View view){
        DataAccess.updateFlag(LibraryListActivity.this, 0, 1);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //リファレンスから値の取得
        int ketu = settings.getInt("ketu",0);
        if(ketu == 0) {
            tts.speak("取り込んだよ", TextToSpeech.QUEUE_FLUSH, null);
        }
        onResume();
    }
}

