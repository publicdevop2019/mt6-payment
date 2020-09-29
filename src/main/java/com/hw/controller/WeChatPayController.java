package com.hw.controller;

import com.hw.clazz.PaymentStatus;
import com.hw.entity.BizPayment;
import com.hw.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.hw.shared.AppConstant.HTTP_HEADER_CHANGE_ID;

@RestController
@RequestMapping(produces = "application/json")
public class WeChatPayController {

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AppPaymentLinkApplicationService appPaymentLinkApplicationService;

    @PostMapping("paymentLink")
    public ResponseEntity<?> createWeChatPaymentLink(@RequestHeader("authorization") String authorization, @RequestBody Map<String, String> body, @RequestHeader(HTTP_HEADER_CHANGE_ID) String changeId) {
        String orderId = body.get("orderId");
        String randomStr = UUID.randomUUID().toString().replace("-", "");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("paymentLink",
                "weixin：//wxpay/bizpayurl?appid=wx2421b1c4370ec43b&mch_id=10000100&nonce_str="
                        + randomStr + "&product_id=" + orderId + "&time_stamp=1415949957&sign=512F68131DD251DA4A45DA79CC7EFE9D");
        /**
         * @todo remove after real integration
         */
        afterQRScanCallback("dummyOpenId", orderId,changeId);
        return ResponseEntity.ok(stringStringHashMap);
    }

    @PostMapping("afterQRScanCallback")
    public ResponseEntity<?> afterQRScanCallback(@RequestParam("openid") String openId, @RequestParam("productid") String productId, @RequestHeader(HTTP_HEADER_CHANGE_ID) String changeId) {
        AppCreatePaymentLinkCommand appCreatePaymentLinkCommand = new AppCreatePaymentLinkCommand();
        appCreatePaymentLinkCommand.setOrderId(productId);
        appCreatePaymentLinkCommand.setUserId(openId);
        appPaymentLinkApplicationService.create(appCreatePaymentLinkCommand,changeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("paySuccessCallback")
    public ResponseEntity<?> paySuccessCallback(@RequestParam("productid") String productId) {
        Optional<BizPayment> paymentByOrderId = paymentRepo.getPaymentByOrderId(productId);
        if (paymentByOrderId.isEmpty())
            return ResponseEntity.badRequest().build();
        paymentByOrderId.get().setStatus(PaymentStatus.paid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("paymentStatus/{orderId}")
    public ResponseEntity<?> checkPaymentStatus(@PathVariable("orderId") String orderId) {
        Optional<BizPayment> paymentByOrderId = paymentRepo.getPaymentByOrderId(orderId);
        if (paymentByOrderId.isEmpty())
            return ResponseEntity.badRequest().build();
        Map<String, Boolean> msg = new HashMap<>();
        if (paymentByOrderId.get().getStatus().equals(PaymentStatus.paid)) {
            msg.put("paymentStatus", Boolean.TRUE);
        } else {
            /** check with WeChat api */
            Boolean aBoolean = checkWeChatPaymentStatus(paymentByOrderId.get().getPrepayId());
            if (aBoolean) {
                paymentByOrderId.get().setStatus(PaymentStatus.paid);
                msg.put("paymentStatus", Boolean.TRUE);
            } else {
                paymentByOrderId.get().setStatus(PaymentStatus.unpaid);
                msg.put("paymentStatus", Boolean.FALSE);
            }
            return ResponseEntity.ok(msg);
        }
        return ResponseEntity.ok().build();
    }




    private Boolean checkWeChatPaymentStatus(String prepayId) {
        return Boolean.TRUE;
    }
}
