import java.util.ArrayList;

//import java.util.concurrent.Semaphore;


public class Process extends Thread {
	
	public int processID;
    ProcessState status=ProcessState.New;	
    Boolean ranOnceBefore;
    ArrayList<SemaphoreI> SemaphoreAccess;
    boolean awaitingWrite;
    boolean awaitingRead;
    boolean awaitingPrint;
    boolean awaitingInput;

	
	public Process(int m , ArrayList<SemaphoreI> sems) {
		processID = m;
		this.SemaphoreAccess = sems;
	}
	@Override
	public void run() {
		//check for the ready queue whose next
		
		
		switch(processID)
		{
		case 1:process1();break;
		case 2:process2();break;
		case 3:process3();break;
		case 4:process4();break;
		case 5:process5();break;
		}

	}
	
	
	public boolean SemReadWait() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Reading")) {
				if(s.available == true) {
					s.changeAvailability();
					return true;
				}
				else {
					return false;
				}		
			}
		}
		return false;
		}
	
	public void SemReadPost() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Reading"))
			s.changeAvailability();
		}
	}
	
	
	public boolean SemWriteWait() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Writing")) {
				if(s.available == true) {
					s.changeAvailability();
					return true;
				}
				else {
					return false;
				}		
			}
		}
		return false;
		
	}
	public void SemWritePost(){
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Writing"))
			s.changeAvailability();
		}
	}
	
	public boolean SemPrintWait() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Printing")) {
				if(s.available == true) {
					s.changeAvailability();
					return true;
				}
				else {
					return false;
				}		
			}
		}
		return false;
		
	}
	
	public void SemPrintPost() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Printing"))
			s.changeAvailability();
		}
	}
	
	public boolean SemInputWait()
	{
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Input")) {
				if(s.available == true) {
					s.changeAvailability();
					return true;
				}
				else {
					return false;
				}		
			}
		}
		return false;
	}
	
	public void SemInputPost() {
		for(int i = 0 ; i<SemaphoreAccess.size() ; i++) {
			SemaphoreI s = this.SemaphoreAccess.get(i);
			if(s.type.equals("Input"))
			s.changeAvailability();
		}
	}
	private void process1() {
		boolean proceed =this.SemReadWait();
		boolean proceed2 = this.SemPrintWait();
		boolean proceed3 = this.SemInputWait();
		// check for semaphore's availability
		if(proceed&&proceed2&&proceed3) {
		OperatingSystem.printText("Enter File Name: ");
		OperatingSystem.printText(OperatingSystem.readFile(OperatingSystem.TakeInput()));
		
		setProcessState(this,ProcessState.Terminated);
		this.SemReadPost();
		this.SemPrintPost();
		this.SemInputPost();
		}
		else {
			setProcessState(this, ProcessState.Waiting);
			this.awaitingRead=true;
			this.awaitingPrint=true;
			this.awaitingInput=true;
		}
		}
	
	private void process2() {
		boolean proceed = this.SemWriteWait();
		boolean proceed2 = this.SemPrintWait();
		boolean proceed3 = this.SemInputWait();

		if(proceed&&proceed2&&proceed3) {
		OperatingSystem.printText("Enter File Name: ");
		String filename= OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter Data: ");
		String data= OperatingSystem.TakeInput();
		OperatingSystem.writefile(filename,data);
		setProcessState(this,ProcessState.Terminated);
		this.SemWritePost();
		this.SemPrintPost();
		this.SemInputPost();
		}
		else {
			setProcessState(this, ProcessState.Waiting);
			this.awaitingInput=true;
			this.awaitingPrint=true;
			this.awaitingWrite=true;
		}
		
		
		}
	private void process3() {
		int x=0;
		boolean proceed = this.SemPrintWait();
		if(proceed) {
		while (x<301)
		{ 
			OperatingSystem.printText(x+"\n");
			x++;
		}
		setProcessState(this,ProcessState.Terminated);
		this.SemPrintPost();
		}
		else {
			setProcessState(this , ProcessState.Waiting);
			this.awaitingPrint=true;
		}
	}
	
	private void process4() {
	boolean proceed = this.SemPrintWait();
	
		int x=500;
		if(proceed) {
		while (x<1001)
		{
			OperatingSystem.printText(x+"\n");
			x++;
		}	
		setProcessState(this,ProcessState.Terminated);
		this.SemPrintPost();
		}
		else {
			setProcessState(this, ProcessState.Waiting);
			this.awaitingPrint=true;
		}
		}
	private void process5() {
		boolean proceed = this.SemPrintWait();
		boolean proceed2 = this.SemWriteWait();
		boolean proceed3 = this.SemInputWait();
		
		if(proceed&&proceed2&&proceed3) {
		OperatingSystem.printText("Enter LowerBound: ");
		String lower= OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter UpperBound: ");
		String upper= OperatingSystem.TakeInput();
		int lowernbr=Integer.parseInt(lower);
		int uppernbr=Integer.parseInt(upper);
		String data="";
		
		while (lowernbr<=uppernbr)
		{
			data+=lowernbr++ +"\n";
		}	
		OperatingSystem.writefile("P5.txt", data);
		setProcessState(this,ProcessState.Terminated);
		this.SemPrintPost();
		this.SemWritePost();
		this.SemInputPost();
		}
		else {
			setProcessState(this, ProcessState.Waiting);
			this.awaitingInput=true;
			this.awaitingPrint=true;
			this.awaitingWrite=true;
		}
	}
	
	 public static void setProcessState(Process p, ProcessState s) {
		 p.status=s;
		 if (s == ProcessState.Terminated)
		 {
			 OperatingSystem.ProcessTable.remove(OperatingSystem.ProcessTable.indexOf(p));
		 }
	}
	 
	 public boolean checkIfCanUnblock() {
		 boolean f1 = false;
		 boolean f2= false;
		 boolean f3 = false;
		 boolean f4 = false;
		 if(this.awaitingInput) {
			 for(int i = 0 ; i<this.SemaphoreAccess.size() ; i++) {
				 if(this.SemaphoreAccess.get(i).type.equals("Input")) {
					 f1 = this.SemaphoreAccess.get(i).available;
				 }
			 }
		 }
		 
		 if(this.awaitingPrint) {
			 for(int i = 0 ; i<this.SemaphoreAccess.size() ; i++) {
				 if(this.SemaphoreAccess.get(i).type.equals("Printing")) {
					 f2 = this.SemaphoreAccess.get(i).available;
				 }
			 }
		 }
		 
		 if(this.awaitingRead) {
			 for(int i = 0 ; i<this.SemaphoreAccess.size() ; i++) {
				 if(this.SemaphoreAccess.get(i).type.equals("Reading")) {
					 f3 = this.SemaphoreAccess.get(i).available;
				 }
			 }
		 }
		 
		 if(this.awaitingWrite) {
			 for(int i = 0 ; i<this.SemaphoreAccess.size() ; i++) {
				 if(this.SemaphoreAccess.get(i).type.equals("Writing")) {
					 f4 = this.SemaphoreAccess.get(i).available;
				 }
			 }
		 }
		 return f1&&f2&&f3&&f4;
		 
	 }
	 
	 public static ProcessState getProcessState(Process p) {
		 return p.status;
	}
}
