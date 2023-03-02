import model.Lesson48Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
    try {
        new Lesson48Server("localhost", 8965).start()
    }catch (IOException ex){
        ex.printStackTrace();
    }
}