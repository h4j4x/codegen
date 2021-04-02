package io.github.h4j4x.codegen.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Data input format for CodeGen.
 */
public class DataInput {
    private List<TemplateObject> templates;
    private Object data;
    private CsvData csvData;

    /**
     * Get the templates objects list.
     * @return the templates objects list.
     */
    public List<TemplateObject> getTemplates() {
        if (templates == null) {
            templates = new LinkedList<>();
        }
        return templates;
    }

    /**
     * Set the templates objects list.
     * @param templates the templates objects list.
     */
    public void setTemplates(List<TemplateObject> templates) {
        this.templates = templates;
    }

    /**
     * Get the data to be rendered.
     * @return the data to be rendered.
     */
    public Object getData() {
        return data;
    }

    /**
     * Set the data to be rendered.
     * @param data the data to be rendered.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Get the input csv data.
     * @return the input csv data.
     */
    public CsvData getCsvData() {
        return csvData;
    }

    /**
     * Set the input csv data.
     * @param csvData the input csv data.
     */
    public void setCsvData(CsvData csvData) {
        this.csvData = csvData;
    }

    /**
     * Get the csv data to be rendered.
     * @param dataFolder data folder to read from.
     * @return the csv data to be rendered.
     * @throws IOException if an I/O error occurs.
     */
    @JsonIgnore
    public Object getCsvDataObject(File dataFolder) throws IOException {
        if (csvData != null) {
            return csvData.getObject(dataFolder);
        }
        return null;
    }
}
