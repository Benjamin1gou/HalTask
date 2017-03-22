package local.hal.st32.android.post2db40024;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Post2DBActivity extends AppCompatActivity {

    private static final String URL = "http://hal.architshin.com/st32/post2DB.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2_db);
    }

    public void insertButtonClick(View view){
        EditText edLastName = (EditText)findViewById(R.id.edLastName);
        EditText edFirstName = (EditText)findViewById(R.id.edFirstName);
        EditText edStudentId = (EditText)findViewById(R.id.edStudentid);
        EditText edSeatNo = (EditText)findViewById(R.id.edSeatNo);
        EditText edMessage = (EditText)findViewById(R.id.edMessage);

        String LastName = edLastName.getText().toString();
        String FirstName = edFirstName.getText().toString();
        String StudentId = edStudentId.getText().toString();
        String SeatNo = edSeatNo.getText().toString();
        String Message = edMessage.getText().toString();

        //ここにRestAccess宣言
        RestAccess access = new RestAccess();
        access.execute(URL, LastName, FirstName, StudentId, SeatNo, Message);
    }

    private class RestAccess extends AsyncTask<String, String, String> {
        private static final String DEBUG_TAG = "RestAccess";
        private boolean _success = false;

        @Override
        public String doInBackground(String... params) {
            String urlStr = params[0];
            String lastname = params[1];
            String firstname = params[2];
            String studentid = params[3];
            String seatno = params[4];
            String message = params[5];

            String postDate = "lastname=" + lastname + "&firstname=" + firstname + "&studentid=" + studentid + "&seatno=" + seatno + "&message=" + message;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                java.net.URL url = new URL(urlStr);
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
                _success = true;

            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                }
            }
            return result;
        }

        @Override
        public void onPostExecute(String result) {
            if (_success) {
                String name = "";
                String studentid= "";
                String seatno="";
                String state = "";
                String msg = "";
                String serialno = "";
                String timestamp = "";
                try {
                    JSONObject rootJSON = new JSONObject(result);
                    name = rootJSON.getString("name");
                    studentid = rootJSON.getString("studentid");
                    seatno = rootJSON.getString("seatno");
                    if(rootJSON.getString("status").equals("1")){
                        state = "成功";
                    }else{
                        state = "失敗";
                    }
                    msg = rootJSON.getString("msg");
                    serialno = rootJSON.getString("serialno");
                    timestamp = rootJSON.getString("timestamp");


                } catch (JSONException ex) {
                    Log.e(DEBUG_TAG, "JSON解析失敗", ex);
                }
                String message = getString(R.string.dlg_name)+name+"\n"+getString(R.string.dlg_studentid)+studentid+"\n"+getString(R.string.dlg_seatno)+seatno+"\n"+getString(R.string.dlg_states)+state+"\n"+msg+"\n"+getString(R.string.dlg_serialno)+serialno+"\n"+getString(R.string.dlg_timestampe)+timestamp;

                AlertDialog.Builder builder = new AlertDialog.Builder(Post2DBActivity.this);
                builder.setTitle(getString(R.string.dlg_title));
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogButtonClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
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

        private class DialogButtonClickListener implements DialogInterface.OnClickListener{
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        }
    }
}
