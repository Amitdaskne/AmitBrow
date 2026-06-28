package com.amitdaskne.amitbrow;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
public class BrowserActivity extends AppCompatActivity {
    private WebView webView;
    private EditText urlBar;
    private LinearLayout tabContainer;
    private ArrayList<String> tabs = new ArrayList<>();
    private int currentTab = 0;
    private String homeUrl = "https://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        initializeViews();
        setupWebView();
        loadInitialUrl();
    }
    private void initializeViews() {
        webView = findViewById(R.id.webView);
        urlBar = findViewById(R.id.urlBar);
        tabContainer = findViewById(R.id.tabContainer);
        urlBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrl(v);
                return true;
            }
            return false;
        });
    }
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                urlBar.setText(url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title != null && !title.isEmpty()) {
                    updateTabTitle(currentTab, title);
                }
            }
        });
    }
    private void loadInitialUrl() {
        String url = getIntent().getStringExtra("url");
        if (url == null || url.isEmpty()) url = homeUrl;
        addTab(url);
        webView.loadUrl(url);
        urlBar.setText(url);
    }
    private void addTab(String url) {
        String tabName = "Tab " + (tabs.size() + 1);
        tabs.add(tabName);
        addTabView(tabName);
    }
    private void addTabView(String title) {
        CardView tabCard = new CardView(this);
        tabCard.setRadius(12);
        tabCard.setCardElevation(4);
        tabCard.setBackgroundColor(0x80FFFFFF);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 4, 4, 4);
        tabCard.setLayoutParams(params);
        tabCard.setPadding(16, 8, 16, 8);
        TextView tabText = new TextView(this);
        tabText.setText(title);
        tabText.setTextSize(12);
        tabText.setTextColor(0xFF333333);
        tabCard.addView(tabText);
        tabCard.setOnClickListener(v -> switchTab(tabContainer.indexOfChild(tabCard)));
        tabContainer.addView(tabCard);
        updateTabStyles();
    }
    private void switchTab(int position) {
        currentTab = position;
        updateTabStyles();
    }
    private void updateTabStyles() {
        for (int i = 0; i < tabContainer.getChildCount(); i++) {
            View child = tabContainer.getChildAt(i);
            if (i == currentTab) {
                child.setBackgroundColor(0xCCFFFFFF);
                child.setElevation(8);
            } else {
                child.setBackgroundColor(0x60FFFFFF);
                child.setElevation(2);
            }
        }
    }
    private void updateTabTitle(int position, String title) {
        if (position < tabContainer.getChildCount()) {
            View child = tabContainer.getChildAt(position);
            if (child instanceof CardView) {
                CardView card = (CardView) child;
                TextView text = (TextView) card.getChildAt(0);
                if (text != null) {
                    String shortTitle = title.length() > 15 ? title.substring(0, 15) + "..." : title;
                    text.setText(shortTitle);
                }
            }
        }
    }
    public void loadUrl(View view) {
        String url = urlBar.getText().toString().trim();
        if (TextUtils.isEmpty(url)) return;
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "https://" + url;
        webView.loadUrl(url);
        urlBar.setText(url);
    }
    public void goBack(View view) {
        if (webView.canGoBack()) webView.goBack();
    }
    public void goForward(View view) {
        if (webView.canGoForward()) webView.goForward();
    }
    public void refresh(View view) {
        webView.reload();
    }
    public void goHome(View view) {
        webView.loadUrl(homeUrl);
        urlBar.setText(homeUrl);
    }
    public void showBookmarks(View view) {}
    public void sharePage(View view) {}
    public void newTab(View view) {
        addTab(homeUrl);
        switchTab(tabs.size() - 1);
        webView.loadUrl(homeUrl);
        urlBar.setText(homeUrl);
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
