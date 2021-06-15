import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class ListeningThread extends Thread{
    public static int PORT;
    public static LinkedList<Socket> SocketList = new LinkedList<>();       // List of Socket objects

    public void run(){
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {      // Opens up server socket and listens on port
            System.out.println("Server is listening on port " + PORT);
            while (true) {          // this loop is constantly listening for new connections
                Socket socket = serverSocket.accept();
                String clientIP;
                clientIP = socket.getRemoteSocketAddress().toString();      // when a new client joins we accept the socket and print it's incoming IP Address
                System.out.println("New client " + clientIP + " connected.");
                new ServerThread(socket).start();       // Since we have a new client, we start the client thread

            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());         // IO Exception provides try catch blocks in case the process goes bad
            ex.printStackTrace();
        }

    }
    public static void List(){
        System.out.println("id: IP Address\t\tPort No.\n");         // when List() is called we print out the list of connected sockets.
        for (int i = 0; i < SocketList.size(); i++){
            System.out.println(i + ": " + SocketList.get(i).getRemoteSocketAddress() + "\t\t" + SocketList.get(i).getPort()+"\n");  // SocketList contains a list of socket objects to keep track of
        }

    }
    public static void addToList(Socket socket){
        SocketList.add(socket);
    }       // when we get a new socket, we add it to the list

    public static void removeFromList(int ID){
        ClientThread.connectedData.remove(ID);      // part of terminating process, remove the Socket from our list to clear up space for new sockets
        SocketList.remove(ID);
    }

}
