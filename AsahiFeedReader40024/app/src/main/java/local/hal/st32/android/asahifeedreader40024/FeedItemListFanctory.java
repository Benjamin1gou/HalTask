package local.hal.st32.android.asahifeedreader40024;

/**
 * Created by Tester on 2016/09/30.
 */

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class FeedItemListFanctory {
    /**
     * ログ人記載するタグ
     */
    private static final String DEBUG_TAG = "ShowLinkPageActivity";

    /**
     * RSS1.0形式のXML文字列を書く記事が格納されたListオブジェクトに変換するメソッド
     * @param xml RSS2.0形式のXML文字列
     * @return 各記事が格納されたListオブジェクト。各記事はMap型であり、キーはtitle(タイトル)、link(リンク先URL),
     *         description(記事の内容),pubDate(記事の投稿日時),pubDateStr(記事の投稿日時を整形した文字列)のいずれかである。
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<Map<String, String>> createFeedItemList(String xml) throws XmlPullParserException,IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(xml));
        parser.nextTag();
        List<Map<String,String>> items = parseFeed(parser);
        return items;
    }
    /**
     * RSS2.0のXmlPullParserオブジェクト全体をパースしてitem要素を取り出し、Listオブジェクトに変換するメソッド
     * @param parser パース対象のRSS2.0のxmlPullparserオブジェクト
     * @return 変換されたListオブジェクト
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static List<Map<String, String >> parseFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Map<String, String >> items = new ArrayList<Map<String, String>>();

        parser.require(XmlPullParser.START_TAG, null, "rdf:RDF");

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("item")){
                Map<String ,String > item = parseItem(parser);
                items.add(item);
            }else if(!name.equals("rdf:RDF")){
                skip(parser);
            }
        }
        return items;
    }

    /**
     * item要素が格納されたXmlPullParserオブジェクトをパースしてこ要素を取り出し、Mapオブジェクトに変換するメソッド
     *
     * @param parser itemようそのXmlPillParserオブジェクト
     * @return 変換されたMapオブジェクト
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static Map<String, String> parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        Map<String, String> item = new HashMap<String, String>();
        parser.require(XmlPullParser.START_TAG, null, "item");

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();
            if(name.equals("title")){
                String title = readText(parser, "title");
                item.put("title", title);
            }else if(name.equals("link")){
                String link = readText(parser, "link");
                item.put("link", link);
            }else if(name.equals("dc:date")){
                String pubDate = readText(parser, "dc:date");
                item.put("dc:date", pubDate);

                String pubDateStr = "";
                try {
                    //SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                    Date pubTimestamp = formatter.parse(pubDate);
                    formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN);
                    pubDateStr = formatter.format(pubTimestamp);
                }catch (ParseException ex){
                    Log.e(DEBUG_TAG, pubDate+"の日付変換",ex);
                }
                item.put("pubDateStr", pubDateStr);
            }else {
                skip(parser);
            }
        }
        return item;
    }


    /**
     * item要素の子要素が格納されたXmlPullParseオブジェクトをパースしてないようテキストを取り出すメソッド
     *
     * @param parser item要素の子要素が格納されたXmlPullParserオブシェクト
     * @param name 子要素の名前
     * @return 取り出された内容テキスト文字列
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readText(XmlPullParser parser, String name) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, name);
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();

        }
        parser.require(XmlPullParser.END_TAG, null, name);
        return result;
    }

    /**
     * XmlPullParserオブジェクトの現在の現在のイベントが開始タグの場合、配下の要素を含め絵全てのイベントを進めておくメソッド。
     * @param parser スキップ対象のXmlPullParserオブジェクト。現在のイベントが開始タグである必要がある。
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while(depth != 0){
            switch(parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
