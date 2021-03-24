package io.github.h4j4x.codegen.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataInput {
    private List<TemplateObject> templates;
    private Object data;
    private CsvData csvData;

    public List<TemplateObject> getTemplates() {
        if (templates == null) {
            templates = new LinkedList<>();
        }
        return templates;
    }

    public void setTemplates(List<TemplateObject> templates) {
        this.templates = templates;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CsvData getCsvData() {
        return csvData;
    }

    public void setCsvData(CsvData csvData) {
        this.csvData = csvData;
    }

    @JsonIgnore
    public Object getCsvDataObject(File dataFolder) throws IOException {
        if (csvData != null) {
            return csvData.getObject(dataFolder);
        }
        return null;
    }
}
