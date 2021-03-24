package io.github.h4j4x.codegen.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.h4j4x.codegen.common.parser.CsvParser;
import io.github.h4j4x.codegen.common.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CsvData {
    private String csvKey;
    private String dataKey;
    private List<String> fields;
    private String filePath;
    private Object data;

    @JsonIgnore
    private Object object;

    public String getCsvKey() {
        return csvKey;
    }

    public void setCsvKey(String csvKey) {
        this.csvKey = csvKey;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

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
                List<List<String>> rawCsv = CsvParser.parseFile(file);
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
