package ws.deezertk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected WebView web;
    protected EditText edToken;
    private static final String urlDeezer = "https://www.deezer.com/mx/login";
    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
    private static final String COOKIE_TOKEN = "arl";
    protected String Cookie,Token;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.web);
        edToken = findViewById(R.id.edTextoToken);
        prefs = getSharedPreferences("save", Context.MODE_PRIVATE);
        Token = prefs.getString("token", "");

        edToken.setText(Token);

        web.getSettings().setUserAgentString(USERAGENT);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportMultipleWindows(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        web.getSettings().setDomStorageEnabled(true);
        web.setWebViewClient(new token());
        web.loadUrl(urlDeezer);
    }

    public void btCopiar(View view) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", edToken.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toastCopy), Toast.LENGTH_SHORT).show();
    }


    private class token extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Cookie = CookieManager.getInstance().getCookie(url);
            CookieManager mCookieManager = CookieManager.getInstance();
            Cookie = mCookieManager.getCookie(url);
            if (Cookie.contains(COOKIE_TOKEN)) {
                int bus = Cookie.indexOf(COOKIE_TOKEN+"=");
                if (bus != -1) {
                    Token = Cookie.substring(bus, Cookie.indexOf(59, bus)).replace(COOKIE_TOKEN+"=", "");
                    edToken.setText(Token);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("token", Token);
                    editor.apply();
                }
            }
            return false;
        }

    }
}
