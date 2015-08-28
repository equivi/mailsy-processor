package com.equivi.mailsy.processor.process;

import com.equivi.mailsy.data.dao.CampaignDao;
import com.equivi.mailsy.data.dao.CampaignSubscriberGroupDao;
import com.equivi.mailsy.data.dao.SubscriberContactDao;
import com.equivi.mailsy.data.entity.CampaignEntity;
import com.equivi.mailsy.data.entity.CampaignSubscriberGroupEntity;
import com.equivi.mailsy.data.entity.SubscriberContactEntity;
import com.equivi.mailsy.data.entity.SubscriberGroupEntity;
import com.equivi.mailsy.processor.process.dto.CampaignEmailObject;
import com.equivi.mailsy.processor.process.dto.CampaignSubscriberList;
import com.google.common.collect.Lists;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(EmailChannelProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        Long campaignId = (Long) exchange.getIn().getBody();

        LOG.info("Process Campaign ID:" +campaignId);

        List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntities = getSubscriberGroupList(campaignId);

        CampaignSubscriberList campaignSubscriberList = new CampaignSubscriberList();
        campaignSubscriberList.setCampaignEmailObjectList(buildCampaignEmailObject(campaignSubscriberGroupEntities));
        exchange.getOut().setBody(campaignSubscriberList);
    }

    private List<CampaignSubscriberGroupEntity> getSubscriberGroupList(Long campaignId) {
        CampaignEntity campaignEntity = campaignDao.findOne(campaignId);
        List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntityList = Lists.newArrayList(campaignSubscriberGroupDao.findByCampaignEntity(campaignEntity));

        return campaignSubscriberGroupEntityList;
    }

    private List<CampaignEmailObject> buildCampaignEmailObject(List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntities) {

        List<CampaignEmailObject> campaignEmailObjects = new ArrayList<>();
        for (CampaignSubscriberGroupEntity campaignSubscriberGroupEntity : campaignSubscriberGroupEntities) {
            CampaignEmailObject campaignEmailObject = new CampaignEmailObject();
            campaignEmailObject.setCampaignId(campaignSubscriberGroupEntity.getCampaignEntity().getId());
            campaignEmailObject.setCampaignUUID(campaignSubscriberGroupEntity.getCampaignEntity().getCampaignUUID());
            campaignEmailObject.setCampaignName(campaignSubscriberGroupEntity.getCampaignEntity().getCampaignName());
            campaignEmailObject.setEmailSubject(campaignSubscriberGroupEntity.getCampaignEntity().getEmailSubject());
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
}
