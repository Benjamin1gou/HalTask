package local.hal.st32.android.todo40024;

/**
 * Created by Tester on 16/06/15.
 */
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class KetuActivity extends AppCompatActivity{

    ListView listView = null;

    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketu);
        setTitle("ケツテーマ設定");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cursor = DataAccess.ketuAll(KetuActivity.this);
        listView = (ListView) findViewById(R.id.listView2);
        String[] from = {"voice_title"};
        int[] to = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(KetuActivity.this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new listViewOnClickListener());

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class listViewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView list = (ListView) parent;
            Cursor item = (Cursor) list.getItemAtPosition(position);

            int idxId = item.getColumnIndex("_id");
            int idxVoiceTitle = item.getColumnIndex("voice_title");
            int idNo = item.getInt(idxId);
            String title = item.getString(idxVoiceTitle);

            DataAccess.ketuSet(KetuActivity.this,idNo);

            Toast.makeText(KetuActivity.this, title+"に変更しました", Toast.LENGTH_SHORT).show();

        }
    }


}
