package com.equivi.mailsy.processor.process;

import com.equivi.mailsy.data.dao.CampaignDao;
import com.equivi.mailsy.data.dao.CampaignSubscriberGroupDao;
import com.equivi.mailsy.data.entity.CampaignEntity;
import com.equivi.mailsy.data.entity.CampaignSubscriberGroupEntity;
import com.equivi.mailsy.data.entity.ContactEntity;
import com.equivi.mailsy.data.entity.SubscriberContactEntity;
import com.equivi.mailsy.data.entity.SubscriberGroupEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EmailChannelProcessorTest {

    @Mock
    private CampaignSubscriberGroupDao campaignSubscriberGroupDao;

    @Mock
    private CampaignDao campaignDao;

    @InjectMocks
    private EmailChannelProcessor emailChannelProcessor;


    @Test
    public void testProcess() throws Exception {
        when(campaignDao.findOne(anyLong())).thenReturn(buildCampaignEntity());
    }

    private CampaignEntity buildCampaignEntity() {
        CampaignEntity campaignEntity = new CampaignEntity();
        campaignEntity.setId(1L);
        campaignEntity.setCampaignName("Test Campaign");
        campaignEntity.setEmailContent("Test Email Content");
        campaignEntity.setEmailSubject("Test Email Subject");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-YYYY hh:mm:ss");
        try {
            Date scheduledDate = simpleDateFormat.parse("11-12-2015 12:30:00");
            campaignEntity.setScheduledSendDate(scheduledDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SubscriberGroupEntity subscriberGroupEntity = new SubscriberGroupEntity();
        subscriberGroupEntity.setGroupName("Automobile marketing");
        subscriberGroupEntity.setSubscribeContactEntityList(getSubscriberContactEntities(subscriberGroupEntity));

        List<CampaignSubscriberGroupEntity> campaignSubscriberGroupEntityList = new ArrayList<>();
        CampaignSubscriberGroupEntity campaignSubscriberGroupEntity = new CampaignSubscriberGroupEntity();
        campaignSubscriberGroupEntity.setCampaignEntity(campaignEntity);
        campaignSubscriberGroupEntity.setSubscriberGroupEntity(subscriberGroupEntity);


        campaignEntity.setCampaignSubscriberGroupEntities(campaignSubscriberGroupEntityList);

        return campaignEntity;
    }

    private List<SubscriberContactEntity> getSubscriberContactEntities(SubscriberGroupEntity subscriberGroupEntity) {
        List<SubscriberContactEntity> subscriberContactEntityList = new ArrayList<>();

        SubscriberContactEntity subscriberContactEntity = buildSubscriberContactEntity(subscriberGroupEntity, "admin_email_marketing@email.com");
        SubscriberContactEntity subscriberContactEntity2 = buildSubscriberContactEntity(subscriberGroupEntity, "admin2_email_marketing@email.com");


        subscriberContactEntityList.add(subscriberContactEntity);
        subscriberContactEntityList.add(subscriberContactEntity2);

        return subscriberContactEntityList;
    }

    private SubscriberContactEntity buildSubscriberContactEntity(SubscriberGroupEntity subscriberGroupEntity, String emailAddress) {
        SubscriberContactEntity subscriberContactEntity = new SubscriberContactEntity();
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setEmailAddress(emailAddress);

        subscriberContactEntity.setContactEntity(contactEntity);
        subscriberContactEntity.setSubscriberGroupEntity(subscriberGroupEntity);
        return subscriberContactEntity;
    }
}