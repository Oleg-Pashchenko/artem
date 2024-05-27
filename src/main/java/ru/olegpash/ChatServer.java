package ru.olegpash;

import java.io.*;
import java.net.*;

public class ChatServer {
    private static Socket clientSocket1 = null;
    private static Socket clientSocket2 = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущен. Ожидание подключения клиентов...");

            clientSocket1 = serverSocket.accept();
            System.out.println("Клиент 1 подключился.");

            clientSocket2 = serverSocket.accept();
            System.out.println("Клиент 2 подключился.");

            Thread client1Handler = new Thread(new ClientHandler(clientSocket1, clientSocket2));
            Thread client2Handler = new Thread(new ClientHandler(clientSocket2, clientSocket1));

            client1Handler.start();
            client2Handler.start();

            client1Handler.join();
            client2Handler.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private Socket otherClientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket, Socket otherClientSocket) {
            this.clientSocket = clientSocket;
            this.otherClientSocket = otherClientSocket;
            try {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(otherClientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Получено: " + message);
                    out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    otherClientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
