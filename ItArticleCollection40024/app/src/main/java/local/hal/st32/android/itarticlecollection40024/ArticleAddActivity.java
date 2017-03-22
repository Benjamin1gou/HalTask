/**
 * Created by Tester on 2017/01/10.
 * content: 新規登録ボタンを押された際に表示するアクティビティ
 */
package local.hal.st32.android.itarticlecollection40024;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class ArticleAddActivity extends AppCompatActivity {

    private static final String _url = "http://hal.architshin.com/st32/insertItArticle.php";

    private Map<String, String> previewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_add);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_add_meun, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.menuInsert:
                TextView tvTitle = (TextView)findViewById(R.id.articleTitle);
                TextView tvUrl = (TextView)findViewById(R.id.articleUrl);
                TextView tvComment = (TextView)findViewById(R.id.articleComment);

                String title = tvTitle.getText().toString();
                String url = tvUrl.getText().toString();
                String comment = tvComment.getText().toString();

                RestAccess access = new RestAccess();
                access.execute(title, url, comment);

                break;

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RestAccess extends AsyncTask<String, Void, String> {
        private static final String DEBUG_TAG = "RestAccess";
        String result;



        @Override
        public String doInBackground(String... params) {
            HttpURLConnection con = null;
            InputStream is = null;
            String postDate = "title=" + params[0] + "&url="+params[1] + "&comment=" + params[2] + "&lastname=上島&firstname=崇寛&studentid=40024&seatno=4";

            try {
                java.net.URL url = new URL(_url);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postDate.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if(status != 200){
                    throw  new IOException("ステータスコード："+status);
                }
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

            re.setRequestId("title");
            re.setRequestId("url");
            re.setRequestId("comment");
            re.setRequestId("name");
            re.setRequestId("studentid");
            re.setRequestId("seatno");
            re.setRequestId("status");
            re.setRequestId("msg");
            re.setRequestId("timestamp");
            previewDate = re.oneColmunJson(result);
            Log.e("", ""+previewDate);

            if(previewDate.get("status").equals("1")){
                finish();
            }else{
                TextView err = (TextView)findViewById(R.id.errMS);
                err.setText(previewDate.get("msg"));
            }

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
}
