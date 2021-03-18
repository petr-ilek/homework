package com.ilek.homework.panel;

import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.page.ResultPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.StringResourceModel;

public class CsvWarningsModal extends Modal<ParsedCsvResult> {

    public CsvWarningsModal(String id, IModel<ParsedCsvResult> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        size(Size.Extra_large);
        header(new StringResourceModel("resultsWarningTitle"));
        queue(new BootstrapListView<>("listView", LambdaModel.of(getModel(), ParsedCsvResult::getParseErrors, ParsedCsvResult::setParseErrors)) {
            @Override
            protected void populateItem(ListItem<String> listItem) {
                listItem.queue(new Alert("alert", listItem.getModel()).type(Alert.Type.Warning));
            }
        });
        final BootstrapAjaxLink<Void> yesBtn = new BootstrapAjaxLink<>("yesBtn", Buttons.Type.Light) {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(new ResultPage(CsvWarningsModal.this.getModel()));
            }
        };
        yesBtn.setLabel(new StringResourceModel("yesBtn"));
        queue(yesBtn);

        final BootstrapAjaxLink<Void> noBtn = new BootstrapAjaxLink<>("noBtn", Buttons.Type.Light) {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                close(ajaxRequestTarget);
            }
        };
        noBtn.setLabel(new StringResourceModel("noBtn"));
        queue(noBtn);


    }


}
