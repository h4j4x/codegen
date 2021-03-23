package io.github.h4j4x.codegen.lib.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.github.h4j4x.codegen.lib.error.TemplateError;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class FreemarkerHandler {
    private final Configuration config;

    public FreemarkerHandler(File templatesDir) throws IOException {
        config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDirectoryForTemplateLoading(templatesDir);
        config.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
        config.setFallbackOnNullLoopVariable(false);
    }

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
