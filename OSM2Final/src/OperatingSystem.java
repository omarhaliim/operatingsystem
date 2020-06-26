import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.Queue;


public class OperatingSystem {
	
	public static ArrayList<Thread> ProcessTable;
	public static	Queue<Process> ReadyQueue ;
	public static 	ArrayList<Process> BlockedQueue;
	public static ArrayList<SemaphoreI> Semaphores;

//	public static int activeProcess= 0;
	//system calls:
	// 1- Read from File
	@SuppressWarnings("unused")
	public static String readFile(String name) {
		String Data="";
		File file = new File(name);
	 try {
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine())
		{
			Data+= scan.nextLine()+"\n";
		}
		scan.close();
	} catch (FileNotFoundException e) {
		System.out.println(e.getMessage());
	}
		return Data;
	}
	
	// 2- Write into file
	@SuppressWarnings("unused")
	public static void writefile(String name, String data) {
		try
		{
			BufferedWriter BW = new BufferedWriter(new FileWriter(name));
			BW.write(data);
			BW.close();
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}

	}
	//3- print to console
	@SuppressWarnings("unused")
	public static void printText(String text) {

		System.out.println(text);
		
	}
	
	//4- take input
	
	@SuppressWarnings("unused")
	public static String TakeInput() {
		Scanner in= new Scanner(System.in);
		String data = in.nextLine();
		return data;
		
	}
	
	private static void createProcess(int processID, ArrayList<SemaphoreI> sems){
		Process p = new Process(processID,sems);
		ProcessTable.add(p);
		Process.setProcessState(p,ProcessState.Ready);
		//I wont make the process immediatly start running
		//p.start();
		
		//I'll add the process to a queue
		ReadyQueue.add(p);
	}
	
	public void scheduleAndDispatch() {
		ProcessTable = new ArrayList<Thread>();
   		ReadyQueue = new LinkedList<Process>();
   		BlockedQueue = new ArrayList<Process>();
   		Semaphores = new ArrayList<>();
   		SemaphoreI s1 = new SemaphoreI("Reading");
   		SemaphoreI s2 = new SemaphoreI("Writing");
   		SemaphoreI s3 = new SemaphoreI("Printing");
   		SemaphoreI s4 = new SemaphoreI("Input");
   		Semaphores.add(s1);
   		Semaphores.add(s2);
   		Semaphores.add(s3);
   		Semaphores.add(s4);

   		
		createProcess(1,Semaphores);
		createProcess(2,Semaphores);
		createProcess(3,Semaphores);
		createProcess(4,Semaphores);
		createProcess(5,Semaphores);
		
		while(!ReadyQueue.isEmpty()) {
			Process p = ReadyQueue.remove();
			if(p.status.equals(ProcessState.Ready)) {
				System.out.println("Process " + p.processID + " is now running");
				//TODO: check if it was never ran before el awel , if it was , p.resume
				p.run();
				
				
				
				if(p.status.equals(ProcessState.Waiting)) {
					BlockedQueue.add(p);
					ReadyQueue.remove(p);
				}
			}
			if(!BlockedQueue.isEmpty()) 
			for(int i = 0 ; i<BlockedQueue.size() ; i ++){
				Process pblocked = BlockedQueue.get(i);
				Boolean decision = pblocked.checkIfCanUnblock();
				if(decision) {
					ReadyQueue.add(pblocked);
					pblocked.status = ProcessState.Ready;
					BlockedQueue.remove(i);
					i--;
				}
			}
			
			
		}
	}
	
	public static void main(String[] args) {
   	
		OperatingSystem OS = new OperatingSystem();
		OS.scheduleAndDispatch();
		
		

	}
}



