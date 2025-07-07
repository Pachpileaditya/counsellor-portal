package com.portal.counsellor.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse 
{

    private Integer totalEnquiries;
    private Integer openEnquiries;
    private Integer enrollEnquiries;
    private Integer lostEnquiries;
    
}
