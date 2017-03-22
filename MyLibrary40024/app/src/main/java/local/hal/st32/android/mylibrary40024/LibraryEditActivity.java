package local.hal.st32.android.mylibrary40024;

/**
 * Created by Tester on 16/07/13.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.widget.DatePicker;


import java.util.Calendar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.support.v7.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.widget.Spinner;
import android.widget.SimpleCursorAdapter;

public class LibraryEditActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    /**
     * モードを表すフィールド
     */
    private int _mode = LibraryListActivity.MODE_INSERT;

    /**
     * 更新の際、現在表示しているメモ情報のデータアクセス上の主キー値
     */
    private int _idNo = 0;

    /**
     * 日付を表示するテキストビュー
     */
    private TextView _label = null;

    /**
     * 期限登録用フィールド
     */
    private String _deadLine = null;

    /**
     * メイン年数
     */
    private int mYear = 0;

    /**
     * メイン月
     */
    private int mMonth = 0;

    /**
     * メイン日
     */
    private int mDayOfMonth =0;

    private TextToSpeech tts;

    private int _ketu = 0;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setTheme(R.style.MyLightTheme);
        setContentView(R.layout.activity_library_edit);
        _label = (TextView) findViewById(R.id.tvPurchase) ;

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",LibraryListActivity.MODE_INSERT);
        _ketu = intent.getIntExtra("ketu", 0);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tts = new TextToSpeech(this,this);

        /**
         * 押されたものが新規のボタンであるか？
         * 正　新規ボタンが押されていた時の処理：タイトル、ボタンのテキスト変更。削除ボタンを出力しなくする
         * 偽　リストビューのアイテムが押されていた時の処理：各入力欄に変更前のデータを出力する
         */
        if(_mode == LibraryListActivity.MODE_INSERT){

            //当日の日付取得
            Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            //メインに格納
            _label.setText(mYear + "/" + (mMonth+1) + "/" + mDayOfMonth);
            _deadLine = mYear + "/" + (mMonth+1) + "/" + mDayOfMonth;

            Cursor cuCategory = DataAccess.categoryAll(LibraryEditActivity.this);
            String[] from = {"name"};
            int[] to = {android.R.id.text1};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(LibraryEditActivity.this,android.R.layout.simple_list_item_1,cuCategory,from,to,0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = (Spinner)findViewById(R.id.spCategory);
            spinner.setAdapter(adapter);


            cuCategory = DataAccess.seasonAll(LibraryEditActivity.this);
            adapter = new SimpleCursorAdapter(LibraryEditActivity.this,android.R.layout.simple_list_item_1,cuCategory,from,to,0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner1 = (Spinner)findViewById(R.id.spSeason);
            spinner1.setAdapter(adapter);

        }else{
            //ToDoListActivityより選択されたアイテムの番号を取得
            _idNo = intent.getIntExtra("idNo",0);

            //取得した番号を使用してDBより、データの取得を行う
            clothes clotheDate = DataAccess.findByPK(LibraryEditActivity.this,_idNo);

            //タスクに名前を表示
            EditText edTaskName = (EditText)findViewById(R.id.edName);
            edTaskName.setText(clotheDate.get_name());

            //日付の取得
            _deadLine = clotheDate.get_purchase();
            _label.setText(clotheDate.get_purchase());
            //年月日に分割
            String[] deadLine = _deadLine.split("/",0);
            //メインに格納
            mYear = Integer.parseInt(deadLine[0]);
            mMonth =(Integer.parseInt(deadLine[1]))-1;
            mDayOfMonth = Integer.parseInt(deadLine[2]);



            Cursor cuCategory = DataAccess.categoryAll(LibraryEditActivity.this);
            String[] from = {"name"};
            int[] to = {android.R.id.text1};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(LibraryEditActivity.this,android.R.layout.simple_list_item_1,cuCategory,from,to,0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = (Spinner)findViewById(R.id.spCategory);
            spinner.setAdapter(adapter);
            spinner.setSelection(clotheDate.get_category()+1);

            cuCategory = DataAccess.seasonAll(LibraryEditActivity.this);
            adapter = new SimpleCursorAdapter(LibraryEditActivity.this,android.R.layout.simple_list_item_1,cuCategory,from,to,0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner1 = (Spinner)findViewById(R.id.spSeason);
            spinner1.setAdapter(adapter);
            spinner.setSelection(clotheDate.get_season()+1);

        }
    }

    /**
     * 2016/06/06 追加分
     * アクションバーの表示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_library_edit_menu, menu);
        MenuItem menuInnerTitle = menu.findItem(R.id.menuUpdate);
        MenuItem menuInnerTitleDel = menu.findItem(R.id.menuDelete);
        if(_mode == LibraryListActivity.MODE_INSERT){
            menuInnerTitle.setTitle("登録");
            menuInnerTitleDel.setVisible(false);
        }


        return true;
    }

    /**
     * 2016/06/06 追加分
     * アクションバーのボタンが選択された時の処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menuUpdate:
                //入力されたタスク名を取得
                EditText edTaskName = (EditText) findViewById(R.id.edName);
                String taskName = edTaskName.getText().toString();

                /**
                 * タスク名が入力されていないか？
                 * 正　タスク名を入力するようトーストを表示
                 * 偽　入力されたほかデータを取得する
                 */
                if(taskName.equals("")){
                    Toast.makeText(LibraryEditActivity.this,"名前を入力してください",Toast.LENGTH_SHORT).show();
                }else{
                    Spinner spinner1 = (Spinner)findViewById(R.id.spCategory);
                    int category = spinner1.getSelectedItemPosition();

                    Spinner spinner2 = (Spinner)findViewById(R.id.spSeason);
                    int season = spinner2.getSelectedItemPosition();

                    /**
                     * 新規登録であるか？　モードチェック
                     * 正　新規登録する
                     * 偽　DBに登録されている情報を更新する
                     */
                    if(_mode == LibraryListActivity.MODE_INSERT){
                        DataAccess.insert(LibraryEditActivity.this, taskName, category, season, _deadLine);
                    }else{
                        DataAccess.update(LibraryEditActivity.this, _idNo, taskName, category, season, _deadLine);

                    }

                    finish();
                }
                break;

            case R.id.menuDelete:
                Builder builder = new Builder(LibraryEditActivity.this);
                builder.setTitle("タスク削除");
                builder.setMessage("消しちゃうよ？");
                builder.setPositiveButton("消しちゃう！！", new deleteDialogButtonOnClickListener());
                builder.setNegativeButton("消しませ〜ん", new deleteDialogButtonOnClickListener());
                AlertDialog dialog =builder.create();
                dialog.show();
                break;

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * 日付変更ボタンを押された時の処理メソッド
     */
    public void onChangeButtonClick(View view){
        //メインをサブに格納
        int year = mYear;
        int month = mMonth;
        int dayOfMonth = mDayOfMonth;

        DatePickerDialog dialog = new DatePickerDialog(LibraryEditActivity.this, new DatePickerDialogDateSetListener(), year, month, dayOfMonth);
        dialog.show();
    }

    /**
     * 日付変更後の処理メソッド
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            //サブをメインに格納
            mYear = year;
            mMonth = monthOfYear;
            mDayOfMonth = dayOfMonth;

            _label.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
            _deadLine = year + "/" + (monthOfYear+1) + "/" + dayOfMonth;

        }
    }




    public class deleteDialogButtonOnClickListener implements DialogInterface.OnClickListener{
        @Override
        public  void onClick(DialogInterface dialog, int which){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    DataAccess.delete(LibraryEditActivity.this, _idNo);
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

        } else {
            System.out.println("Oops!");
        }
    }

}
