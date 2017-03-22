package local.hal.st32.android.todo40024;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.widget.DatePicker;


import java.util.Calendar;

/**
 * 2016/06/06 追加インポート
 */
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.content.SharedPreferences;

/**
 * Created by Tester on 16/04/19.
 */
public class ToDoEditActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    /**
     * モードを表すフィールド
     */
    private int _mode = ToDoListActivity.MODE_INSERT;

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
        setContentView(R.layout.activity_to_do_edit);
        _label = (TextView) findViewById(R.id.tvLimit) ;

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",ToDoListActivity.MODE_INSERT);
        _ketu = intent.getIntExtra("ketu", 0);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tts = new TextToSpeech(this,this);

        /**
         * 押されたものが新規のボタンであるか？
         * 正　新規ボタンが押されていた時の処理：タイトル、ボタンのテキスト変更。削除ボタンを出力しなくする
         * 偽　リストビューのアイテムが押されていた時の処理：各入力欄に変更前のデータを出力する
         */
        if(_mode == ToDoListActivity.MODE_INSERT){
            //タイトル名の変更
            TextView tvTitleEdit = (TextView) findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_insert_title);

            //当日の日付取得
            Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            //メインに格納
            _label.setText(mYear + "/" + (mMonth+1) + "/" + mDayOfMonth);
            _deadLine = mYear + "/" + (mMonth+1) + "/" + mDayOfMonth;

        }else{
            //ToDoListActivityより選択されたアイテムの番号を取得
            _idNo = intent.getIntExtra("idNo",0);

            //取得した番号を使用してDBより、データの取得を行う
            Task taskDate = DataAccess.findByPK(ToDoEditActivity.this,_idNo);

            //タスクに名前を表示
            EditText edTaskName = (EditText)findViewById(R.id.edTaskName);
            edTaskName.setText(taskDate.getName());

            //日付の取得
            _deadLine = taskDate.getDeadline();
            _label.setText(taskDate.getDeadline());
            //年月日に分割
            String[] deadLine = _deadLine.split("/",0);
            //メインに格納
            mYear = Integer.parseInt(deadLine[0]);
            mMonth =(Integer.parseInt(deadLine[1]))-1;
            mDayOfMonth = Integer.parseInt(deadLine[2]);


            //完了状態の表示
            Switch swCompletion = (Switch) findViewById(R.id.swCompletion);
            int completion = taskDate.getDone();
            if(completion == 0){
                swCompletion.setChecked(false);
            }else{
                swCompletion.setChecked(true);
            }

            //詳細の表示
            EditText edDetail = (EditText) findViewById(R.id.edDetail);
            edDetail.setText(taskDate.getNote());



        }
    }

    /**
     * 2016/06/06 追加分
     * アクションバーの表示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_to_do_edit_menu, menu);
        MenuItem menuInnerTitle = menu.findItem(R.id.menuUpdate);
        MenuItem menuInnerTitleDel = menu.findItem(R.id.menuDelete);
        if(_mode == ToDoListActivity.MODE_INSERT){
            menuInnerTitle.setTitle(R.string.menu_insert);
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
                EditText edTaskName = (EditText) findViewById(R.id.edTaskName);
                String taskName = edTaskName.getText().toString();

                /**
                 * タスク名が入力されていないか？
                 * 正　タスク名を入力するようトーストを表示
                 * 偽　入力されたほかデータを取得する
                 */
                if(taskName.equals("")){
                    Toast.makeText(ToDoEditActivity.this,"タスク名を入力してください",Toast.LENGTH_SHORT).show();
                }else{
                    //完了状態取得
                    Switch swCompletion = (Switch) findViewById(R.id.swCompletion);
                    boolean checked = swCompletion.isChecked();
                    int Completion = 0;
                    /**
                     * 未完かどうか？
                     * 正　未完=0
                     * 偽　完了=1
                     */
                    if(checked == false){
                        Completion = 0;
                    }else{
                        Completion = 1;
                    }

                    //詳細の取得
                    EditText edDetail = (EditText) findViewById(R.id.edDetail);
                    String detail = edDetail.getText().toString();

                    /**
                     * 新規登録であるか？　モードチェック
                     * 正　新規登録する
                     * 偽　DBに登録されている情報を更新する
                     */
                    if(_mode == ToDoListActivity.MODE_INSERT){
                        DataAccess.insert(ToDoEditActivity.this, taskName, _deadLine, Completion, detail);
                        if(_ketu == 0) {
                            String text = DataAccess.ketuCompletion(ToDoEditActivity.this);
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }else{
                        DataAccess.update(ToDoEditActivity.this, _idNo, taskName, _deadLine, Completion, detail);
                        if(_ketu == 0) {
                            String text = DataAccess.ketuCompletion(ToDoEditActivity.this);
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }

                    finish();
                }
                break;

            case R.id.menuDelete:
                Builder builder = new Builder(ToDoEditActivity.this);
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

        DatePickerDialog dialog = new DatePickerDialog(ToDoEditActivity.this, new DatePickerDialogDateSetListener(), year, month, dayOfMonth);
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
                    DataAccess.delete(ToDoEditActivity.this, _idNo);
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
    public void onTaskName(View view){
        tts.speak("タスク名を入力してください", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onEditClick(View view){
        tts.speak("詳細を入力してください。", TextToSpeech.QUEUE_FLUSH, null);
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        if(null != tts){
//            tts.shutdown();
//        }
//    }
}
