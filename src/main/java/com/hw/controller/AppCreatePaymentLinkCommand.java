package com.hw.controller;

import lombok.Data;

@Data
public class AppCreatePaymentLinkCommand {
    private String userId;

    private String orderId;

    private String prepayId;

}
