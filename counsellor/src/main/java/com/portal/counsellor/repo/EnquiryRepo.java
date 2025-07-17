package com.portal.counsellor.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.entity.Enquiry;


@Repository
public interface EnquiryRepo extends JpaRepository<Enquiry, Integer>
{

    List<Enquiry> findAllByCounsellor(Counsellor counsellor);

    @Query(value = "select * from enquiry where c_id = :counsellorId", nativeQuery = true)
    List<Enquiry> getAllEnquiresByCounsellorId(Integer counsellorId);

    @Query("SELECT e FROM Enquiry e JOIN FETCH e.counsellor")
    List<Enquiry> findAllWithCounsellor();
 


}
