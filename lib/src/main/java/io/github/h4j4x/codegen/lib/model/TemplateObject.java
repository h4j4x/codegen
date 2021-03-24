package io.github.h4j4x.codegen.lib.model;

public class TemplateObject {
    private String template;
    private String file;
    private String mergeInTemplate;
    private String mergeInFile;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMergeInTemplate() {
        return mergeInTemplate;
    }

    public void setMergeInTemplate(String mergeInTemplate) {
        this.mergeInTemplate = mergeInTemplate;
    }

    public String getMergeInFile() {
        return mergeInFile;
    }

    public void setMergeInFile(String mergeInFile) {
        this.mergeInFile = mergeInFile;
    }

    public boolean hasTemplate() {
        return template != null
            && !template.isBlank();
    }

    public boolean hasFile() {
        return hasTemplate()
            && file != null
            && !file.isBlank();
    }

    public boolean hasMerge() {
        return hasTemplate()
            && mergeInTemplate != null
            && !mergeInTemplate.isBlank()
            && mergeInFile != null
            && !mergeInFile.isBlank();
    }
}
