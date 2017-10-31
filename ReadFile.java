import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadFile implements Runnable{
	BufferedReader in;
	OperatingSys OS;
	
	public ReadFile(String filein, OperatingSys OS){
		try{
			in = new BufferedReader(new FileReader(filein));
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		this.OS = OS;
	}
	//This makes it so that the read process will execute on a thread
	@Override
	public void run(){
		
		String line = "";
		ArrayList<String> tokenized;
		try{
			while((line = in.readLine()) != null){
				line = line.trim();
				tokenized = new ArrayList<String>(Arrays.asList(line.split("\\s")));
				
				switch(tokenized.get(0)){
				case "proc" :
					ProcessControlBlock block = new ProcessControlBlock(OS.total, tokenized);
					OS.total += 1;
					OS.CPUq.add(block);
					break;
				
				case "sleep" :
					Thread.sleep(Integer.parseInt(tokenized.get(1)));
					break;
				case "stop" :
					return;
				
				default :
					System.out.println("Reading error has occurred");
						
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(NumberFormatException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
