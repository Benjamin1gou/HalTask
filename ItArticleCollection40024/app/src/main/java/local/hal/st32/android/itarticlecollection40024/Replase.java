package local.hal.st32.android.itarticlecollection40024;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 2016/11/10.
 *
 */

public class Replase {

    private ArrayList<String> requestId = new ArrayList<String>();

    private ArrayList<String> responseId = new ArrayList<String>();

    static String tableName;

    private static final String DEBUG_TAG = "replaseJson";

    public void setRequestId(String date) {
        requestId.add(date);
    }

    public void setResponseId(String date) {
        responseId.add(date);
    }

    public void setTableName(String name) {
        tableName = name;
    }

    public List<Map<String, String>> json(String result) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {

            Map<String, String> map = new HashMap<String, String>();
            JSONObject rootJSON = new JSONObject(result);
            JSONArray arrayJson = rootJSON.getJSONArray("list");
            for (int i = 0; i < arrayJson.length(); i++) {
                JSONObject data = arrayJson.getJSONObject(i);
                map = new HashMap<String, String>();

                for (int x = 0; x < requestId.size(); x++) {
                    map.put(requestId.get(x), data.getString(requestId.get(x)));
                    Log.e("map", requestId.get(x) + ":" + data.getString(requestId.get(x)));
                }
                list.add(map);
            }
        } catch (JSONException ex) {
            Log.e(DEBUG_TAG, "JSON解析失敗", ex);
        }
        return list;
    }

    public Map<String, String> jsonOne(String result) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject rootJSON = new JSONObject(result);
            JSONArray arrayJson = rootJSON.getJSONArray("date");
            JSONObject data = arrayJson.getJSONObject(0);
            for (int x = 0; x < requestId.size(); x++) {
                map.put(requestId.get(x), data.getString(requestId.get(x)));
            }

        } catch (JSONException ex) {
            Log.e(DEBUG_TAG, "JSON解析失敗", ex);
        }
        return map;
    }
}
