package HarvardProcessor;

import java.util.Arrays;

public class HarvardArch {
	
	private static final String Program_File = "program.txt";
	
	 Processor p= new Processor();
	 InstructionMemory im = new InstructionMemory();
	 DataMemory dm =  new DataMemory();
	 int cycles=1;
	
	
	public void fetch() { 
		  p.instruction = (short) im.Instructions[p.Pc];
		  p.Pc++;
	}
	
	
	public void decode() {
	     p.opcode =(p.instruction >>> 12) & 0b1111;
	     p.R1Address =(p.instruction >>> 6) & 0b111111;
	     p.R2AddorImm=(p.instruction & 0b111111);
	     if(p.type(p.opcode).equals("Immediate") && p.opcode!= 8 && p.opcode!=9 && p.opcode!=10 && p.opcode!=11) {
	    	 if(((p.instruction >> 5) & 1) == 1) {
	    		int complement = (p.R2AddorImm | 0b11111111111111111111111111100000); // Sign extension to 32 bits
	 		    int result = -((~complement) + 1);
	    		 p.R2AddorImm=result;
	    	 }}
	     else p.R2AddorImm =(p.instruction) & 0b111111;
	     p.R1value=p.GeneralPurposeRegisters[p.R1Address];
		 if(p.type(p.opcode).equals("R2"))p.R2value=p.GeneralPurposeRegisters[p.R2AddorImm];
	     
	}
	
	
	public void execute() {
		 p.Execute(dm);
	}
	
	
	
	public String printFirst(byte[] gpr,byte[] da,String fetchInst, String decodeInst, String executeInst,String oldval,int oldPc,boolean branched) {
		
		String fetchInOut="",decodeInOut="",executeInOut="";
		String val;if(p.type(p.opcode).equals("Immediate"))val="="+p.R2AddorImm;else val=": R"+p.R2AddorImm+"="+p.R2value;
		if(fetchInst!="")fetchInOut="     Inputs the intruction in the memory with the the PC value: "+(oldPc-1)+ " & Outputs 16-bit binary";
		if(decodeInst!="") decodeInOut="     Inputs the 16-bit binary & Outputs opcode = "+p.opcode+" ("+getopcodetype(p.opcode)+"), R1: R"+p.R1Address+"="+p.R1value+", "+p.type(p.opcode)+val;
		if(executeInst!="")executeInOut="     "+oldval+" & Outputs the changes below:-";
		
		return "Clock Cycle " + cycles + ":-\n\n"
				+"(IF): "+ fetchInst +fetchInOut+"\n\n"
				+"(ID): "+ decodeInst +decodeInOut+"\n\n"
				+"(EX): "+ executeInst +executeInOut+"\n"
                + changes(p.GeneralPurposeRegisters,gpr,"General Purpose Registers") + "\n"
                + changes(dm.Data,da,"Data Memory") + "\n"
                +"Flags: Zero = "+ ((p.StatusRegister >>> 0) & 0b1)+", Sign = "+ ((p.StatusRegister >>> 1) & 0b1)+", Negative = "+ ((p.StatusRegister >>> 2) & 0b1)
                +", Overflow = "+ ((p.StatusRegister >>> 3) & 0b1)+", Carry = "+ ((p.StatusRegister >>> 4) & 0b1)+"\n"
                +"PC : "+(oldPc)+"\n"
                +"Branched : "+branched+"\n"
                + "--------------------------------- \n";
	}
	
	public String printSecond() {
		String[] binaryStrings = new String[im.Instructions.length];
		int numInstructions = im.size();
        for (int i = 0; numInstructions!=0; i++) {
        	binaryStrings[i] = String.format("%16s", Integer.toBinaryString(im.Instructions[i] & 0xFFFF)).replace(' ', '0');
        	numInstructions--;
        }
        
		return  "Data Memory: " + Arrays.toString(dm.Data) + "\n"
				+ "Instruction memory:  " + Arrays.toString(binaryStrings) + "\n"
				+"General Purpose Registers: " + Arrays.toString(p.GeneralPurposeRegisters) + "\n"
                +"Flags: Zero = "+ ((p.StatusRegister >> 0)&1)+", Sign = "+ ((p.StatusRegister >> 1)&1)+", Negative = "+ ((p.StatusRegister >> 0)&2)
                +", Overflow = "+ ((p.StatusRegister >> 0)&3)+", Carry = "+ ((p.StatusRegister >> 0)&4)+"\n"
                +"PC : "+(p.Pc)+"\n"
                + "---------------------------------";
	}
	
	
	
