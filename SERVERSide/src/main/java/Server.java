import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{

    PokerInfo test2;
    int count = 1;
    boolean serverOn = false;
    Integer[] numbers = {1,2,3,4,5};
    int chosenPort;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private final Consumer<Serializable> callback;



    Server(Consumer<Serializable> call, int portNum){
        callback = call;
        chosenPort = portNum;

//        server = new TheServer();
//        server.start();
    }
    public void onServer() {
        server = new TheServer();
        server.start();
    }
    public void closeServer() {
        try {
            this.server.mysocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.clients.forEach(thread-> {
            try {
                PokerInfo temp = new PokerInfo();
                temp.closeScreen = true;
                thread.out.writeObject(temp);

                thread.connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    public class TheServer extends Thread{

        ServerSocket mysocket;
        public void run() {

            try{
                mysocket = new ServerSocket(chosenPort);
                System.out.println("Server is waiting for a client!");
                serverOn = true;

                try {
                    while(serverOn) {
//                        if(paused){
//                            continue;
//                        }
                        ClientThread c = new ClientThread(mysocket.accept(), count);
//                        if(paused){
//                            continue;
//                        }
                        clients.add(c);

                        test2 = new PokerInfo();
                        test2.maxPlayers = clients.size() > 4;
                        test2.maxOnce = true;
                        test2.justJoined = true;
                        test2.playerNum = count;

                        c.start();

                        System.out.println("There are " + clients.size() + " players.");
                        count++;

                    }
                }
                catch(Exception e) {
                    System.out.println("Server is off or max number of players on server.");
                }
            }//end of try
            catch(Exception e) {
                System.out.println("Server socket not opened");
            }
        }//end of while
    }


    class ClientThread extends Thread{
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ArrayList<Card> deck = new ArrayList<>(52);
        Deck makeDeck = new Deck();

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            try{
                out.writeObject(test2);
            }
            catch(Exception e){
                System.out.println("Output failure");
            }

            while(true) {
                try {
                    deck = makeDeck.createDeck();
//                    if(paused){
//                        continue;
//                    }
                    PokerInfo data = (PokerInfo) in.readObject();
//                    if(paused){
//                        continue;
//                    }
                    ThreeCardLogic info = new ThreeCardLogic(data,deck,clients.size());

                    info.saveData(data);
                    data = info.getData();

                    data.totalPlayers = clients.size();

                    data.playerNum = count;

                    callback.accept(data);



                    out.reset();
                    out.writeObject(data);

                }
                catch(Exception e) {


                    clients.remove(this);

                    PokerInfo data = new PokerInfo();
                    data.left = true;
                    data.playerNum = count;
                    data.totalPlayers = clients.size();
                    callback.accept(data);
                    e.printStackTrace();
                    --count;

                    break;
                }
            }
        }//end of run


    }//end of client thread
}