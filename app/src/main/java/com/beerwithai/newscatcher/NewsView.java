package com.beerwithai.newscatcher;

import android.app.Activity;
import android.webkit.WebView;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beerwithai.newscatcher.R;

public class NewsView extends Activity
{
    private WebView mWebView = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_view);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        mWebView.loadUrl(url);
    }
}