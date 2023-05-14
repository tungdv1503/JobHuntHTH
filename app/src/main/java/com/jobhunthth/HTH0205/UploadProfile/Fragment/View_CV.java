package com.jobhunthth.HTH0205.UploadProfile.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;


public class View_CV extends Fragment {


    ImageView imgCV,cv;
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View c= inflater.inflate(R.layout.fragment_view__c_v, container, false);
        cv=c.findViewById(R.id.cv);
        imgCV=c.findViewById(R.id.imgCV);
        url=getArguments().getString("viewcv");
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                .into(cv);

        return c;
    }
    private class CV extends WebViewClient {
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