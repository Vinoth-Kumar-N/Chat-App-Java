package client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private final BufferedReader in;
    private final PrintWriter out;
    private final Consumer<String> onMessageReceived;

    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
        Socket socket = new Socket(serverAddress, serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void startClient() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 3000, System.out::println);
    }
}