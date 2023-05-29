package HarvardProcessor;

public class Processor {
	 byte[]  GeneralPurposeRegisters = new byte[64];
	 byte Pc=0;
	 int StatusRegister=0;
	 short instruction=0;
	 int opcode = 0;  // bits 15:12
	 int R1Address = 0;// bits 11:6     
	 int R2AddorImm = 0; //bits 5:0
	 byte R1value=0;
	 byte R2value=0;
	 boolean branched=false;
	 
	
	public void Execute(DataMemory dm) {
		StatusRegister=0;
		boolean carryFlag=false,overflowFlag=false,NegativeFlag=false,SignFlag=false,ZeroFlag=false;
		branched=false;
		
		if(opcode==0) {   //ADD
			byte number =(byte) (R1value+R2value);
			GeneralPurposeRegisters[R1Address]=number;
			carryFlag = (((R1value+R2value) >> 8) & 1) == 1;
			overflowFlag = ((R1value < 0 && R2value < 0 && number >= 0) || (R1value >= 0 && R2value >= 0 && number < 0));
			NegativeFlag = number<0;
			SignFlag = (overflowFlag==false && NegativeFlag==true)||(overflowFlag==true && NegativeFlag==false);
			ZeroFlag=number==0;
			
		}
		else if(opcode==1) {   //SUB
			byte number = (byte) (R1value-R2value);
			GeneralPurposeRegisters[R1Address]=number;
			overflowFlag = ((R1value < 0 && R2value < 0 && number >= 0) || (R1value >= 0 && R2value >= 0 && number < 0));
			NegativeFlag = number<0;
			SignFlag = (overflowFlag==false && NegativeFlag==true)||(overflowFlag==true && NegativeFlag==false);
			ZeroFlag=number==0;
			
		}
		else if (opcode==2) {   //MUL
			byte number = (byte) (R1value*R2value);
			GeneralPurposeRegisters[R1Address]=number;
			NegativeFlag = number<0;
			ZeroFlag=number==0;
			
		}
		else if(opcode==3) {   //MOVI
			GeneralPurposeRegisters[R1Address] =(byte) R2AddorImm; //MOVI
			
		}
		else if(opcode==4) {   //BEQZ
			if(GeneralPurposeRegisters[R1Address]==0) {
				Pc=(byte) (dm.readData(2047)-3);
				Pc=(byte) (Pc+(R2AddorImm+1));//BEQZ
				branched=true;
			}
			
		}
		else if(opcode==5) {   //ANDI
			byte number = (byte) (R1value&R2AddorImm);
			GeneralPurposeRegisters[R1Address]=number;
			NegativeFlag = number<0;
			ZeroFlag=number==0;
			
		}
		else if(opcode==6) {   //EOR
			byte number = (byte) (R1value^R2value);
			GeneralPurposeRegisters[R1Address]=number;
			NegativeFlag = number<0;
			ZeroFlag=number==0;
			
		}
		else if(opcode==7) {   //BR
			Pc=(byte) Integer.parseInt((Integer.toBinaryString(R1value)+Integer.toBinaryString(R2value)),2);
			branched=true;
			
		}
		else if(opcode==8) {   //SAL
			byte number = (byte) (R1value<<R2AddorImm);
			GeneralPurposeRegisters[R1Address]=number;
			NegativeFlag = number<0;
			ZeroFlag=number==0;
			
		}
		else if(opcode==9) {   //SAR
			byte number = (byte) (R1value>>R2AddorImm);
			GeneralPurposeRegisters[R1Address]=number;
			NegativeFlag = number<0;
			ZeroFlag=number==0;
			
		}
		else if(opcode==10) {   //LDR
			GeneralPurposeRegisters[R1Address] = (byte) dm.readData(R2AddorImm);
			
		}
		else if(opcode==11) {   //STR
			dm.writeData(R2AddorImm,R1value);
			
		}
		
		if(carryFlag) StatusRegister |= (1 << 4);
		if(overflowFlag) StatusRegister |= (1 << 3);
		if(NegativeFlag) StatusRegister |= (1 << 2);
		if(SignFlag) StatusRegister |= (1 << 1);else StatusRegister &= ~(1 << 1);
		if(ZeroFlag) StatusRegister |= (1 << 0);
    }
	
	
	
	public String type(int x) {
		if ((x>=0 && x<=2) || x==6 || x==7)return "R2";
		return "Immediate";
	}
}
	