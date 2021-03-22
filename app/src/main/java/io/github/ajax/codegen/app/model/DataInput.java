package io.github.ajax.codegen.app.model;

import java.util.LinkedList;
import java.util.List;

public class DataInput {
    private List<TemplateObject> templates;
    private Object data;

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
}
