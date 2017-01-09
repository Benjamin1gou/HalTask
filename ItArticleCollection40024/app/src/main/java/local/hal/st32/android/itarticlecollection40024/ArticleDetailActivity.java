package local.hal.st32.android.itarticlecollection40024;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

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


}
