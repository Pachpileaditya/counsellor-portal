package com.portal.counsellor.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.counsellor.entity.Counsellor;


@Repository
public interface CounsellorRepo extends JpaRepository<Counsellor, Integer>
{


    Optional<Counsellor> findByEmail(String email);
    
    boolean existsByEmail(String email);
	

}
