package com.hw.controller;

import com.hw.entity.BizPayment;
import com.hw.shared.rest.RoleBasedRestfulService;
import com.hw.shared.rest.VoidTypedClass;
import com.hw.shared.sql.RestfulQueryRegistry;
import org.springframework.stereotype.Service;

@Service
public class AppPaymentLinkApplicationService extends RoleBasedRestfulService<BizPayment, Void, Void, VoidTypedClass> {
    {
        entityClass = BizPayment.class;
        role = RestfulQueryRegistry.RoleEnum.APP;
    }

    @Override
    protected BizPayment createEntity(long id, Object command) {
        return BizPayment.create(id, (AppCreatePaymentLinkCommand) command);
    }

}
