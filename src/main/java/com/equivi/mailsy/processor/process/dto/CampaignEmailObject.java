package com.equivi.mailsy.processor.process.dto;

import com.equivi.mailsy.data.entity.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CampaignEmailObject {

    private String campaignName;

    private String emailFrom;

    private String emailSubject;

    private String emailContent;

    private Date scheduledSendDate;

    private List<String> emailList;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public Date getScheduledSendDate() {
        return scheduledSendDate;
    }

    public void setScheduledSendDate(Date scheduledSendDate) {
        this.scheduledSendDate = scheduledSendDate;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }
}
