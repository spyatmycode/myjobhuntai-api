package com.spyatmycode.myjobhuntai.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;

@RestController
public class HealthController {

    @RequestMapping("/alive")
    public ResponseEntity<ApiResponse<Boolean>> checkServerIsAlive(){
        return ApiResponse.success(HttpStatus.OK, true, "I'm alive", null);
    }
    
}
