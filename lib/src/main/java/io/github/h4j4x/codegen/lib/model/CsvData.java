package io.github.h4j4x.codegen.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.h4j4x.codegen.lib.internal.util.CsvUtils;
import io.github.h4j4x.codegen.lib.internal.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Csv input data.
 */
public class CsvData {
    private String csvKey;
    private String dataKey;
    private List<String> fields;
    private String filePath;
    private Object data;

    @JsonIgnore
    private Object object;

    /**
     * Get the csv key for template.
     * @return the csv key for template.
     */
    public String getCsvKey() {
        return csvKey;
    }

    /**
     * Set the csv key for template.
     * @param csvKey the csv key for template.
     */
    public void setCsvKey(String csvKey) {
        this.csvKey = csvKey;
    }

    /**
     * Get the data key for template.
     * @return the data key for template.
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * Set the data key for template.
     * @param dataKey the data key for template.
     */
    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    /**
     * Get the csv fields to be read from file. (ordered columns)
     * @return the csv fields to be read from file. (ordered columns)
     */
    public List<String> getFields() {
        return fields;
    }

    /**
     * Set the csv fields to be read from file. (ordered columns)
     * @param fields the csv fields to be read from file. (ordered columns)
     */
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * Set the csv file path to be read. (relative to data folder)
     * @return the csv file path to be read. (relative to data folder)
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the csv file path to be read. (relative to data folder)
     * @param filePath the csv file path to be read. (relative to data folder)
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get the common data.
     * @return the common data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Set the common data.
     * @param data the common data.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Get the object to be rendered.
     * @param dataFolder the data folder to be read.
     * @return the object to be rendered.
     * @throws IOException if an I/O error occurs.
     */
    public Object getObject(File dataFolder) throws IOException {
        if (object == null) {
            File file = FileUtils.getFile(dataFolder, filePath);
            if (!file.exists()) {
                file = FileUtils.getFile(null, filePath);
            }
            Map<String, Object> root = new HashMap<>();
            if (data != null) {
                root.put(dataKey, data);
            }
            if (file.exists() && file.canRead()) {
                List<Map<String, String>> list = new LinkedList<>();
                List<List<String>> rawCsv = CsvUtils.parseFile(file);
                for (List<String> raw : rawCsv) {
                    Map<String, String> data = new HashMap<>();
                    for (int i = 0; i < fields.size(); i++) {
                        data.put(fields.get(i), raw.get(i));
                    }
                    list.add(data);
                }
                root.put(csvKey, list);
            }
            object = root;
        }
        return object;
    }
}
