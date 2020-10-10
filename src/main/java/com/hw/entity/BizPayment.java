package com.hw.entity;

import com.hw.config.PaymentStatus;
import com.hw.controller.AppCreatePaymentLinkCommand;
import com.hw.shared.Auditable;
import com.hw.shared.rest.IdBasedEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
public class BizPayment extends Auditable implements IdBasedEntity , Serializable {
    private static final long serialVersionUID = 1;
    @Id
    Long id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String userId;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String orderId;

    /**
     * @note from wechat
     */
    private String prepayId;

    private PaymentStatus status;

    public BizPayment(long id, AppCreatePaymentLinkCommand command) {
        this.id = id;
        this.userId = command.getUserId();
        this.orderId = command.getOrderId();
        this.status = PaymentStatus.unpaid;
    }

    public static BizPayment create(long id, AppCreatePaymentLinkCommand command) {
        String prepayId = callWeChatPaymentAPI();
        BizPayment payment = new BizPayment(id, command);
        payment.setPrepayId(prepayId);
        notifyWeChatToStartUserPay(prepayId);
        return payment;
    }

    private static String callWeChatPaymentAPI() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static void notifyWeChatToStartUserPay(String prepayId) {
    }
}
