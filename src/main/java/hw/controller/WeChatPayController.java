package hw.controller;

import hw.clazz.PaymentStatus;
import hw.entity.Payment;
import hw.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "v1/api", produces = "application/json")
public class WeChatPayController {

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("paymentLink")
    public ResponseEntity<?> getWeChatPaymentLink(@RequestHeader("authorization") String authorization, @RequestBody Map<String, String> body) {
        String orderId = body.get("orderId");
        String randomStr = UUID.randomUUID().toString().replace("-", "");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("paymentLink",
                "weixin：//wxpay/bizpayurl?appid=wx2421b1c4370ec43b&mch_id=10000100&nonce_str="
                        + randomStr + "&product_id=" + orderId + "&time_stamp=1415949957&sign=512F68131DD251DA4A45DA79CC7EFE9D");
        /**
         * @todo remove after real integration
         */
        afterQRScanCallback("dummyOpenId", orderId);
        return ResponseEntity.ok(stringStringHashMap);
    }

    @PostMapping("afterQRScanCallback")
    public ResponseEntity<?> afterQRScanCallback(@RequestParam("openid") String openId, @RequestParam("productid") String productId) {

        Payment payment = new Payment();
        payment.setOrderId(productId);
        payment.setUserId(openId);
        payment.setStatus(PaymentStatus.unpaid);


        String prepayId = callWeChatPaymentAPI();

        payment.setPrepayId(prepayId);

        paymentRepo.save(payment);

        notifyWeChatToStartUserPay(prepayId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("paySuccessCallback")
    public ResponseEntity<?> paySuccessCallback(@RequestParam("productid") String productId) {
        Optional<Payment> paymentByOrderId = paymentRepo.getPaymentByOrderId(productId);
        if (paymentByOrderId.isEmpty())
            return ResponseEntity.badRequest().build();
        paymentByOrderId.get().setStatus(PaymentStatus.paid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("paymentStatus/{orderId}")
    public ResponseEntity<?> checkPaymentStatus(@PathVariable("orderId") String orderId) {
        Optional<Payment> paymentByOrderId = paymentRepo.getPaymentByOrderId(orderId);
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

    private String callWeChatPaymentAPI() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void notifyWeChatToStartUserPay(String prepayId) {
    }

    private Boolean checkWeChatPaymentStatus(String prepayId) {
        return Boolean.TRUE;
    }
}
