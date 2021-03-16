package com.ilek.homework.page;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.ilek.homework.panel.UploadCsvPanel;
import org.apache.wicket.markup.html.WebPage;

@WicketHomePage
public class HomePage extends WebPage {

    public HomePage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new UploadCsvPanel("uploadPanel"));
    }
}