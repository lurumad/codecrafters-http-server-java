import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        PrintWriter out = null;
        try {
            serverSocket = new ServerSocket(4221);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            var socket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            out = new PrintWriter(socket.getOutputStream(), true);
            var response = HttpResponse.ok();
            out.println(
                    new CRLFString(
                            new CRLFString(
                                    String.format("%s %s", response.getHttpVersion(), response.getStatus())
                            ).toString()
                    )
            );

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }

            if (out != null) {
                out.close();
            }
        }
    }
}
