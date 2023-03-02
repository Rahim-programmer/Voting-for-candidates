package FileService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Candidate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Paths.get;

public class FileService {
    private static final Gson GSON = new GsonBuilder().create();
    public static List<Candidate> readFile(){
        Path path = get("src/json/candidates.json");
        String json = "";
        try{
            json = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Candidate[] books = GSON.fromJson(json, Candidate[].class);
        return List.of(books);
    }
}
