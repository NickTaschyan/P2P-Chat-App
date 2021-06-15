import java.io.*;
import java.net.*;


public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));        // BufferedReader is used to grab incoming messages the server is receiving
            // create a InputStream so we can read data from it.
            String message;
            // read the message from the socket
            do {
                try{
                    message = inputStream.readLine(); // try to read the messages, if the message is null, this means the socket might have closed so we break if so.
                }
                catch(SocketException e){
                    break;
                }
                if (message == null){
                    break;      // similar check to see if the socket is closed
                }
                if (message.contains("*#")) {       // this is my personal check, the initial startup sends a #* followed by the port to let the server know to connect back
                    message = message.replaceAll("\\D+", "");       // some regex and calls to clean up the message
                    String sockIP = socket.getRemoteSocketAddress().toString();
                    String[] splitIP = sockIP.split("[:].*");
                    String[] connectIP = splitIP[0].split("[/]");
                    new ClientThread(connectIP[1], message).start();        // make a new client thread connection to connect back to the incoming socket
                }
                else if(message.contains("TERMINATE")){             // if the message contains TERMINATE in all caps this means the sender has terminated and we should respond accordingly on the server end
                    String[] splitQuit = message.split("\\s+");     // some regex to clean up the message
                    for (int i = 0; i < ListeningThread.SocketList.size(); i++){
                        if (ListeningThread.SocketList.get(i).getInetAddress().toString().contains(splitQuit[1]) && (Integer.toString(ListeningThread.SocketList.get(i).getPort()).contains(splitQuit[2]))){
                            ClientThread.terminate(i); // if the termination IP and Port match our data in the SocketList list then we find it and also remove it from the list.
                        }
                    }
                }
                else {
                    String[] splitInput = message.split("\\s+");
                    message = message.replaceAll(".+?(?=:)", "");
                    System.out.println("Message received from: "+  splitInput[0] + "\nSender's Port: " +  splitInput[1] + "\nMessage" + message); // if the above cases aren't true, then we just have a message from a user so we deal with it
                }
            }while (socket.isConnected()); // run this loop until the socket is disconnected.
                socket.close(); // close socket once its all done .

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}