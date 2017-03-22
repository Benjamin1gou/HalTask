package local.hal.st21.android.saigoku3340024;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 第２画面行事用アクティビティ
 * 寺リスト編集画面を表示する
 * 作成日:2016/01/29
 * 作成者:t.ueshima
 */
public class TempleEditActivity extends  Activity{

    /**
     * 寺リスト画面で選択されたリストの行番号
     */
    private int _selectedTempleNo = 0;

    /**
     * 寺リスト画面で選択された寺名
     */
    private String _selectedTempleName = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_edit);

        //第１画面の値を入手
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //ちゃんと送られていたら受け取る
        if(extras != null){
            _selectedTempleNo = extras.getInt("selectedTempleNo");
            _selectedTempleName = extras.getString("selectedTempleName");
        }

        //選択されたレコードを検索
        Temple content = DataAccess.findByPK(TempleEditActivity.this, _selectedTempleNo);

        //寺名出力
        TextView tvTemple = (TextView) findViewById(R.id.tvTemple);
        tvTemple.setText(_selectedTempleName);


        if(content != null) {
            //本尊名出力
            EditText edHonzon = (EditText) findViewById(R.id.edHonzon);
            edHonzon.setText(content.getHonzon());

            //宗旨出力
            EditText edShushi = (EditText) findViewById(R.id.edShushi);
            edShushi.setText(content.getShushi());

            //所在地出力
            EditText edPreace = (EditText) findViewById(R.id.edPreace);
            edPreace.setText(content.getAddress());

            //URL出力
            EditText edUrl = (EditText) findViewById(R.id.edUrl);
            edUrl.setText(content.getUrl());

            //感想出力
            EditText edReview = (EditText) findViewById(R.id.edReview);
            edReview.setText(content.getNote());
        }

        //保存ボタンが押された時の処理
        Button btSave = (Button) findViewById(R.id.btSave);
        btSave.setOnClickListener( new ButtonClickListener());
    }

    /**
     * ボタンがクリックされた時の処理が記述されたメンバクラス
     * DBにデータを保存する
     * @author ohs40024
     */
    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //入力された値の取得
            //本尊名入力
            EditText edHonzon = (EditText) findViewById(R.id.edHonzon);
            String honzon = edHonzon.getText().toString();

            //宗旨入力
            EditText edShushi = (EditText) findViewById(R.id.edShushi);
            String shushi = edShushi.getText().toString();

            //所在地入力
            EditText edPreace = (EditText) findViewById(R.id.edPreace);
            String address = edPreace.getText().toString();

            //URL入力
            EditText edUrl = (EditText) findViewById(R.id.edUrl);
            String url = edUrl.getText().toString();

            //感想入力
            EditText edReview = (EditText) findViewById(R.id.edReview);
            String note = edReview.getText().toString();

            //DB内にデータが存在するかのチェック
            boolean exist = DataAccess.findRowByPK(TempleEditActivity.this,_selectedTempleNo);
            if(exist){
                //更新
                DataAccess.update(TempleEditActivity.this,_selectedTempleNo,_selectedTempleName,honzon,shushi,address,url,note);
            }else{
                //新規挿入
                DataAccess.insert(TempleEditActivity.this,_selectedTempleNo,_selectedTempleName,honzon,shushi,address,url,note);
            }
            finish();
        }

    }
}