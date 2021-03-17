package com.ilek.homework.model;

import com.ilek.homework.enums.ApplicationTypes;
import com.ilek.homework.enums.InstrumentTypes;

import java.io.Serializable;
import java.util.Date;

public class SecurityDto implements Serializable {

    private String isin;
    private String issuer;
    private ApplicationTypes applicationType;
    private InstrumentTypes instrumentType;
    private Date listingDate;

    public SecurityDto(String isin, String issuer, ApplicationTypes applicationType, InstrumentTypes instrumentType, Date listingDate) {
        this.isin = isin;
        this.issuer = issuer;
        this.applicationType = applicationType;
        this.instrumentType = instrumentType;
        this.listingDate = listingDate;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public ApplicationTypes getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationTypes applicationType) {
        this.applicationType = applicationType;
    }

    public InstrumentTypes getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentTypes instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Date getListingDate() {
        return listingDate;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public Date getListingDateFormatted() {
        //todo add formating date to display full year
        return listingDate;
    }
}
