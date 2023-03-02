package model;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import server.BasicServer;
import server.ContentType;
import server.ResponseCodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Lesson48Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    public Lesson48Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this ::candidateRender);
        registerGet("/thankYou", this::thankYouRender);
        registerPost("/vote", this :: voteRender);
        registerGet("/votes", this :: votesRender);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {

            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
    private void thankYouRender(HttpExchange exchange) {
        renderTemplate(exchange, "candidate.html", getBooksDataModel());
    }
    private void voteRender(HttpExchange exchange) {
        renderTemplate(exchange, "votes.html", getBooksDataModel());
    }
    private void votesRender(HttpExchange exchange) {
        renderTemplate(exchange, "votes.html", getBooksDataModel());
    }
    private void candidateRender(HttpExchange exchange) {
        renderTemplate(exchange, "votes.html", getSampleDataModel());
    }
}

