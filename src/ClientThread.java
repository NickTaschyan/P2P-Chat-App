import java.io.*;
import java.net.*;
import java.util.LinkedList;


class connectData{      // this class stores all the connected data in a clean manner to compare with when we attempt to connect to other sockets
    String IPAddress;
    String portNumber;
    public connectData(String IPAddress, String portNumber){
        this.IPAddress = IPAddress;
        this.portNumber = portNumber;
    }
}
public class ClientThread extends Thread {
    public String IP;
    public String Port;
    public static LinkedList<connectData> connectedData = new LinkedList<>();       // linked list of connectData objects containing information about connected sockets
    public ClientThread(String ip, String port){        // initiate with IP and port
        this.Port = port;
        this.IP = ip;
    }
    public static String serverPort;
    public static String serverIP;

    public static void sendMessage(String ID, String Message) throws IOException {
        if (Integer.parseInt(ID) > (ListeningThread.SocketList.size() - 1)){        // if the ID we want to send a message too doesn't exist this flag catches the attempt and returns
            System.out.println("ID out of scope.");
            return;
        }

        PrintWriter writer = new PrintWriter( ListeningThread.SocketList.get(Integer.parseInt(ID)).getOutputStream(), true);        // PrinterWriter allows us to send messages to the socket
        writer.println(serverIP + " " + serverPort + " : " + Message);  // In this case we want to send the server our own servers information along with a message so that it knows to connect back to our server
        String DestinationIP = ListeningThread.SocketList.get(Integer.parseInt(ID)).getInetAddress().toString();    // this just grabs the IP Address of the thread we are sending a message too
        DestinationIP = DestinationIP.replaceAll("[a-z,A-Z,/,-]", "");  // we use Regex to clean up the default string the Socket provides in its data set
        System.out.println("Message sent to " + DestinationIP); // cleanly print to the user that our message was sent.
    }


    public void run(){
        for (connectData connectedDatum : connectedData) {
            if (connectedDatum.portNumber.equals(Port) && connectedDatum.IPAddress.equals(IP)) {    // if we are attempting to connect to a socket that already has a connection we return
                return;
            }
        }

        try (Socket socket = new Socket(IP, Integer.parseInt(Port))){       // attempt to connect to a socket server
            System.out.println("Successfully Connected to " + socket.getRemoteSocketAddress() + " at port: " + socket.getPort()); // if successful print the message

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // again we use Writer to send information to the server

            writer.println("*#" + String.valueOf(serverPort));      // upon initial connection, we send the server details about our port so that they can reconnect to us and establish P2P connection
            ListeningThread.addToList(socket);      // add the socket to the list of connections
            connectedData.add(new connectData(IP,Port));        // also add to our compare list
            while(true){
            // while loop to keep socket open and thread alive
            }

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println(ex.getMessage() + " Invalid IP address or Port number"); // Default try catch blocks provided by IOException and unknownhost exception
        }
    }

    public static void sendTermination(String ID) throws IOException{       // When this method is called, we send the server socket a message saying we are termminating
        if (ListeningThread.SocketList.isEmpty()){                           // this way, the server knows to close the connection on it's end too
            System.out.println("No connections to terminate.");             // if the list is empty we return
            return;
        }
        else if (Integer.parseInt(ID) > ListeningThread.SocketList.size() - 1){     // similarily if the ID is out of scope we return
            System.out.println("ID out of scope.");
            return;
        }
        PrintWriter writer = new PrintWriter(ListeningThread.SocketList.get(Integer.parseInt(ID)).getOutputStream(), true); // write to the server socket telling it we are closing
        writer.println("TERMINATE" + " " + serverIP + " " + serverPort);

    }

    public static void terminate(int ID) throws IOException {
        if (ListeningThread.SocketList.isEmpty()){
            System.out.println("No connections to terminate.");     // check to see if the list is empty before attempting to terminate
            return;
        }
        else if (ID > ListeningThread.SocketList.size() - 1){
            System.out.println("ID out of scope.");     // check to see if valid ID number to terminate
            return;
        }
        System.out.println("Connection to Peer " + ListeningThread.SocketList.get(ID).getRemoteSocketAddress() + " has been terminated...");        // print message confirming actions
        sendTermination(Integer.toString(ID));      // call sendTermination to send server socket a notice of shutdown
        ListeningThread.SocketList.get(ID).close();     // close the socket
        ListeningThread.removeFromList(ID);     // remove it from the lists


    }

}
