package com.frontline.mschainca.service;


import com.frontline.mschainca.util.Util;
import org.springframework.stereotype.Service;

@Service
public class CaService {


    public String[] decodeCSR(String csr) {
        Util.decodeCSR(csr);
        return null;
    }


}
