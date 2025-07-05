import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
 
public class Server {
    private static final int PORT = 12345;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java"; 
    private static final String DB_USER = "root";                         
    private static final String DB_PASSWORD = "Kshirod#2003";             

    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[Database] JDBC Driver Loaded.");
            try (Connection testConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                System.out.println("[Database] Connection successful!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[Database] Failed to initialize:");
            e.printStackTrace();
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[Server] New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    static void saveMessageToDatabase(String sender, String message) {
        System.out.println("[Database] Attempting to save: " + sender + " - " + message);
        String sql = "INSERT INTO ChatMessages (sender, message) VALUES (?, ?)"; 

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sender);
            stmt.setString(2, message);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("[Database] Saved message -> Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("[Database] Error while saving message:");
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clientName = in.readLine();
                System.out.println("[Server] " + clientName + " connected.");
                broadcast(clientName + " has joined the chat.", this);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("[Server] Received from " + clientName + ": " + message);
                    broadcast(clientName + ": " + message, this);
                    saveMessageToDatabase(clientName, message);
                }
            } catch (IOException e) {
                System.out.println("[Server] " + clientName + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                removeClient(this);
                broadcast(clientName + " has left the chat.", this);
            }
        }

        void sendMessage(String message) {
            out.println(message);
        }
    }
}
