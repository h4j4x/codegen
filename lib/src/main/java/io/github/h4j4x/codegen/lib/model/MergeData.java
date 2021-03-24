package io.github.h4j4x.codegen.lib.model;

import java.util.LinkedList;
import java.util.List;

public class MergeData {
    private final String template;
    private final String file;
    private final List<MergeObject> objects;

    public MergeData(String template, String file) {
        this.template = template;
        this.file = file;
        objects = new LinkedList<>();
    }

    public String getTemplate() {
        return template;
    }

    public String getFile() {
        return file;
    }

    public List<MergeObject> getObjects() {
        return objects;
    }

    public boolean isValid() {
        return template != null
            && !template.isBlank()
            && file != null
            && !file.isBlank()
            && !objects.isEmpty();
    }

    public void addObject(MergeObject object) {
        if (object != null) {
            objects.add(object);
        }
    }
}
