package client;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class FileProperties {

	private Properties property;
	private String ip ;
	private int port;
	private String file;
	
	public FileProperties(String archivo) {
		property = new Properties();
		ip = "";
		port = 0;
		this.file = archivo;
	}
	
	public void read() {
		try {
			property.load(new FileInputStream(file));
			ip = property.getProperty("IP", "localhost");
			port = Integer.parseInt(property.getProperty("PUERTO", "10000"));
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void write(String ip, int puerto) {
		try {
			property.setProperty("IP", ip);
			property.setProperty("PUERTO", "" + puerto);
			
			property.store(new FileOutputStream(file), null);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPuerto() {
		return port;
	}
}
