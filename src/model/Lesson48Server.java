package model;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import server.BasicServer;
import server.ContentType;
import server.ResponseCodes;
import server.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Lesson48Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private static CandidateGeneration candidates = new CandidateGeneration();
    private static Candidate currentVotedCandidate = new Candidate();
    private static Integer allVotes = 0;
    public Lesson48Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this ::candidateRender);
        registerGet("/thankyou", this::thankYouRender);
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
        Map<String, Object> data = new HashMap<>();
        data.put("candidate", currentVotedCandidate);
        data.put("percent", (currentVotedCandidate.getVotes() * 100 / allVotes));
        renderTemplate(exchange, "thankyou.html", data);
    }
    private void voteRender(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        for(var candidate: candidates.getCandidates()){
        if(parsed.get("candidateId").equalsIgnoreCase(candidate.getId().toString())){
           candidate.setVotes(candidate.getVotes() + 1);
           currentVotedCandidate = candidate;
           break;
            }
        }
        allVotes++;
        redirect303(exchange, "/thankyou");

    }
    private void votesRender(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("allvotes", allVotes);
        data.put("candidates", candidates.getCandidates().stream().sorted((Comparator.comparing(Candidate::getVotes)).reversed()).collect(Collectors.toList()));
        renderTemplate(exchange, "votes.html", data);
    }
    private void candidateRender(HttpExchange exchange) {
      renderTemplate(exchange, "candidates.html", candidates);
    }
}

