package hw.entity;

import hw.clazz.PaymentStatus;
import hw.shared.Auditable;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Payment")
@Data
public class Payment extends Auditable {
    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String userId;

    @Id
    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String orderId;

    /**@note from wechat*/
    private String prepayId;

    private PaymentStatus status;
}
