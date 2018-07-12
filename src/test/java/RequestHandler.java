import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {
    private Socket socket;
    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream());)  {
            out.println("Hello World");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
