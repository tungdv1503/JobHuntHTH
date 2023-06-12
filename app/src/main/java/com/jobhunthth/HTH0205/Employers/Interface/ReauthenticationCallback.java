package com.jobhunthth.HTH0205.Employers.Interface;

public interface ReauthenticationCallback {
    void onReauthenticationSuccess();
    void onReauthenticationFailure(Exception exception);
}
