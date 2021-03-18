package com.ilek.homework.panel;

import com.ilek.homework.dataprovider.SecuritiesDataProvider;
import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.model.SecurityDto;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.toolbars.BootstrapNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultPanel extends GenericPanel<ParsedCsvResult> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public ResultPanel(String id, IModel<ParsedCsvResult> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new UploadCsvPanel("uploadPanel") {
            @Override
            protected String getInitialCaption() {
                return getString("newFile");
            }
        });

        final SecuritiesDataProvider dataProvider = new SecuritiesDataProvider(getModelObject().getSecurities());
        final BootstrapDefaultDataTable<SecurityDto, String> dataTable = new BootstrapDefaultDataTable<>("table", createColumns(),
                dataProvider, 100);
        final BootstrapNavigationToolbar bottomNav = new BootstrapNavigationToolbar(dataTable);
        dataTable.setOutputMarkupId(true);
        dataTable.addBottomToolbar(bottomNav);
        queue(dataTable);

        final FilterForm<SecuritiesDataProvider.IsinFilter> filterForm = new FilterForm<>("filterForm", dataProvider);
        filterForm.queue(new TextField<String>("isinField", new Model<>() {
            @Override
            public String getObject() {
                return dataProvider.getFilterState().getIsin();
            }

            @Override
            public void setObject(String object) {
                dataProvider.getFilterState().setIsin(object);
            }
        }));
        queue(filterForm);
    }

    private List<IColumn<SecurityDto, String>> createColumns() {
        final List<IColumn<SecurityDto, String>> columns = new ArrayList<>();
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new StringResourceModel("isin"), "isin", "isin", SecuritiesDataProvider.SortableColumns.PROPERTY_ISIN));
        columns.add(new PropertyColumn<>(new StringResourceModel("issuer"), "issuer"));
        columns.add(new PropertyColumn<>(new StringResourceModel("applicationType"), "applicationType"));
        columns.add(new PropertyColumn<>(new StringResourceModel("instrumentType"), "instrumentType"));
        columns.add(new PropertyColumn<>(new StringResourceModel("listingDate"), SecuritiesDataProvider.SortableColumns.PROPERTY_LISTING_DATE, "listingDate") {
            @Override
            public IModel<?> getDataModel(IModel<SecurityDto> rowModel) {
                if(rowModel.getObject().getListingDate()!=null) {
                    return Model.of(DATE_FORMAT.format(rowModel.getObject().getListingDate()));
                } else {
                    return Model.of("");
                }
            }
        });
        return columns;
    }

}
