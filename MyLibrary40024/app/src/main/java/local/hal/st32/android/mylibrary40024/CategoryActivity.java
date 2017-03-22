package local.hal.st32.android.mylibrary40024;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import android.widget.Toast;

/**
 * Created by Tester on 16/07/13.
 */
public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_category,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menuUpdate:
                //入力されたタスク名を取得
                EditText edCategoryName = (EditText) findViewById(R.id.edCategoryName);
                String categoryName = edCategoryName.getText().toString();

                /**
                 * タスク名が入力されていないか？
                 * 正　タスク名を入力するようトーストを表示
                 * 偽　入力されたほかデータを取得する
                 */
                if(categoryName.equals("")){
                    Toast.makeText(CategoryActivity.this,"名前を入力してください",Toast.LENGTH_SHORT).show();
                }else{
                    DataAccess.categoryInsert(CategoryActivity.this, categoryName);
                    finish();
                }
                break;


            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
