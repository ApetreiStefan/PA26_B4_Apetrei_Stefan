package com.report;

import com.model.MovieReportEntry;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private final Configuration freemarker;

    public ReportGenerator() throws Exception {
        // Point FreeMarker at the /resources folder on the classpath
        freemarker = new Configuration(Configuration.VERSION_2_3_33);
        freemarker.setClassForTemplateLoading(getClass(), "/");
        freemarker.setDefaultEncoding("UTF-8");
        freemarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * Renders com.report.ftl with the given movie data and writes the result
     * to the specified output file path (e.g. "com.report.html").
     */
    public void generate(List<MovieReportEntry> movies, String outputPath) throws Exception {
        Template template = freemarker.getTemplate("report.ftl");

        // The data com.model is a plain Map — FreeMarker reads it from the template
        Map<String, Object> model = new HashMap<>();
        model.put("movies", movies);
        model.put("generatedAt", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));

        try (Writer writer = new FileWriter(outputPath)) {
            template.process(model, writer);
        }

        System.out.println("Report written to: " + outputPath);
    }
}