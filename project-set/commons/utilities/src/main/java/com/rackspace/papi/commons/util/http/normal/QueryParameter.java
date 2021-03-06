package com.rackspace.papi.commons.util.http.normal;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zinic
 */
public class QueryParameter implements Comparable<QueryParameter> {

    private final String name;
    private final List<String> values;

    public QueryParameter(String name) {
        this.name = name;
        this.values = new LinkedList<String>();
    }

    public String getName() {
        return name;
    }

    public void addValue(String value) {
        values.add(value);
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public int compareTo(QueryParameter o) {
        return getName().compareTo(o.getName());
    }
}
