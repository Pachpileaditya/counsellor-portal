package com.portal.counsellor.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewEnquiryWithFlterRequest {

    private String classMode;
    private String course;
    private String status;

}
