package local.hal.st21.android.favoriteshops40024;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

/**
 * Created by ohs40024 on 2016/02/11.
 */

public class ShopEditActivity extends Activity{

    /**
     * 新規登録モードか更新モード化を表すフィールド
     */
    private int _mode = ShopListActivity.MODE_INSERT;

    /**
     * 更新モードの際、現在表示知っているメモ情報のデータアベース上の主キー値
     */
    private int _idNo = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",ShopListActivity.MODE_INSERT);

        if(_mode == ShopListActivity.MODE_INSERT){
            TextView tvTitleEdit = (TextView) findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);

            Button btnSave = (Button) findViewById(R.id.btSave);
            btnSave.setText(R.string.bt_insert);

            Button btnDelete = (Button) findViewById(R.id.btDelete);
            btnDelete.setVisibility(View.INVISIBLE);

        }else{
            _idNo = intent.getIntExtra("idNo", 0);
            Shop shopData = DataAccess.findByPK(ShopEditActivity.this,_idNo);

            EditText etInputStore = (EditText)findViewById(R.id.edInputStore);
            etInputStore.setText(shopData.getName());

            EditText etInputTel = (EditText)findViewById(R.id.edInputTel);
            etInputTel.setText(shopData.getTel());

            EditText etInputUrl = (EditText)findViewById(R.id.edInputUrl);
            etInputUrl.setText(shopData.getUrl());

            EditText etInputNote = (EditText)findViewById(R.id.edInputMemo);
            etInputNote.setText(shopData.getNote());
        }
    }

    /**
     * 登録・更新ボタンが押された時のイベント処理メソッド
     * @param  view 画面部品
     */
    public  void onSaveButtonClick(View view){
        EditText edInputName = (EditText)findViewById(R.id.edInputStore);
        String inputName = edInputName.getText().toString();
        if(inputName.equals("")){
            Toast.makeText(ShopEditActivity.this,R.string.msg_input_store_name,Toast.LENGTH_SHORT).show();
        }else{
            EditText edInputTel = (EditText)findViewById(R.id.edInputTel);
            EditText edInputUrl = (EditText)findViewById(R.id.edInputUrl);
            EditText edInputMemo = (EditText)findViewById(R.id.edInputMemo);

            String inputTel = edInputTel.getText().toString();
            String inputUrl = edInputUrl.getText().toString();
            String inputMemo = edInputMemo.getText().toString();

            if(_mode == ShopListActivity.MODE_INSERT){
                DataAccess.insert(ShopEditActivity.this,inputName,inputTel,inputUrl,inputMemo);
            }else {
                DataAccess.update(ShopEditActivity.this,_idNo,inputName,inputTel,inputUrl,inputMemo);
            }
            finish();
        }
    }

    /**
     * 戻るボタンが押された時のイベント処理用メソッド
     * @param view 画面部品
     */
    public void onBackButtonClick(View view){
        finish();
    }

    /**
     * 削除ボタンが押された時のイベント処理用メソッド
     *
     * @param view 画面部品
     */

    public void onDeleteButtonClick(View view){
        Builder builder = new Builder(ShopEditActivity.this);
        builder.setTitle("削除");
        builder.setMessage("マジで消すのん？");
        builder.setPositiveButton("マジで消す！！", new deleteDialogButtonOnClickListener());
        builder.setNegativeButton("やっぱ消すのやめる～", new deleteDialogButtonOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    /**
     * デリートボタンが押された時の処理が記述されたメンバクラス
     */
    public class deleteDialogButtonOnClickListener implements DialogInterface.OnClickListener {
       @Override
       public void onClick(DialogInterface dialog, int which){
           switch (which){
               case DialogInterface.BUTTON_POSITIVE:
                   DataAccess.delete(ShopEditActivity.this, _idNo);
                   finish();
                   break;
               case DialogInterface.BUTTON_NEGATIVE:
                   break;
           }
       }


    }
}
