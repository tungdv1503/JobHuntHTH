package com.jobhunthth.HTH0205.Employers.Interface;

import com.jobhunthth.HTH0205.Models.EmployerModel;

public interface CompanyInfoCallBack {
    void onSuccess(EmployerModel company);
    void onFailure(Exception e);
}
