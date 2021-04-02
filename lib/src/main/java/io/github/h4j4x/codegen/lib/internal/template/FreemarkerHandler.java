package io.github.h4j4x.codegen.lib.internal.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.github.h4j4x.codegen.lib.internal.error.TemplateError;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Freemarker templates handler.
 */
public class FreemarkerHandler {
    private final Configuration config;

    /**
     * Create template handler.
     * @param templatesDir the folder to read templates from.
     * @throws IOException if an I/O error occurs.
     */
    public FreemarkerHandler(File templatesDir) throws IOException {
        config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDirectoryForTemplateLoading(templatesDir);
        config.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
        config.setFallbackOnNullLoopVariable(false);
    }

    /**
     * Render a template.
     * @param template the template name.
     * @param data the data input.
     * @return the rendered result as string.
     * @throws IOException if an I/O error occurs.
     * @throws TemplateError if a template processing error occurs.
     */
    public String render(String template, Object data) throws IOException, TemplateError {
        Template temp = config.getTemplate(fixTemplateName(template));
        Writer out = new StringWriter();
        try {
            temp.process(data, out);
            return out.toString();
        } catch (TemplateException e) {
            throw new TemplateError(e.getMessage(), e);
        }
    }

    private String fixTemplateName(String name) {
        String templateName = "" + name;
        int sepIndex = templateName.lastIndexOf(".");
        if (sepIndex < 0) {
            templateName += ".ftl";
        }
        return templateName;
    }
}
