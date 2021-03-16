package com.ilek.homework.model;

import java.io.Serializable;
import java.util.List;

public class ParsedCsvResult implements Serializable {

    private List<SecurityDto> securities;
    private List<String> parseErrors;

    public ParsedCsvResult(List<SecurityDto> securities, List<String> parseErrors) {
        this.securities = securities;
        this.parseErrors = parseErrors;
    }

    public List<SecurityDto> getSecurities() {
        return securities;
    }

    public void setSecurities(List<SecurityDto> securities) {
        this.securities = securities;
    }

    public List<String> getParseErrors() {
        return parseErrors;
    }

    public void setParseErrors(List<String> parseErrors) {
        this.parseErrors = parseErrors;
    }
}
