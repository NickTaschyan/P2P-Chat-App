import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class chat {
        public static void main(String[] args) throws IOException {
            if (args.length < 1) return;
            int port = Integer.parseInt(args[0]);
            ListeningThread.PORT = port;
            ClientThread.serverPort = Integer.toString(port);
            String myIP = InetAddress.getLocalHost().toString();      // Grabs IP address of local machine
            myIP = myIP.replaceAll("[a-z,A-Z,/,-]", "");
            ClientThread.serverIP = myIP;
            new ListeningThread().start();      // starts the Listener Thread, this thread will listen for incoming connections and handle them.
            Scanner scanner = new Scanner(System.in);       // create a scanner object to grab input from user
            String input;
            do {
                input = scanner.nextLine(); // scan for inputs
                if (input.equals("help")){      // bunch of checks to see if entered input are for commands
                    System.out.println("Welcome to Chat Application Project\n\nHere are some commands to help you:\n\nhelp:\t\t\t\t\tThis command shows the help screen." +
                            "\nmyip:\t\t\t\t\tDisplays the IP address of this computer.\nmyport:\t\t\t\t\tDisplay the " +
                            "port number this process is listening on.\nconnect <destination> <portno>:\t\tThis command establishes a new TCP connection the the specified <destination> and <portno>\n\t\t\t\t\t destination must be an IP address of machine you wish to connect too." +
                            "\nlist:\t\t\t\t\tThis command lists all the connections this process is apart of displaying the IP Address and port number.\n" +
                            "terminate <connection ID>:\t\tThis command terminates the connection associated with the ID number. Example : terminate 1\n\t\t\t\t\t, terminates the connection in list ID 1.\nsend <connectionID> <message>:\t\tSimilarly to terminate this " +
                            "command sends a message to the\n\t\t\t\t\t selected connection ID. Example send 2 Hi! will send Hi! to connection id 2.\nexit:\t\t\t\t\tThis command terminates all connections and exits the application.");
                }
                if (input.equals("myip")){ // returns ip of local machine
                    System.out.println("IP address: "+ myIP);
                }
                if (input.equals("myport")){ // returns port
                    System.out.println("Listening port: "+ port);
                }
                if (input.contains("connect")){
                    String[] splitInput = input.split(  "\\s+");          // splits the string based off of whitespaces
                    if (myIP.contains(splitInput[1]) && splitInput[2].equals(String.valueOf(port))) { // check for self connection
                        System.out.println("You cannot connect yourself!");
                    }
                    else {
                        for (int i = 0; i < ClientThread.connectedData.size(); i++){ // check for attempt to connect to already existing connection
                            if (ClientThread.connectedData.get(i).IPAddress.equals(splitInput[1]) && ClientThread.connectedData.get(i).portNumber.equals(splitInput[2])){
                                System.out.println("Already a connection made!");
                                break;
                            }
                        }
                        new ClientThread(splitInput[1], splitInput[2]).start();   // grabs ip address, port number, starts thread
                    }

                }
                if (input.equals("list")){ // returns list of connected sockets
                    ListeningThread.List();
                }
                if (input.contains("terminate")){
                    String[] splitInput = input.split("\\s+");          // splits the string based off of whitespaces
                    //System.out.println(splitInput[1]);          // grabs the IP connection ID
                    ClientThread.terminate(Integer.parseInt(splitInput[1]));
                }
                if (input.contains("send")) {
                    String[] splitInput = input.split("\\s+");          // splits the string based off of whitespaces
                   // (splitInput[1]);          // grabs the IP connection ID
                    String[] message;
                    message = input.split(" ", 3);
                    // message[2] = message to send to user
                    ClientThread.sendMessage(splitInput[1], message[2]);
                }
            }while(!input.equals("exit"));      // run app until message received is exit
            for (int i = 0; i < ListeningThread.SocketList.size(); i++){    // if we are exiting, close off all socket connections
                ClientThread.terminate(i);
            }
            System.exit(1); // exit

        }
    }
