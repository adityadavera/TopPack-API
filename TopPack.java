
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } 
    }
    
}
  
public class TopPack {
	 
	static JSONArray jsonarr = new JSONArray();
	static JSONArray jsonarr_repo=new JSONArray();
	static int index,choice;
    static InputStream is = null;
    static Map<String,Integer> packages = new HashMap<String,Integer>();
    
    static OkHttpClient client = new OkHttpClient();
 	public static String run(String url)throws IOException{
 		   Request request = new Request.Builder()
 		                                .url(url)
 		                                .build();
 		   try (Response response = client.newCall(request).execute()){
 			   return response.body().string();
 		   }
 	   }
  
   
 	 public static void Search(String key_word ){
		        
 			    
		        String inputStr = null;
				
					try {
						inputStr = run("https://api.github.com/search/repositories?q="+key_word);
						if(inputStr.contains("API rate Limit exceeded")){System.out.println("Error: API rate limit exceeded ");System.exit(0);}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
				
		        JSONParser parse = new JSONParser();
		        
		        JSONObject obj = null;
				try {
					obj = (JSONObject)parse.parse(inputStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        jsonarr = (JSONArray)obj.get("items");
		        if(choice==1){
		        for(int i=0;i<jsonarr.size();i++)
		        {
		        	JSONObject ob = (JSONObject)jsonarr.get(i);
		        	System.out.println("\nrepo_id: "+ob.get("id"));
		        	System.out.println("name: "+ob.get("name"));
		        	String s = (String)ob.get("full_name");
		        	for(index=0;index<s.length();index++){if(s.charAt(index)=='/')break;}
		        	s=s.substring(0,index);
		        	System.out.println("owner_name:" +s);
		        	System.out.println("forks:"+ob.get("forks"));
		        	System.out.println("stars: "+ob.get("stargazers_count"));
		        }
		        }
		}
		 public static void Import(String N){
	        String inputStr = null;
			try {
				inputStr = run("https://api.github.com/repositories/"+N);
				if(inputStr.contains("API rate Limit exceeded")){System.out.println("Error: API rate limit exceeded ");System.exit(0);}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
	        JSONParser parse = new JSONParser();
	        JSONObject obj = null;
			try {
				obj = (JSONObject)parse.parse(inputStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        String name,download_url;
	        name = (String)obj.get("full_name");
	        String input1 = null;
	        try {
				input1 = run("https://api.github.com/repos/"+name+"/contents");
				if(input1.contains("API rate Limit exceeded")){System.out.println("Error: API rate limit exceeded ");System.exit(0);}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
	        parse = new JSONParser();
	        Object xyz = null;
			try {
				xyz = parse.parse(input1);
			} catch (ParseException e2) {
				
				e2.printStackTrace();
			}
			jsonarr_repo = (JSONArray)xyz;
	        for(int i=0;i<jsonarr_repo.size();i++)
	        {
	         obj=(JSONObject)jsonarr_repo.get(i);
	         name=(String)obj.get("name");
	         if(name.equals("package.json"))
	         {
	        	 System.out.println("Yes");
	        	 download_url=(String)obj.get("download_url");
	        	 try {
					inputStr=run(download_url);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	 	         parse = new JSONParser();
	 	         JSONObject job = new JSONObject();
	 	         try {
					job = (JSONObject)parse.parse(inputStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
	 	         JSONObject dev = (JSONObject)job.get("dependencies");
	 	         JSONObject devDep = (JSONObject)job.get("devDependencies");
	 	         for (Object key : dev.keySet()) {
	 	           String keyStr = (String)key;
                   if(packages.containsKey(keyStr)){
                	   packages.replace(keyStr, packages.get(keyStr)+1);
                   }
                   else{
                	   packages.put(keyStr,1);
                   }
                   }
	 	         for (Object key : devDep.keySet()) {
	 	           String keyStr = (String)key;
	 	          if(packages.containsKey(keyStr)){
               	   packages.replace(keyStr, packages.get(keyStr)+1);
                  }
                  else{
               	   packages.put(keyStr,1);
                  }
	 	          }
	 	         if(choice==2){
	 	          System.out.println("Packages Used: ");
	 	          for(Object key : packages.keySet()) {
		 	           String keyStr = (String)key;
		 	          System.out.println(keyStr);
	 	        }}   
	 	          break;
	         }
	        }
		 }
	        
	       
		
		 public static void Toppack(String key_word){
			 Search(key_word);
			 for(int i=0;i<jsonarr.size();i++)
			 {
				 JSONObject ob = (JSONObject)jsonarr.get(i);
				 Import(ob.get("id").toString());
				 System.out.println(ob.get("id").toString());
			 }
			 
			 ValueComparator bvc = new ValueComparator(packages);
			 TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
			 sorted_map.putAll(packages);
			 int z=0;
			 for(Object key : sorted_map.keySet()) {
				 while(z<10){
				   z++;
	 	           String keyStr = (String)key;
	 	           int keyValue = sorted_map.get(keyStr);
	 	           System.out.println(keyStr+" : "+keyValue);
	 	          }
	       }
			 
	
	 }
		 public static void main(String[] args) {
			System.out.println("1.Search");
			System.out.println("2.Import");
			System.out.println("3.Toppack");
		 	Scanner scan = new Scanner(System.in);
		 	choice = scan.nextInt();
		 	if(choice==1){
		 		String key_word = scan.next();
		 	    Search(key_word);
		 	}
		 	else if(choice == 2){
		 		String id = scan.next();
		 		Import(id);
		 	}else if(choice ==3){
		 		String key_word = scan.next();
		 		Toppack(key_word);
		 		
		 	}
		 	scan.close();
		 	
		 }
}
