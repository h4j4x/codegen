package com.ajax.codegen.app.model;

import java.util.Map;

public class DataInput {
    private Map<String, String> templatesFilesMap;
    private Object data;

    public Map<String, String> getTemplatesFilesMap() {
        return templatesFilesMap;
    }

    public void setTemplatesFilesMap(Map<String, String> templatesFilesMap) {
        this.templatesFilesMap = templatesFilesMap;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
