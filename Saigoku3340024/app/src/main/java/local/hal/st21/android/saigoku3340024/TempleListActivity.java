package local.hal.st21.android.saigoku3340024;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一画面
 * 寺の一覧を出力する
 * 作成日:2016/01/29
 * 作成者:t.ueshima
 */
public class TempleListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_list);

        ListView lvTemple = (ListView) findViewById(R.id.lv_temple);

        List<String> templeList = createTempleList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TempleListActivity.this,android.R.layout.simple_list_item_1,templeList);

        lvTemple.setAdapter(adapter);
        lvTemple.setOnItemClickListener(new ListItemClickListener());
    }

    /**
     * 寺院リストを生成するメソッド
     * @return 寺院リストオブジェクト
     */

    private List<String> createTempleList(){
        List<String> templeList = new ArrayList<String>();
        templeList.add("第一番　青岸渡寺");
        templeList.add("第二番　金剛宝寺");
        templeList.add("第三番　粉河寺");
        templeList.add("第四番　施福寺");
        templeList.add("第五番　葛井寺");
        templeList.add("第六番　南法華寺");
        templeList.add("第七番　岡寺");
        templeList.add("第八番　長谷寺");
        templeList.add("第十番　三室戸寺");
        templeList.add("第十一番　上醍醐寺");
        templeList.add("第十二番　正法寺");
        templeList.add("第十三番　石山寺");
        templeList.add("第十四番　三井寺");
        templeList.add("第十五番　今熊野観音寺");
        templeList.add("第十六番　清水寺");
        templeList.add("第十七番　六波羅蜜寺");
        templeList.add("第十八番　六角堂");
        templeList.add("第十九番　行願寺");
        templeList.add("第二十番　善峯寺");
        templeList.add("第二十一番　穴太寺");
        templeList.add("第二十二番　総持寺");
        templeList.add("第二十三番　勝尾寺");
        templeList.add("第二十四番　中山寺");
        templeList.add("第二十五番　清水寺");
        templeList.add("第二十六番　一乗寺");
        templeList.add("第二十七番　圓教寺");
        templeList.add("第二十八番　成相寺");
        templeList.add("第二十九番　松尾寺");
        templeList.add("第三十番　宝厳寺");
        templeList.add("第三十一番　長寿寺");
        templeList.add("第三十二番　観音正寺");
        templeList.add("第三十三番　華厳寺");
        return templeList;
    }

    /**
     * リストが選択された時の処理がされたメンバクラス
     * @author ohs40024
     *
     */
    private class ListItemClickListener implements  OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            ListView listView = (ListView) parent;
            String templeName = (String) listView.getItemAtPosition(position);

            //受け渡し先
            Intent intent = new Intent(TempleListActivity.this,TempleEditActivity.class);
            //受け渡す値
            intent.putExtra("selectedTempleNo",position);
            intent.putExtra("selectedTempleName",templeName);
            startActivity(intent);
        }
    }
}
