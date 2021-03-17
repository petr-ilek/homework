package com.ilek.homework.page;

import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.panel.ResultPanel;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;

public class ResultPage extends GenericWebPage<ParsedCsvResult> {

    public ResultPage(IModel<ParsedCsvResult> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new ResultPanel("resultPanel", getModel()));
    }
}
