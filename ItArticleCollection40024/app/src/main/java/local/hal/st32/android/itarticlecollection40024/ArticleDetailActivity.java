package local.hal.st32.android.itarticlecollection40024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Tester on 2017/01/10.
 * content: リストアイテムが押された際に表示する詳細情報のActivity
 */

public class ArticleDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        preview();
    }

    private void preview(){
        Intent intent = getIntent();

        TextView tvId = (TextView)findViewById(R.id.no);
        TextView tvTitle = (TextView)findViewById(R.id.title);
        TextView tvUrl = (TextView)findViewById(R.id.url);
        TextView tvComment = (TextView)findViewById(R.id.comment);
        TextView tvStudentId = (TextView)findViewById(R.id.studentId);
        TextView tvSeatNo = (TextView)findViewById(R.id.seatNo);
        TextView tvName = (TextView)findViewById(R.id.name);
        TextView tvCreatedAt = (TextView)findViewById(R.id.createAt);

        tvId.setText(intent.getStringExtra("id"));
        tvTitle.setText(intent.getStringExtra("title"));
        tvUrl.setText(intent.getStringExtra("url"));
        tvComment.setText(intent.getStringExtra("comment"));
        tvStudentId.setText(intent.getStringExtra("student_id"));
        tvSeatNo.setText(intent.getStringExtra("seat_no"));
        tvName.setText(intent.getStringExtra("last_name")+ " "+ intent.getStringExtra("first_name"));
        tvCreatedAt.setText(intent.getStringExtra("created_at"));
    }

    public void intentWeb(View view){
        try{
            TextView url = (TextView)findViewById(R.id.url);
            String strUrl = url.getText().toString();
            strUrl = URLEncoder.encode(strUrl, "UTF-8");
            Uri uri = Uri.parse(strUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }catch (UnsupportedEncodingException ex){
            Log.e("MapSearchActivity", "keyword変換失敗", ex);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
