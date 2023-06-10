package com.jobhunthth.HTH0205.Employers.Interface;

import com.jobhunthth.HTH0205.Models.UserInfo;

public interface AccountInfoCallBack {
    void onSuccess(UserInfo info);

    void onFailure(Exception e);
}
