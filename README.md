# Instructions

For this project you can use Linux like Ubuntu or a command line like Windows Powershell. The chat application has a menu with all the necessary commands which can be reached by typing "help". You can check your local machines ip address and port number with the myip and myport commands. To connect with another client use the connect command with the destination ip address and port number (the program must be running on the destination machine aswell). To list all your connections use the list command. To terminate the connection use the terminate command with the ID number corresponding to the list. To send a message to another client use the send command with the ID number and the message itself. To exit the application use the exit command. This will terminate all connections.    

# Compilation 

## For a linux machine:

Download the necessary files and place them in your desired directory, then on the command line navigate to the directory containing the project files. run "make" and the makefile should just handle the rest and compile the code. If you want to run the program, simply type "java chat XXXX" where XXXX is the port number you wish to use to listen. 

## For a windows machine: 

If you are on a windows machine, again place all the existing .java in a directory of your choice and open up windows powershell. With powershell navigate to the directory that contains the .java files and type "javac chat.java ClientThread.java ListeningThread.java ServerThread.java", if this doesn't work then you don't have the right java jdk files to run commands like this in powershell and you should enable those in the system enviornment. If this compiles successfully, then you should notice a bunch of .class files being generated. If it compiled successfully, you can begin the chat app by typing "java chat XXXX" where XXXX is the port you desire to host the server. If it doesn't compile I reccomend compiling the program in your IDE and then just locating the .class files to put in the project directory. 

# Demo 

![P2P Chat App - Animated gif demo](P2P-Chat-App/Demo GIF/P2P Chat Application.gif)
