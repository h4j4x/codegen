package io.github.ajax.codegen.app.model;

public class MergeObject {
    private final String template;
    private final Object data;

    public MergeObject(String template, Object data) {
        this.template = template;
        this.data = data;
    }

    public String getTemplate() {
        return template;
    }

    public Object getData() {
        return data;
    }
}
