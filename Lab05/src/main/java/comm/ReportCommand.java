package comm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import repo.Repository;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ReportCommand implements Command {
    private Repository repository;

    public ReportCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        try {
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
            Template template = cfg.getTemplate("report.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("resources", repository.getResources());

            File htmlFile = new File("repository_report.html");
            try (Writer out = new FileWriter(htmlFile)) {
                template.process(data, out);
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}