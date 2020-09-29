package com.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hw.clazz.BizPaymentQueryRegistry;
import com.hw.entity.BizPayment;
import com.hw.repo.PaymentRepo;
import com.hw.shared.IdGenerator;
import com.hw.shared.idempotent.AppChangeRecordApplicationService;
import com.hw.shared.rest.DefaultRoleBasedRestfulService;
import com.hw.shared.rest.VoidTypedClass;
import com.hw.shared.sql.RestfulQueryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
@Service
public class AppPaymentLinkApplicationService extends DefaultRoleBasedRestfulService<BizPayment,Void,Void, VoidTypedClass> {
    @Autowired
    private PaymentRepo repo2;
    @Autowired
    private AppChangeRecordApplicationService changeHistoryRepository;

    @Autowired
    private IdGenerator idGenerator2;

    @Autowired
    private BizPaymentQueryRegistry productQueryRegistry;

    @Autowired
    private ObjectMapper om2;

    @PostConstruct
    private void setUp() {
        repo = repo2;
        idGenerator = idGenerator2;
        queryRegistry = productQueryRegistry;
        entityClass = BizPayment.class;
        role = RestfulQueryRegistry.RoleEnum.APP;
        om = om2;
        appChangeRecordApplicationService = changeHistoryRepository;
    }
    @Override
    public BizPayment replaceEntity(BizPayment bizPayment, Object command) {
        return null;
    }

    @Override
    public Void getEntitySumRepresentation(BizPayment bizPayment) {
        return null;
    }

    @Override
    public Void getEntityRepresentation(BizPayment bizPayment) {
        return null;
    }

    @Override
    protected BizPayment createEntity(long id, Object command) {
        return BizPayment.create(id,(AppCreatePaymentLinkCommand)command);
    }

    @Override
    public void preDelete(BizPayment bizPayment) {

    }

    @Override
    public void postDelete(BizPayment bizPayment) {

    }

    @Override
    protected void prePatch(BizPayment bizPayment, Map<String, Object> params, VoidTypedClass middleLayer) {

    }

    @Override
    protected void postPatch(BizPayment bizPayment, Map<String, Object> params, VoidTypedClass middleLayer) {

    }
}
