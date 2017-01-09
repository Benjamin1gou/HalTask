/**
 * createDate: 2016/01/10
 * creater: Tester
 * content: サーバよりjsonデータを取得し、リストで表示する
 */
package local.hal.st32.android.itarticlecollection40024;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ArticleListActivity extends AppCompatActivity {

    private ListView list = null;
    private List<Map<String,String>> listDate;

    public static final String _url = "http://hal.architshin.com/st32/getItArticlesList.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        list = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume(){
        super.onResume();

        RestAccess access = new RestAccess();
        access.execute();
    }

    private class RestAccess extends AsyncTask<String, Void, String> {
        private static final String DEBUG_TAG = "RestAccess";
        String result;



        @Override
        public String doInBackground(String... params) {
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(_url);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }
            return result;

        }

        @Override
        public void onPostExecute(String result) {
            Replase re = new Replase();

            re.setRequestId("id");
            re.setRequestId("title");
            re.setRequestId("url");
            re.setRequestId("comment");
            re.setRequestId("student_id");
            re.setRequestId("seat_no");
            re.setRequestId("last_name");
            re.setRequestId("first_name");
            re.setRequestId("created_at");
            listDate = re.json(result);
            Log.e("", ""+listDate);
            preview();
        }

    }

    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }

    /**
     * 新規登録ボタンを押された時の処理メソッド
     * @param view
     */
    public void onNewButtonClick(View view){
        Intent intent = new Intent(ArticleListActivity.this, ArticleAddActivity.class);
        //移動処理
        startActivity(intent);
    }

    private void preview(){
        String[] from = {"title","created_at"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(ArticleListActivity.this, listDate,android.R.layout.simple_list_item_2, from, to);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new listViewOnClickListener());
    }

    private class listViewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ListView list = (ListView) parent;
//            Cursor item = (Cursor) list.getItemAtPosition(position);
//
//            int idxId = item.getColumnIndex("_id");
//            int idNo = item.getInt(idxId);
//
//            //遷移先の設定
//            Intent intent = new Intent(ToDoListActivity.this, ToDoEditActivity.class);
//
//            //遷移先に送る値の格納
//            intent.putExtra("mode", MODE_EDIT);
//            intent.putExtra("idNo", idNo);
//            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//            //リファレンスから値の取得
//            int ketu = settings.getInt("ketu",0);
//            intent.putExtra("ketu", ketu);
//
//            //移動処理
//            startActivity(intent);
        }
    }

}
