package com.ilek.homework.panel;

import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.page.ResultPage;
import com.opencsv.exceptions.CsvValidationException;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.BootstrapFileInputField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.util.Collections;

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
        final BootstrapFileInputField fileUpload = new BootstrapFileInputField("fileUpload") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                try {
                    ParsedCsvResult result = parseCsvFile(getFileUpload().getInputStream());
                    if(!result.getParseErrors().isEmpty()) {
                        modal.setModelObject(result);
                        target.add(modal);
                        modal.show(target);
                    } else {
                        setResponsePage(new ResultPage(UploadCsvPanel.this.getModel()));
                    }
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                    notificationPanel.error(e.getLocalizedMessage());
                } finally {
                    target.add(notificationPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                target.add(notificationPanel);
            }
        };

        fileUpload.getConfig()
                .maxFileCount(1)
                .allowedFileExtensions(Collections.singletonList("csv"))
                .showPreview(false)
                .initialCaption(getInitialCaption())
                .captionClass();

        form.queue(fileUpload.setRequired(true));
        queue(form);
    }

    protected String getInitialCaption() {
        return getString("selectFile");
    }

}
