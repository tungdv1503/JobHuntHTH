package com.jobhunthth.HTH0205.Employers.Interface;

import com.jobhunthth.HTH0205.Models.UserInfoModel;

public interface AccountInfoCallBack {
    void onSuccess(UserInfoModel info);

    void onFailure(Exception e);
}
