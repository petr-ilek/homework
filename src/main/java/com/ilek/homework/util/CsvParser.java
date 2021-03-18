package com.ilek.homework.util;

import com.ilek.homework.enums.ApplicationTypes;
import com.ilek.homework.enums.InstrumentTypes;
import com.ilek.homework.exception.InvalidEnumValueException;
import com.ilek.homework.exception.InvalidIsinException;
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
import java.util.Arrays;
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
                    SecurityDto securityDto = new SecurityDto(
                            parseIsin(lineInArray[0]),
                            lineInArray[1],
                            parseEnumValues(ApplicationTypes.class, lineInArray[2].toUpperCase()),
                            parseEnumValues(InstrumentTypes.class, lineInArray[3].toUpperCase()),
                            parseDate(lineInArray[4]));
                    securities.add(securityDto);
                } catch (InvalidIsinException | InvalidEnumValueException | ParseException e) {
                    errors.add("Line: " + line + ", cause: " + e.getMessage());
                } finally {
                    line++;
                }
            }
        }
        return new ParsedCsvResult(securities, errors);
    }

    public static String parseIsin(String isin) throws InvalidIsinException {
        Pattern isinPattern = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = isinPattern.matcher(isin);
        if(!matcher.matches()) {
            throw new InvalidIsinException("Invalid ISIN");
        } else {
            return isin;
        }
    }

    public static <E extends Enum<E>> E parseEnumValues(Class<E> enumType, String value) throws InvalidEnumValueException {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("Invalid value in column with " + enumType.getSimpleName() + ". Possible values are: " + Arrays.toString(enumType.getEnumConstants()), e);
        }
    }

    public static Date parseDate(String date) throws ParseException {
        if(date==null || "".equals(date)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setLenient(false);
        return formatter.parse(date);
    }

}
