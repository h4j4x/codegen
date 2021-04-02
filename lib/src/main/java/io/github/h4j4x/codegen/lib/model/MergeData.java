package io.github.h4j4x.codegen.lib.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Merge data object.
 */
public class MergeData {
    private final String template;
    private final String file;
    private final List<MergeObject> objects;

    /**
     * Create merge data object.
     * @param template the template name.
     * @param file the output file.
     */
    public MergeData(String template, String file) {
        this.template = template;
        this.file = file;
        objects = new LinkedList<>();
    }

    /**
     * Get the template name.
     * @return the template name.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Get the output file.
     * @return the output file.
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the objects list to be merged.
     * @return the objects list to be merged.
     */
    public List<MergeObject> getObjects() {
        return objects;
    }

    /**
     * Get if has valid data.
     * @return {@literal true} if has valid data.
     */
    public boolean isValid() {
        return template != null
            && !template.isBlank()
            && file != null
            && !file.isBlank()
            && !objects.isEmpty();
    }

    /**
     * Add a new object to be merged.
     * @param object the new object to be merged.
     */
    public void addObject(MergeObject object) {
        if (object != null) {
            objects.add(object);
        }
    }
}
