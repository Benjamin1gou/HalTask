package local.hal.st21.android.favoriteshops40024;

import android.content.Intent;
import android.app.ListActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ShopListActivity extends ListActivity {

    /**
     * 新規登録モードを表す定数
     */
    static final int MODE_INSERT = 1;

    /**
     * 更新モードを表す定数フィールド
     */
    static final int MODE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        Cursor cursor = DataAccess.findAll(ShopListActivity.this);
        String [] from = {"name"};
        int[] to = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(ShopListActivity.this,android.R.layout.simple_list_item_1,cursor,from,to,0);
        setListAdapter(adapter);
    }

    /**
     *リスト表示部分
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView, view, position, id);
        Cursor item = (Cursor) listView.getItemAtPosition(position);
        int idxId = item.getColumnIndex("_id");
        int idNo = item.getInt(idxId);

        Intent intent = new Intent(ShopListActivity.this, ShopEditActivity.class);
        intent.putExtra("mode", MODE_EDIT);
        intent.putExtra("idNo", idNo);

        startActivity(intent);
    }

    /**
     * 新規ボタンが押された時のイベント処理用メソッド
     * @param view 画面部品
     */
    public void onNewButtonClick(View view){
        Intent intent = new Intent(ShopListActivity.this, ShopEditActivity.class);
        intent.putExtra("mode", MODE_INSERT);
        startActivity(intent);
    }

}
