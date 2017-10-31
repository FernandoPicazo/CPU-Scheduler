import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String args[]){
		if(args.length < 4 || args.length > 7){
			System.out.println("Usage: \n\t prog -alg [FIFO|SJF|PR|RR] [-quantum [integer(ms)]] -input [file name]");
		}
		
		
		ExecutorService Sim =  Executors.newCachedThreadPool();
		ArrayList<ProcessControlBlock> CPUq = new ArrayList<ProcessControlBlock>();
		ArrayList<ProcessControlBlock> IOq = new ArrayList<ProcessControlBlock>();
		ArrayList<ProcessControlBlock> done = new ArrayList<ProcessControlBlock>();
		
		String algo = null;
		String fname = null;
		int q = -1;
		int total = 0;
		int procCount =0;
		int CPUtime =0;
		int taTime=0; 
		int tWait=0;
		
		for(int i = 0; i<args.length; i++){
			switch(args[i]){
				case "-alg":
					algo = args[i+1];
					i++;
					break;
				case "-input":
					fname = args[i+1];
					i++;
					break;
				case "quantum":
					q = Integer.parseInt(args[i+1]);
					i++;
					break;
				default:
					System.out.println("Invalid arguments");
					System.exit(1);
			}
		}
		if(algo == null || fname == null || (algo.equals("RR") && q <1)){
			System.out.println("Flags are incorrect, please try again\n");
			System.out.println("Usage: \n\t prog -alg [FIFO|SJF|PR|RR] [-quantum [integer(ms)]] -input [file name]");
		}
		
		OperatingSys OS = new OperatingSys(CPUq, IOq, done, total);
		
		Sim.execute(new ReadFile(fname,OS));
		Sim.execute(new CPU(OS, algo, q));
		Sim.execute(new IO(OS));
		
		Sim.shutdown();
		
		try{
			Sim.awaitTermination(1, TimeUnit.MINUTES);
		}catch(InterruptedException e){
			
		}
		
		for(ProcessControlBlock buff: done){
			procCount++;
			CPUtime += buff.cpuTime;
			taTime += buff.tATime;
			tWait += buff.wTime;			
		}
		
		double util = CPUtime/taTime*100;
		double avgTurn = taTime/procCount*100;
		double tput = 1000/taTime;
		double avgWait = tWait/procCount;
		
		System.out.println("Input File Name    : " + fname);
		System.out.print("CPU Schedule Alg   : " + algo);
		if (algo.equals("RR"))
			System.out.println(" " + q);
		else
			System.out.println();
		System.out.println("CPU utilization    : " + util + "%");
		System.out.println("Throughput         : " + tput + " processes per second");
		System.out.println("Turnaround time    : " + avgTurn + " ms (average) " + taTime + " (total)");
		System.out.println("Waiting time       : " + avgWait + " ms (average) " + tWait + " (total)");
	}
	
}
