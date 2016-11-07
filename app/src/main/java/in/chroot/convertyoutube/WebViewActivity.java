package in.chroot.convertyoutube;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    String link ;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView =(WebView)findViewById(R.id.webView);
        if (getIntent().getStringExtra("link")!=null){

            link = getIntent().getStringExtra("link");

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(link);
        }


    }


    public static void startThisActivity(Context context, String link){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("link",link);
        context.startActivity(intent);
    }
}
