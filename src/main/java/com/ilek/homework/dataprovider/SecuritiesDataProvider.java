package com.ilek.homework.dataprovider;

import com.ilek.homework.model.SecurityDto;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SecuritiesDataProvider extends SortableDataProvider<SecurityDto, String> implements IFilterStateLocator<SecuritiesDataProvider.IsinFilter> {

    private final List<SecurityDto> data;
    private List<SecurityDto> filteredData;
    private IsinFilter filterState = new IsinFilter();

    public SecuritiesDataProvider(List<SecurityDto> data) {
        this.data = data;
        this.filteredData = data;
    }

    @Override
    public Iterator<? extends SecurityDto> iterator(long first, long count) {
        int from = (int) first;
        int to = (int) (first + count);
        if (to > filteredData.size()) {
            to = filteredData.size();
        }

        SortParam<String> sortParam = getSort();
        if (sortParam != null) {
            filteredData.sort(getComparator(sortParam));
        }
        return filteredData.subList(from, to).iterator();

    }

    private Comparator<SecurityDto> getComparator(SortParam<String> sortParam) {
        Comparator<SecurityDto> comparator;
        if (SortableColumns.PROPERTY_ISIN.equals(sortParam.getProperty())) {
            comparator = Comparator.nullsFirst(Comparator.comparing(SecurityDto::getIsin));
        } else if (SortableColumns.PROPERTY_LISTING_DATE.equals(sortParam.getProperty())) {
            comparator = Comparator.comparing(SecurityDto::getListingDate, Comparator.nullsLast(Comparator.naturalOrder()));
        } else {
            throw new IllegalStateException("The sort param " + sortParam.getProperty() + " is not yet supported");
        }
        if (!sortParam.isAscending()) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public long size() {
        doFilter(); // do filtering in size method because this is called before the iterator method.
        return filteredData == null ? 0 : filteredData.size();
    }

    private void doFilter() {
        filteredData = data;
        if(getFilterState().getIsin()!=null) {
            filteredData = filteredData.stream()
                    .filter(i -> i.getIsin().toUpperCase().startsWith(getFilterState().getIsin().toUpperCase()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public IModel<SecurityDto> model(SecurityDto securityDto) {
        return Model.of(securityDto);
    }

    public static class SortableColumns {
        public static final String PROPERTY_ISIN = "isin";
        public static final String PROPERTY_LISTING_DATE = "listingDate";
    }

    public static class IsinFilter implements Serializable {
        private String isin;

        public String getIsin() {
            return isin;
        }

        public void setIsin(String isin) {
            this.isin = isin;
        }
    }

    @Override
    public IsinFilter getFilterState() {
        return filterState;
    }

    @Override
    public void setFilterState(IsinFilter state) {
        filterState = state;
    }
}
