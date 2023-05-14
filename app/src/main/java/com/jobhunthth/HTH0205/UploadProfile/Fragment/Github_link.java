package com.jobhunthth.HTH0205.UploadProfile.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jobhunthth.HTH0205.R;


public class Github_link extends Fragment {
    WebView gitWB;
    ImageView imgWB;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_github_link, container, false);
        gitWB=v.findViewById(R.id.git);
        imgWB=v.findViewById(R.id.imgWB);
        url=getArguments().getString("githublink");
        WebSettings webViewST=gitWB.getSettings();
        webViewST.setBuiltInZoomControls(true);
        webViewST.setJavaScriptEnabled(true);
        gitWB.setWebViewClient(new GitWB());
        gitWB.loadUrl(url);

        return v;
    }
    private class GitWB extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}