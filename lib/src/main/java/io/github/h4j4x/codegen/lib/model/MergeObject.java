package io.github.h4j4x.codegen.lib.model;

/**
 * Merge templates data object.
 */
public class MergeObject {
    private final String template;
    private final Object data;
    private final Integer order;

    /**
     * Creates merge data object.
     * @param template the template name.
     * @param data the common data for render in template.
     * @param order the content order.
     */
    public MergeObject(String template, Object data, Integer order) {
        this.template = template;
        this.data = data;
        this.order = order;
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

    /**
     * Get content order for merge content. Lower order values renders first.
     * @return content order.
     */
    public Integer getOrder() {
        if (order == null) {
            return Integer.MAX_VALUE;
        }
        return order;
    }
}
