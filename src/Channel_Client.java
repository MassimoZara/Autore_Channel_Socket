import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Channel_Client {

	public void startClient() throws IOException, InterruptedException {
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", Channel_Server.port);
		
		SocketChannel server = SocketChannel.open(hostAddress);		//apre un canale socket e lo connette all'indirizzo
		System.out.println("Client connesso");
		
		String message = "Client 1";
		ByteBuffer buffer = ByteBuffer.allocate(74);		//inzializzo un buffer di 74 bytes
		buffer.put(message.getBytes());
		buffer.flip();
		server.write(buffer);
		System.out.println("Messaggio inviato");
		buffer.clear();
		
		server.close();		//chiude la connessione
	}

	public static void main(String[] args) throws InterruptedException {
		Channel_Client c= new Channel_Client();
		try {
			c.startClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
}
