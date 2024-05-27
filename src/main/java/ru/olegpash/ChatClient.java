package ru.olegpash;

import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Подключено к серверу чата. Для отправки сообщения введите его и нажмите enter...");

            Thread readThread = new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        System.out.println("Получено: " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readThread.start();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
