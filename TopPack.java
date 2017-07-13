import java.io.*;
import java.util.*;
import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class TopPack{
   
    class Data{
   //	private int user_ID;
   	private String Repository_Name;
   	private String Owner_Name;
   	private int Stars;
   	private int Fork;
   	private String Repo_ID;
    public Data(String Repository_Name,String Owner_Name,int Stars,int Fork,String Repo_ID){
          this.Repository_Name=Repository_Name;
          this.Owner_Name=Owner_Name;
          this.Stars=Stars;
          this.Fork=Fork;
          this.Repo_ID=Repo_ID;
    }
    public String getRepository_Name(){return Repository_Name;}
    public String getOwner_Name(){return Owner_Name;}
    public int getStars(){return Stars;}
    public int getFork(){return Fork;}
    public String getRepo_ID(){return Repo_ID;}
    public void setRepository_name(String Repository_Name){this.Repository_Name=Repository_Name;}
    public void setOwner_Name(String Owner_Name){this.Owner_Name=Owner_Name;}
    public void setStars(int Stars){this.Stars=Stars;}
    public void setFork(int Fork){this.Fork=Fork;}
    public void setRepo_ID(String Repo_ID){this.Repo_ID=Repo_ID;}
   } 
   
   	
   
   public static void Search(String key_word ){
   
    URL url;
    InputStream is = null;
    BufferedReader br;
    BufferedWriter bufferedWriter = null;
    String line;
    StringTokenizer st; 
    Data entry[]= new Data[300];
    int index=0,flag=0;
    String temp="";
    try {
        url = new URL("https://api.github.com/search/repositories?q="+key_word);
        is = url.openStream();  // throws an IOException
        br = new BufferedReader(new InputStreamReader(is));
        /*File myFile = new File("./Downloads/DataFile.txt");
        Writer writer = new FileWriter(myFile);
        bufferedWriter = new BufferedWriter(writer);
         
            // check if file exist, otherwise create the file before writing
        if (!myFile.exists()) {
                myFile.createNewFile();}*/
        while ((line = br.readLine()) != null) {
        	st = new StringTokenizer(line,",:\"");
        	while(st.hasMoreTokens())
        	{
        		temp=st.nextToken();
        		if(temp.equals("id")){
        			if(flag%2==0){System.out.println("id: "+st.nextToken());}
        			/*{
                      entry[index].setRepo_ID(st.nextToken());
                      System.out.println("id: "+ entry[index].getRepo_ID());
        			}*/
        			flag++;
        		}
        		else if(temp.equals("name")){
        			System.out.println("Repository_Name: "+st.nextToken());
        		}
        		else if(temp.equals("login")){
        			System.out.println("Owner_Name: "+st.nextToken());
        		}
        		else if(temp.equals("stargazers_count")){
        			System.out.println("Stars: "+st.nextToken());
        		}
        		else if(temp.equals("forks")){
        			System.out.println("Forks: "+st.nextToken());
        		}

            }//bufferedWriter.write(line);
        }
    } catch (MalformedURLException mue) {
         mue.printStackTrace();
    } catch (IOException ioe) {
         ioe.printStackTrace();
    } finally {
        try {
            if (is != null) is.close();
        } catch (IOException ioe) {
            // nothing to see here
     }
 }

}
 public static void main(String[] args) {
 	Scanner scan = new Scanner(System.in);
 	String key_word = scan.next();
 	Search(key_word);
 }
}

 

