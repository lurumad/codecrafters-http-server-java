import server.HttpServer;

public class Main {
    public static void main(String[] args) {
        var server = new HttpServer(4221);
        server.start();
    }
}
