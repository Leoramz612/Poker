import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    int portNum;
    String addressNum;

    private Consumer<Serializable> callback;


    Client(Consumer<Serializable> call, int port, String address){

        callback = call;
        portNum = port;
        addressNum = address;
    }

    public void run() {

        try {
            socketClient= new Socket(addressNum,portNum);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {
            System.out.println("Streams not opened correctly");
        }

        while(true) {
            try {
                PokerInfo data = (PokerInfo) in.readObject();
                callback.accept(data);
            }
            catch(Exception e) {
            }
        }
    }
    public void send(PokerInfo data) {
        try {
            out.reset();
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("server is closed");
            e.printStackTrace();
        }
    }


}