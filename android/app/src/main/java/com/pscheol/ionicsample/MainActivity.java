package com.pscheol.ionicsample;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.pscheol.ionicsample.plugins.PhotoGalleryPlugin;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerPlugin(PhotoGalleryPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
