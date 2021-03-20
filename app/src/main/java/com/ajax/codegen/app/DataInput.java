package com.ajax.codegen.app;

import java.util.List;

public class DataInput {
    private List<String> templates;
    private Object data;

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
