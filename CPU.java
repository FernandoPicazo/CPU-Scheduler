
public class CPU implements Runnable{
	OperatingSys OS;	
	String algo;		//scheduling algorithm
	int q;				//quantum
	
	public CPU(OperatingSys OS, String algo, int q){
		this.OS = OS;
		this.algo = algo;
		this.q = q;
	}
	
	@Override
	public void run(){
		ProcessControlBlock process = null;
		
		
		while(true){
			if(!OS.CPUq.isEmpty()){
				switch( algo ){
				
				case "FIFO":
					
					synchronized (OS.CPUq){
						process = OS.CPUq.remove(0);
					}
					
					//burst(process);
					
					break;
					
				case "SJF": 
					synchronized(OS.CPUq){
						process = OS.CPUq.get(0);
						for(ProcessControlBlock buff: OS.CPUq){
							if(buff.CPUBurst.get(0) < process.CPUBurst.get(0)){
								process = buff;
							}
						}
						
						OS.CPUq.remove(process);
					}
					
					
				
				
				case "PR":
					synchronized(OS.CPUq){
						process = OS.CPUq.get(0);
						for(ProcessControlBlock buff: OS.CPUq){
							if(buff.priority > process.priority){
								process = buff;
							}
						}
						
						OS.CPUq.remove(process);
					}
					
					//burst(process);
					
					break;
					
				
				case "RR":
					synchronized(OS.CPUq){
						process = OS.CPUq.remove(0);
					}
					
					//rrburst(process);
					break;					
				}
			}
			if(OS.done.size() == OS.total){
				break;
			}
		}
	}
	
	
	
	
	
	void burst(ProcessControlBlock process){
		try{
			Thread.sleep(process.CPUBurst.get(0));
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		process.tATime += process.CPUBurst.get(0);
		process.cpuTime += process.CPUBurst.get(0);
		
		synchronized (OS.CPUq) {
			for (ProcessControlBlock buff : OS.CPUq){
				buff.wTime += process.CPUBurst.get(0);
				buff.tATime += process.CPUBurst.get(0);
			}
		}
		
		process.CPUBurst.remove(0);
		
		if(process.CPUBurst.isEmpty()){
			synchronized(OS.done){
				OS.done.add(process);
			}
		}else{
			synchronized (OS.IOq){
				OS.IOq.add(process);
			}
		}
	}
	
	void rrburst(ProcessControlBlock process){
		int btime;
		int obtime = process.CPUBurst.get(0);
		
		if(q < obtime){
			btime = q;
			process.CPUBurst.set(0, obtime - q);
		}else{
			btime = obtime;
		}
		
		try{
			Thread.sleep(btime);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		
		process.tATime += btime;
		process.cpuTime += btime;
		
		synchronized (OS.CPUq){
			for(ProcessControlBlock buff : OS.CPUq){
				buff.wTime += btime;
				buff.tATime += btime;
			}
		}
		
		if(btime == obtime){
			process.CPUBurst.remove(0);
		}
		
		if(process.CPUBurst.isEmpty()){
			synchronized (OS.done){
				OS.done.add(process);
			}
		}
		
		else if(btime == obtime ){
			synchronized(OS.IOq){
				OS.IOq.add(process);
			}
		}else{
			synchronized(OS.CPUq){
				OS.CPUq.add(process);
			}
		}		
	}
}
