package HarvardProcessor;

import java.io.BufferedReader;
import java.io.FileReader;



public class InstructionMemory {

    short[] Instructions = new short[1024];
    
    
    
    public  void loadInstruction(String filePath) throws Exception {
    	boolean Itype,Rtype,shiftOPorAddOP;
    	String Inst;
    	@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		String[] content = new String[3];
		while (line != null) {
			Itype=false;Rtype=false;Inst="";shiftOPorAddOP=false;
			content = line.split(" ");
			if(content.length!=3) throw new Exception("Assembler error make sure to write the assembly in the correct way");
			switch(content[0]) {
			case"ADD" :Inst="0000";Rtype=true;break;
			case"SUB" :Inst="0001";Rtype=true;break;
			case"MUL" :Inst="0010";Rtype=true;break;
			case"MOVI":Inst="0011";Itype=true;break;
			case"BEQZ":Inst="0100";Itype=true;break;
			case"ANDI":Inst="0101";Itype=true;break;
			case"EOR" :Inst="0110";Rtype=true;break;
			case"BR"  :Inst="0111";Rtype=true;break;
			case"SAL" :Inst="1000";Itype=true;shiftOPorAddOP=true;break;
			case"SAR" :Inst="1001";Itype=true;shiftOPorAddOP=true;break;
			case"LDR" :Inst="1010";Itype=true;shiftOPorAddOP=true;break;
			case"STR" :Inst="1011";Itype=true;shiftOPorAddOP=true;break;
			default:throw new Exception("Invalid operation name");
			}
			for(int i=0;i<65;i++) {
				if(i==64)throw new Exception("Invalid register name");
				if(content[1].charAt(0)=='R' && i==Integer.parseInt(content[1].substring(1))){
					Inst+=String.format("%6s", Integer.toBinaryString(i)).replace(' ', '0');
					break;}}
			if(Rtype) {
				for(int i=0;i<65;i++) {
					if(i==64)throw new Exception("Invalid register name");
					if(content[2].charAt(0)=='R' && i==Integer.parseInt(content[2].substring(1))){
						Inst+=String.format("%6s", Integer.toBinaryString(i)).replace(' ', '0');
						break;}}}
			if(Itype) {
				if(shiftOPorAddOP) {
					if(Integer.parseInt(content[2])>63 || Integer.parseInt(content[2])<0)throw new Exception("The immediate value is incorrectly written.");
					Inst+=String.format("%6s", Integer.toBinaryString(Integer.parseInt(content[2]))).replace(' ', '0');
				}
				else {
					//if(Integer.parseInt(content[2])>31 || Integer.parseInt(content[2])<-32)throw new Exception("The Immediate is greater than signed 6 bits");
				    int number = Integer.parseInt(content[2]);
				    if (number >= 0) Inst+=String.format("%6s", Integer.toBinaryString(number)).replace(' ', '0');
				    else {
				    	Inst+=Integer.toBinaryString((number & 0b111111));}
				      
				        }
					}
				

			
			
			for(int i=0;i<Instructions.length;i++) {
				if(Instructions[i]==0) {Instructions[i]=(short) Integer.parseInt(Inst,2);break;}
			}
			line = br.readLine();
			
		}
}
    

    
    public int size() {
    	int size = 0;
    	for (int i = 0; i < Instructions.length; i++) {
    	    if (Instructions[i] != 0) {
    	        size++;
    	    }
    	    }
    	return size;
    }
    

}
