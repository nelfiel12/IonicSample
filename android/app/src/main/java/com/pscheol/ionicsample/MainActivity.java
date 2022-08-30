package com.pscheol.ionicsample;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.webkit.WebViewCompat;

import com.getcapacitor.BridgeActivity;
import com.pscheol.ionicsample.plugins.PhotoGalleryPlugin;
import com.pscheol.ionicsample.plugins.SocialLoginPlugin;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerPlugin(PhotoGalleryPlugin.class);
        registerPlugin(SocialLoginPlugin.class);
        super.onCreate(savedInstanceState);

        PackageInfo info = WebViewCompat.getCurrentWebViewPackage(getApplicationContext());

        Toast.makeText(getApplicationContext(), "WebView version : " + info.versionName, Toast.LENGTH_LONG).show();
    }
}
