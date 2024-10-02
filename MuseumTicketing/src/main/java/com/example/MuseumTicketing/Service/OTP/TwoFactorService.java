package com.example.MuseumTicketing.Service.OTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TwoFactorService {

    private final RestTemplate restTemplate;

    @Value("${2factor.api.key}")
    private String apiKey; // Add your 2Factor API key in application.properties or application.yml

    public TwoFactorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateOtp(String mobileNumber) {
        String apiUrl = "https://2factor.in/API/V1/{apiKey}/SMS/{mobileNumber}/AUTOGEN";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("apiKey", apiKey);
        uriVariables.put("mobileNumber", mobileNumber);

//        Details existingDetails = detailsRepo.findByMobileNumber(mobileNumber);
//
//        if (existingDetails == null) {
//            Details newdetails = new Details();
//            newdetails.setMobileNumber(mobileNumber);
//            detailsRepo.save(newdetails);
//        }
        // Make a GET request to generate OTP
        return restTemplate.getForObject(apiUrl, String.class, uriVariables);
    }

    public String resendOtp(String mobileNumber) {
        String apiUrl = "https://2factor.in/API/V1/{apiKey}/SMS/{mobileNumber}/AUTOGEN";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("apiKey", apiKey);
        uriVariables.put("mobileNumber", mobileNumber);

        // Make a GET request to resend OTP
        return restTemplate.getForObject(apiUrl, String.class, uriVariables);
    }

    public String validateOtp(String sessionId, String enteredOtp, String mobileNumber) {
        String apiUrl = "https://2factor.in/API/V1/{apiKey}/SMS/VERIFY/{sessionId}/{enteredOtp}";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("apiKey", apiKey);
        uriVariables.put("sessionId", sessionId);
        uriVariables.put("enteredOtp", enteredOtp);


//        Details exsistdetails = detailsRepo.findByMobileNumber(mobileNumber);
//
//        if(exsistdetails != null) {
//
//            exsistdetails.setSessionId(sessionId);
//            detailsRepo.save(exsistdetails);
//        }


        // Make a GET request to validate OTP
        return restTemplate.getForObject(apiUrl, String.class, uriVariables);
    }
}
