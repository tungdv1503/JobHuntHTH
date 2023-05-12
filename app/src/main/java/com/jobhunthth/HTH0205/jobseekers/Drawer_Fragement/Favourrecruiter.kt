package com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jobhunthth.HTH0205.R


class Favourrecruiter : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val v = inflater.inflate(R.layout.fragment_favour_recruiter, container, false);
        return v;
    }


}