package com.ilek.homework.panel;

import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.page.ResultPage;
import com.opencsv.exceptions.CsvValidationException;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import java.io.IOException;
import java.util.List;

import static com.ilek.homework.util.CsvParser.parseCsvFile;

public class UploadCsvPanel extends GenericPanel<ParsedCsvResult> {

    public UploadCsvPanel(String id) {
        super(id, Model.of(new ParsedCsvResult()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final CsvWarningsModal modal = new CsvWarningsModal("modal", getModel());
        queue(modal);

        final NotificationPanel notificationPanel = new NotificationPanel("feedbackPanel");
        queue(notificationPanel.setOutputMarkupId(true));
        final BootstrapForm<FileUpload> form = new BootstrapForm<>("form");
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
                target.add(notificationPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                try {
                    ParsedCsvResult result = parseCsvFile(fileUpload.getFileUpload().getInputStream());
                    if(!result.getParseErrors().isEmpty()) {
                        modal.setModelObject(result);
                        target.add(modal);
                        modal.show(target);
                    } else {
                        setResponsePage(new ResultPage(getModel()));
                    }
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                    notificationPanel.error(e.getLocalizedMessage());
                } finally {
                    target.add(notificationPanel);
                }
            }
        });
        queue(form);
    }


}
