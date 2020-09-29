package com.hw.clazz;

import com.hw.entity.BizPayment;
import com.hw.shared.sql.RestfulQueryRegistry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class BizPaymentQueryRegistry extends RestfulQueryRegistry<BizPayment> {
    @Override
    @PostConstruct
    protected void configQueryBuilder() {

    }
}
