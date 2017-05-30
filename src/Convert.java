import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
public class Convert {

	public static void main(String[] args) {
		int count = 0; 
		Scanner in = new Scanner(System.in);
		System.out.println("Enter location of playlists: ");
		String name = in.nextLine();
		File dir = new File(name);
		in.close();
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		     try {
				FileReader playlist = new FileReader(child);
				File fout = new File(child.getName().replaceAll("wpl", "m3u"));
				FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				Scanner input = new Scanner(playlist);
				bw.write("#EXTM3U");
				bw.newLine();
				while(input.hasNext()){
					if(input.nextLine().contains("<seq>")){
						boolean check = true;
						while(check){
							String hold = input.nextLine();
							if(hold.contains("</seq>")){
								check = false;
							}else{
								hold = hold.replaceAll("<media ", "").replaceAll(" />", "");
								String song[] = hold.split("\" ");
								bw.write("#EXTINF:");
								bw.write(Integer.toString((Integer.parseInt(getProperty(song,"duration"))/1000)));
								bw.write(",");
								bw.write(getProperty(song,"trackArtist"));
								bw.write(" - ");
								bw.write(getProperty(song,"trackTitle"));
								bw.newLine();
								bw.write(getProperty(song,"src"));
								bw.newLine();
							}
							
						}
					}
				}
				bw.close();
				input.close();
				count ++;
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }
		  System.out.printf("%d playlists were converted!",count);

	}

	public static String getProperty(String info[], String property){
		String propValue = "";
		for(int i = 0; i < info.length; i++){
			if(info[i].split("=\"")[0].contains(property)){
				propValue = info[i].split("=\"")[1].replaceAll("\"", "");
			}
		}
		return propValue;
	}
}
