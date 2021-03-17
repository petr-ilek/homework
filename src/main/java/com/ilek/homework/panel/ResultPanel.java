package com.ilek.homework.panel;

import com.ilek.homework.dataprovider.SecuritiesDataProvider;
import com.ilek.homework.model.ParsedCsvResult;
import com.ilek.homework.model.SecurityDto;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class ResultPanel extends GenericPanel<ParsedCsvResult> {

    public ResultPanel(String id, IModel<ParsedCsvResult> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final SecuritiesDataProvider dataProvider = new SecuritiesDataProvider(getModelObject().getSecurities());
        final BootstrapDefaultDataTable<SecurityDto, String> dataTable = new BootstrapDefaultDataTable<>("table", createColumns(),
                dataProvider, 100);

        queue(dataTable);
        final FilterForm<SecuritiesDataProvider.IsinFilter> filterForm = new FilterForm<>("filterForm", dataProvider);
        filterForm.add(new TextField<String>("isinField", new Model<>() {
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
        columns.add(new BootstrapTextFilteredPropertyColumn<>(Model.of("ISIN"), "isin", "isin", SecuritiesDataProvider.SortableColumns.PROPERTY_ISIN));
        columns.add(new PropertyColumn<>(Model.of("issuer"), "issuer"));
        columns.add(new PropertyColumn<>(Model.of("applicationType"), "applicationType"));
        columns.add(new PropertyColumn<>(Model.of("instrumentType"), "instrumentType"));
        columns.add(new PropertyColumn<>(Model.of("listingDate"), SecuritiesDataProvider.SortableColumns.PROPERTY_LISTING_DATE, "listingDateFormatted"));
        return columns;
    }

}
