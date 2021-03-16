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
}
