package com.ilek.homework.panel;

import com.ilek.homework.enums.ApplicationTypes;
import com.ilek.homework.enums.InstrumentTypes;
import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.model.SecurityDto;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidator;

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

public class UploadCsvPanel extends Panel {

    public UploadCsvPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        queue(feedbackPanel.setOutputMarkupId(true));
        final Form<FileUpload> form = new Form<>("form");
        final FileUploadField fileUpload = new FileUploadField("fileUpload");
        fileUpload.add((IValidator<List<FileUpload>>) iValidatable -> {
            if (!"text/csv".equals(iValidatable.getValue().get(0).getContentType())) {
                fileUpload.error(getString("invalidFileFormat"));
            }
        });
        form.queue(fileUpload.setRequired(true));
        form.queue(new AjaxSubmitLink("submit", form) {
            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                target.add(feedbackPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                //todo fixed csv separator?, control file size?
                try {
                    ParsedCsvResult result = parseCsvFile(fileUpload.getFileUpload().getInputStream());
                    if(!result.getParseErrors().isEmpty()) {
                        for(String error : result.getParseErrors()) {
                            feedbackPanel.error(error);
                        }
                    }
                    //todo show panel with CSV table
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                    feedbackPanel.error(e.getLocalizedMessage());
                } finally {
                    target.add(feedbackPanel);
                }
            }
        });
        queue(form);
    }

    public ParsedCsvResult parseCsvFile(InputStream inputStream) throws IOException, CsvValidationException {
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
                    e.printStackTrace();
                    errors.add("Error on line: " + line + ", cause: " + e.getLocalizedMessage());
                } finally {
                    line++;
                }
            }
        }
        return new ParsedCsvResult(securities, errors);
    }

    public String parseIsin(String isin) throws IllegalArgumentException {
        Pattern isinPattern = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = isinPattern.matcher(isin);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid ISIN");
        } else {
            return isin;
        }
    }

    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.parse(date);
    }
}
