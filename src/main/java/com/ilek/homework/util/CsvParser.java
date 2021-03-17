package com.ilek.homework.util;

import com.ilek.homework.enums.ApplicationTypes;
import com.ilek.homework.enums.InstrumentTypes;
import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.model.SecurityDto;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParser {

    public static ParsedCsvResult parseCsvFile(InputStream inputStream) throws IOException, CsvValidationException {
        List<String> errors = new ArrayList<>();
        List<SecurityDto> securities = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build())
        {
            String[] lineInArray;
            int line = 2;
            while ((lineInArray = reader.readNext()) != null) {
                try {
                    //todo allow empty values ?
                    SecurityDto securityDto = new SecurityDto(
                            parseIsin(lineInArray[0]),
                            lineInArray[1],
                            ApplicationTypes.valueOf(lineInArray[2].toUpperCase()),
                            InstrumentTypes.valueOf(lineInArray[3].toUpperCase()),
                            parseDate(lineInArray[4]));
                    securities.add(securityDto);
                } catch (IllegalArgumentException | ParseException e) {
                    //todo log ?
                    errors.add("Line: " + line + ", cause: " + e.getLocalizedMessage());
                } finally {
                    line++;
                }
            }
        }
        return new ParsedCsvResult(securities, errors);
    }

    public static String parseIsin(String isin) throws IllegalArgumentException {
        Pattern isinPattern = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = isinPattern.matcher(isin);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid ISIN");
        } else {
            return isin;
        }
    }

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.parse(date);
    }

}
