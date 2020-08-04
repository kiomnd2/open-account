package com.kakao.openaccount.transfer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TransferController {

    @PostMapping(value = "/transfer-certify")
    public void certificationByTransfer(@RequestBody TransferDto transferDto) {
        
    }
}
