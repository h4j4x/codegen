package io.github.h4j4x.codegen.lib.model;

/**
 * Merge templates data object.
 */
public class MergeObject {
    private final String template;
    private final Object data;

    /**
     * Creates merge data object.
     * @param template the template name.
     * @param data the common data for render in template.
     */
    public MergeObject(String template, Object data) {
        this.template = template;
        this.data = data;
    }

    /**
     * Get the template name.
     * @return the template name.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Get the data for render in template.
     * @return the common data for render in template.
     */
    public Object getData() {
        return data;
    }
}
