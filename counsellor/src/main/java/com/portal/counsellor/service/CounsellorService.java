package com.portal.counsellor.service;

import java.util.Optional;

import com.portal.counsellor.DTO.DashboardResponse;
import com.portal.counsellor.entity.Counsellor;

public interface CounsellorService 
{

    public Optional<Counsellor> findByEmail(String email);

    public boolean registerCounsellor(Counsellor c);

    public Counsellor loginCounsellor(String email, String password);

    public DashboardResponse getDashboardResponse(int counsellorId);


}
