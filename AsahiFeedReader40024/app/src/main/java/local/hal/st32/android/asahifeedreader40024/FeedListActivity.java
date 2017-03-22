package local.hal.st32.android.asahifeedreader40024;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.app.ListActivity;
import android.util.Log;
import android.widget.SimpleAdapter;

public class FeedListActivity extends ListActivity {

    private static final String _URL = "http://rss.asahi.com/rss/asahi/newsheadlines.rdf";

    /**
     * フィードを表示するリストビュー
     */
    private List<Map<String, String >> _result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        ListView lvList = getListView();

        RestAccess access = new RestAccess(lvList);
        access.execute(_URL);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView, view, position, id);

        Map<String, String> item = _result.get(position);
        String link = item.get("link");
        Log.e("a",link);
        Intent intent = new Intent(FeedListActivity.this, ShowLinkPageActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }


    /**
     * 非同期でRSSデータを取得するクラス
     */
    private class RestAccess extends AsyncTask<String, Void, List<Map<String, String >>>{

        /**
         * ログに記載するタグ
         */
        private static final String DEBUG_TAG = "RestAccess";

        /**
         * フィードを表示するリストビュー
         */
        private ListView _lvList = null;


        /**
         * コンストラクタ
         */
        public RestAccess(ListView listView) {
            _lvList = listView;
        }

        @Override
        public List<Map<String , String >> doInBackground(String... params){
            String urlStr = params[0];

            HttpURLConnection con = null;
            InputStream is = null;
            List<Map<String, String>> result = null;

            try{
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();

                String xmlStr = is2String(is);
                result = FeedItemListFanctory.createFeedItemList(xmlStr);

            }catch (MalformedURLException ex){
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            }catch (IOException ex){
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }catch (XmlPullParserException ex){
                Log.e(DEBUG_TAG, "xml解析失敗",ex);
            }finally {
                if(con != null){
                    con.disconnect();
                }
                try{
                    if(is!= null){
                        is.close();
                    }
                }catch (IOException ex){
                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                }
            }
            return result;

        }

        @Override
        public void onPostExecute(List<Map<String, String>> result){
            String[] from = {"title", "pubDateStr"};
            int[] to = {android.R.id.text1,android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(FeedListActivity.this, result, android.R.layout.simple_list_item_2, from, to);
            setListAdapter(adapter);
            _result = result;
        }



        /**
         * InputStreamオブジェクトを文字列に変換するメソッド
         * 変換文字コードはUTF-8
         * @param is 変換された文字列
         * @return 変換された文字列
         * @throws IOException 変換に失敗したときに発生
         */
        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0<=(line = reader.read(b))){
                sb.append(b,0,line);
            }
            return  sb.toString();
        }

    }
}
