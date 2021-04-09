package io.github.h4j4x.codegen.lib.model;

/**
 * Templates data.
 */
public class TemplateObject {
    private String template;
    private String file;
    private String mergeInTemplate;
    private String mergeInFile;
    private Integer mergeOrder;

    /**
     * Get template name.
     * @return the template name. (relative to template folder)
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set template name.
     * @param template the template name. (relative to template folder)
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Get target file path for generation.
     * @return the file path. (relative to output folder)
     */
    public String getFile() {
        return file;
    }

    /**
     * Set target file path for generation.
     * @param file the file path. (relative to output folder)
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Get the template to be merged in.
     * @return the template to be merged in.
     */
    public String getMergeInTemplate() {
        return mergeInTemplate;
    }

    /**
     * Set the template to be merged in.
     * @param mergeInTemplate the template to be merged in.
     */
    public void setMergeInTemplate(String mergeInTemplate) {
        this.mergeInTemplate = mergeInTemplate;
    }

    /**
     * Get the output file path to be merged in for generation.
     * @return the output file path to be merged in for generation. (relative to output folder)
     */
    public String getMergeInFile() {
        return mergeInFile;
    }

    /**
     * Set the output file path to be merged in for generation.
     * @param mergeInFile the output file path to be merged in for generation. (relative to output folder)
     */
    public void setMergeInFile(String mergeInFile) {
        this.mergeInFile = mergeInFile;
    }

    /**
     * Get content order for merge content. Lower order values renders first.
     * @return content order. Defaults to {@literal Integer.MAX_VALUE}
     */
    public Integer getMergeOrder() {
        if (mergeOrder == null) {
            mergeOrder =  Integer.MAX_VALUE;
        }
        return mergeOrder;
    }

    /**
     * Set content order for merge content. Lower order values renders first.
     * @param mergeOrder content order. Defaults to {@literal Integer.MAX_VALUE}
     */
    public void setMergeOrder(Integer mergeOrder) {
        this.mergeOrder = mergeOrder;
    }

    /**
     * Return if template has been specified.
     * @return {@literal true} if template has been specified.
     */
    public boolean hasTemplate() {
        return template != null
            && !template.isBlank();
    }

    /**
     * Return if target file path has been specified.
     * @return {@literal true} if target file path has been specified.
     */
    public boolean hasFile() {
        return hasTemplate()
            && file != null
            && !file.isBlank();
    }

    /**
     * Return if merge data has been specified.
     * @return {@literal true} if merge data has been specified.
     */
    public boolean hasMerge() {
        return hasTemplate()
            && mergeInTemplate != null
            && !mergeInTemplate.isBlank()
            && mergeInFile != null
            && !mergeInFile.isBlank();
    }
}
