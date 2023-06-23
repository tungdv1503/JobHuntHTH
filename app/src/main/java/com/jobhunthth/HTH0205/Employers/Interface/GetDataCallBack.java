package com.jobhunthth.HTH0205.Employers.Interface;

import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.Models.UserProfileModel;

public interface GetDataCallBack {

    void onGetInfoProfileSuccess(UserProfileModel profile, UserInfoModel info);

    void onFail(Exception e);

}
