
public class IO implements Runnable{
	
	OperatingSys OS;
	
	public IO(OperatingSys OS){
		this.OS = OS;
	}
	
	
	
	
	@Override
	public void run() {
		ProcessControlBlock process = null;
		while(OS.IOq.isEmpty()){
			try{
				Thread.sleep(1);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
		while(true){
			if(!OS.IOq.isEmpty()){
				
				synchronized (OS.IOq){
					process = OS.IOq.remove(0);
				}
				
				process.tATime += process.IOBurst.get(0);
				process.ioTime += process.IOBurst.get(0);
				
				synchronized(OS.IOq){
					for(ProcessControlBlock buf: OS.IOq){
						buf.wTime += process.IOBurst.get(0);
						buf.tATime += process.IOBurst.get(0);
					}
				}
				
				process.IOBurst.remove(0);
				
				synchronized(OS.CPUq){
					OS.CPUq.add(process);
				}
			}
			
			if (OS.done.size() == OS.total){
				break;
			}
		}
		
		
		
	}

}
