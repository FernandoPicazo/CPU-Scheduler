import java.util.ArrayList;

public class OperatingSys {
	ArrayList<ProcessControlBlock> CPUq;
	ArrayList<ProcessControlBlock> IOq;
	ArrayList<ProcessControlBlock> done;
	int total;
	
	public OperatingSys(ArrayList<ProcessControlBlock> CPUq, ArrayList<ProcessControlBlock> IOq,ArrayList<ProcessControlBlock> done, int total){
		this.CPUq = CPUq;
		this.IOq = IOq;
		this.done = done;
		this.total = total;
	}
	
}
