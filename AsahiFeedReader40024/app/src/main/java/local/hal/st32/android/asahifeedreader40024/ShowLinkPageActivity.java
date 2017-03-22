package local.hal.st32.android.asahifeedreader40024;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowLinkPageActivity extends Activity{

    private WebView _web;

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_link);

        _web = (WebView)findViewById(R.id.wvBrowser);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");

        _web.stopLoading();
        _web.setWebViewClient(new WebViewClient());
        WebSettings settings = _web.getSettings();
        settings.setJavaScriptEnabled(true);
        _web.loadUrl(link);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _web.stopLoading();
        ViewGroup parent = (ViewGroup) _web.getParent();

        if(parent != null){
            parent.removeView(_web);
        }
        _web.destroy();
    }
}
