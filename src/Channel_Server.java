import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Channel_Server {
	private Selector selector;
	private InetSocketAddress listenAddress;
	public final static int port = 35353;
	
	public Channel_Server() {
		listenAddress = new InetSocketAddress("localhost",port);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Channel_Server().loop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loop() throws IOException {
		this.selector = Selector.open();
		
		ServerSocketChannel sChannel = ServerSocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.socket().bind(listenAddress);
		sChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("Server inizializzato nella porta >> " + this.port);
		int i =1;
		while (true) {
			
			int selectorCount = selector.select();
			if (selectorCount == 0) {
				continue;
			}
		Set<SelectionKey> keySet = selector.selectedKeys();
		Iterator iterator = keySet.iterator();

		while(iterator.hasNext()) {
			SelectionKey key = (SelectionKey) iterator.next();

			// Remove key from set so we don't process it twice
			iterator.remove();
		
			if (!key.isValid()) {
				continue;
			}

			if (key.isAcceptable()) { // Accept client connections
				this.accept(key);
				System.out.println("Cliet accettato");
				
			} else if (key.isReadable()) { // Read from client
				String message = read(key);
				System.out.println(message);
				}
			}
		}
	}
	
	public void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();	//prendo il canale dalla chiave e lo assegno al serverChannel 
		
		SocketChannel channel = serverChannel.accept();		//accetto la connessione dal socket di questo canale
		
		channel.configureBlocking(false);	//imposto in modalità non bloccante
		
		Socket socket = channel.socket();	//ottengo il socket di questo canale
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();		//ottengo l'indirizzo del socket
		System.out.println("Connesso a: " + remoteAddr);

		channel.register(this.selector, SelectionKey.OP_READ);		//registo sul selettore questo canale in modalità lettura
	}
	
	private String read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		// leggo, e metto quello che leggo dentro buffer, e mi viene restituito il numero dei byte letti
		numRead = channel.read(buffer);

		// channel chiuso
		if (numRead == -1) {
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return "%%";
		}
	
			// leggo e stampo (esempi di lettura)
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);
			return new String(data);
			
			
	}
	private void write(SelectionKey key) {
		
	}

}