	public String changes(byte[] generalPurposeRegisters,byte[] gpr,String name) {
		String kind="";
		if (name=="General Purpose Registers")kind=" R";
		else kind=" Address ";
		StringBuilder result = new StringBuilder();
		result.append("The changes in the "+name+": " );
		boolean flag=true;

	    for (int i = 0; i < generalPurposeRegisters.length; i++) {
	        if (generalPurposeRegisters[i] != gpr[i]) {
	            result.append("In "+kind+i+" From ").append(gpr[i]).append(" --> ").append(generalPurposeRegisters[i]).append(", ");
	            flag=false;
	        }
	    }
	    
	    if(flag)return (result.append(" Null").toString());

	    if (result.length() > 0) {
	        result.delete(result.length() - 2, result.length());  // Remove the trailing comma and space
	    }

	    return result.toString();
	    
		
		
	}
	
	public String getopcodetype(int x) {
		String type="";
		switch (x){
		case 0:type="ADD";break;
		case 1:type="SUB";break;
		case 2:type="MUL";break;
		case 3:type="MOVI";break;
		case 4:type="BEQZ";break;
		case 5:type="ANDI";break;
		case 6:type="EOR";break;
		case 7:type="BR";break;
		case 8:type="SAL";break;
		case 9:type="SAR";break;
		case 10:type="LDR";break;
		case 11:type="STR";break;
		}
		return type;
	}
	
	public String ExecuteProgram() throws Exception {
		
		    String result="";
		    im.loadInstruction(Program_File);
	        int numInstructions = im.size();
	        int numCycles = 3 + ((numInstructions - 1) * 1);
	        boolean fetched=false,decoded=false;
	        byte[] da=new byte[2048], gpr=new byte[64];
	        String fetchInst="",decodeInst="",executeInst="",oldval="";
	        int count=0,printPc=0;byte OldPc=0,newPc=0;

	        for (int cycle = 1; cycle <= numCycles; cycle++) {
	            
	        	if(p.Pc==numInstructions && fetchInst.isEmpty() && decodeInst.isEmpty())break;
	        	
	        	if(cycle == 1) {
	            	  fetchInst="Instruction"+(p.Pc+1);
	            	  fetched=true;
	            	  fetch();
	              }
	              else {
	            	  OldPc=p.Pc;
	            	  if(decoded) {
	            		  da= Arrays.copyOf(dm.Data, dm.Data.length);
	            		  gpr = Arrays.copyOf(p.GeneralPurposeRegisters, p.GeneralPurposeRegisters.length);
	            		  executeInst=new String(decodeInst);
	            		  decodeInst="";
	            		  if(p.opcode==4)dm.writeData(2047, p.Pc);
	            		  execute();
	            		  decoded=false;
	            	  }
	            	  if(fetched && count!=1) {
	            		  decoded=true;
	            		  decodeInst=new String(fetchInst);
	            		  fetchInst="";
	            		  decode();
	            		  newPc=p.Pc;p.Pc=OldPc;
	            		  if(OldPc<numInstructions) {
	            			  fetchInst="Instruction"+(OldPc+1);
	            			  fetch();}
	            		  else fetched=false;
	            		  if(p.branched==true) {
	            			  count++;
	            			  fetched=false;
	            			  decoded=false;
	            			  p.Pc=newPc;
	            		  }
	            	  }
	            	 else if(count==1)
	            	 {
	            		 fetchInst="Instruction"+(p.Pc+1);
		            	  fetched=true;
		            	  fetch();
		            	  count=0;
		            	  p.branched=false;
		            	  decodeInst="";
		            	  executeInst="";
		            	  if(p.Pc>numInstructions)break;
	            	 }
	              }
	        	  if(p.branched)printPc=OldPc+1;else printPc=p.Pc;
	              result+=printFirst(gpr,da,fetchInst,decodeInst,executeInst,oldval,printPc,p.branched);
	              String val;if(p.type(p.opcode).equals("Immediate"))val="="+p.R2AddorImm;else val=": R"+p.R2AddorImm+"="+p.R2value;
	              oldval = "Inputs opcode = "+p.opcode+" ("+getopcodetype(p.opcode)+"), R1: R"+p.R1Address+"="+p.R1value+", "+p.type(p.opcode)+val;
	              cycles++;
	           }
	        result+=printSecond();
	        return result;
	}
	
	
	public static void main(String[]args) throws Exception {
		HarvardArch x = new HarvardArch();
		System.out.println(x.ExecuteProgram());
	}
}
