package com.hw.config;

import com.hw.entity.BizPayment;
import com.hw.shared.sql.RestfulQueryRegistry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class BizPaymentQueryRegistry extends RestfulQueryRegistry<BizPayment> {
    @Override
    public Class<BizPayment> getEntityClass() {
        return BizPayment.class;
    }

}
