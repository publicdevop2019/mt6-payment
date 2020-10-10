package com.hw.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppCreatePaymentLinkCommand implements Serializable {
    private static final long serialVersionUID = 1;
    private String userId;

    private String orderId;

    private String prepayId;

}
