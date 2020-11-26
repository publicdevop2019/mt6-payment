package com.hw.entity;

import com.hw.config.PaymentStatus;
import com.hw.controller.AppCreatePaymentLinkCommand;
import com.hw.shared.Auditable;
import com.hw.shared.rest.Aggregate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
public class BizPayment extends Auditable implements Aggregate, Serializable {
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
    @Version
    @Setter(AccessLevel.NONE)
    private Integer version;
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
