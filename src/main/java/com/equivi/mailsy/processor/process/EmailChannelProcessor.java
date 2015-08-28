package com.equivi.mailsy.processor.process;

import com.equivi.mailsy.data.dao.CampaignDao;
import com.equivi.mailsy.data.dao.CampaignSubscriberGroupDao;
import com.equivi.mailsy.data.dao.SubscriberContactDao;
import com.equivi.mailsy.data.entity.*;
import com.equivi.mailsy.processor.process.dto.CampaignEmailObject;
import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class EmailChannelProcessor implements Processor {

    @Autowired
    private CampaignSubscriberGroupDao campaignSubscriberGroupDao;

    @Autowired
    private SubscriberContactDao subscriberContactDao;

    @Autowired
    private CampaignDao campaignDao;

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Process :" + exchange.getIn().getMessageId());

        Long campaignId = (Long) exchange.getIn().getBody();

        List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntities = getSubscriberGroupList(campaignId);

        exchange.getOut().setBody(buildCampaignEmailObject(campaignSubscriberGroupEntities));
    }

    private List<CampaignSubscriberGroupEntity> getSubscriberGroupList(Long campaignId) {
        CampaignEntity campaignEntity = campaignDao.findOne(campaignId);
        List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntityList = Lists.newArrayList(campaignSubscriberGroupDao.findAll(getCampaignSubscriberGroupPredicate(campaignEntity)));

        return campaignSubscriberGroupEntityList;
    }

    private List<CampaignEmailObject> buildCampaignEmailObject(List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntities) {

        List<CampaignEmailObject> campaignEmailObjects = new ArrayList<>();
        for (CampaignSubscriberGroupEntity campaignSubscriberGroupEntity : campaignSubscriberGroupEntities) {
            CampaignEmailObject campaignEmailObject = new CampaignEmailObject();
            campaignEmailObject.setCampaignName(campaignSubscriberGroupEntity.getCampaignEntity().getCampaignName());
            campaignEmailObject.setEmailContent(campaignSubscriberGroupEntity.getCampaignEntity().getEmailContent());
            campaignEmailObject.setEmailFrom(campaignSubscriberGroupEntity.getCampaignEntity().getEmailFrom());

            campaignEmailObject.setEmailList(buildEmailList(campaignSubscriberGroupEntity.getSubscriberGroupEntity()));
            campaignEmailObjects.add(campaignEmailObject);
        }

        return campaignEmailObjects;
    }

    private List<String> buildEmailList(SubscriberGroupEntity subscriberGroupEntity) {

        List<SubscriberContactEntity> subscriberContactEntityList = Lists.newArrayList(subscriberContactDao.readBySubscriberGroupEntity(subscriberGroupEntity));

        List<String> subscriberList = new ArrayList<>();
        if(!subscriberContactEntityList.isEmpty()){
            for (SubscriberContactEntity subscriberContactEntity : subscriberContactEntityList) {
                subscriberList.add(subscriberContactEntity.getContactEntity().getEmailAddress());
            }
        }
        return subscriberList;
    }

    private Predicate getCampaignSubscriberGroupPredicate(CampaignEntity campaignEntity) {

        QCampaignSubscriberGroupEntity qCampaignSubscriberGroupEntity = QCampaignSubscriberGroupEntity.campaignSubscriberGroupEntity;
        BooleanBuilder booleanMerchantPredicateBuilder = new BooleanBuilder();

        booleanMerchantPredicateBuilder.or(qCampaignSubscriberGroupEntity.campaignEntity.eq(campaignEntity));

        return booleanMerchantPredicateBuilder;
    }

    private Predicate getSubscriberContactList(SubscriberGroupEntity subscriberGroupEntity) {

        QSubscriberContactEntity qSubscriberContactEntity = QSubscriberContactEntity.subscriberContactEntity;
        BooleanBuilder booleanMerchantPredicateBuilder = new BooleanBuilder();

        booleanMerchantPredicateBuilder.or(qSubscriberContactEntity.subscriberGroupEntity.eq(subscriberGroupEntity));

        return booleanMerchantPredicateBuilder;
    }

}
