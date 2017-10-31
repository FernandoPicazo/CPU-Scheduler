import java.util.ArrayList;

//This process will be used as a structure to contain information about a process

public class ProcessControlBlock{
	int id;
	ArrayList<Integer> CPUBurst;
	ArrayList<Integer> IOBurst;
	int priority;
	int cpuTime, ioTime, tATime, wTime;
	
	public ProcessControlBlock(int id, ArrayList<String> args){
		this.id = id;
		CPUBurst = new ArrayList<Integer>();
		IOBurst = new ArrayList<Integer>();
		priority = Integer.parseInt(args.get(1));
		
		for(int i = 3; i< args.size(); i++){
			if(i % 2 == 1){
				CPUBurst.add(Integer.parseInt(args.get(i)));
			}else{
				IOBurst.add(Integer.parseInt(args.get(i)));
			}
		}
		
	}
	

}
