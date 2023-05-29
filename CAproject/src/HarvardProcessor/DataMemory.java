package HarvardProcessor;

public class DataMemory {
     byte[] Data = new byte[2048];
    
    public int readData(int address) {
		return Data[address];
    	
    }
    public void writeData(int address,byte value) {
		Data[address]=value;
    	
    }
}
