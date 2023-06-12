package com.jobhunthth.HTH0205.Employers.Interface;

import com.jobhunthth.HTH0205.Models.CompanyInfoModel;

public interface CompanyInfoCallBack {
    void onSuccess(CompanyInfoModel company);
    void onFailure(Exception e);
}
