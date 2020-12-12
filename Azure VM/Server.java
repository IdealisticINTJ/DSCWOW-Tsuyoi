import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
class Server
{
    public void exec() throws Exception
    {

        while(true)
        {
            //Format: <IP>/help?name=<name>&email=<email>&lat=<latitude>&long=<longitude>&end
            int port=6789;
            System.out.println("Server Reset: Waiting for connection "+port);
            ServerSocket ss=new ServerSocket(port);
            Socket s=ss.accept();
            System.out.println("Connected");
            BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            String str=br.readLine();
            System.out.println(str);
            str=str.substring(str.indexOf("help?")+"help?".length(),str.indexOf("&end"));
            System.out.println(str);
            System.out.println("Showing data");
            StringTokenizer st=new StringTokenizer(str,"=&");
            st.nextToken();
            String name=st.nextToken();
            st.nextToken();
            String em=st.nextToken();
            st.nextToken();
            String loc=st.nextToken();
            st.nextToken();
            loc+=","+st.nextToken();
            String url="https://google.com/maps?q="+loc;
            System.out.println(name);
            System.out.println(em);
            System.out.println(loc);
            System.out.println(url);
            System.out.println();
            sendEmail(name,em,url);
            saveDb(name,em,url);
            br.close();
            s.close();
            ss.close();
        }

    }

        void saveDb(String a, String b, String c)throws Exception
        {
                System.out.println("Saving to cloud database");
                String k="python dbupdate.py "+a+" "+b+" "+c;
                Runtime.getRuntime().exec(k);
        }
    void sendEmail(String name, String email, String loc) throws Exception
    {
        String url1="https://api.sendgrid.com/v3/mail/send";
        String data="{\"personalizations\": [{\"to\": [{\"email\": \""+email+"\"}]}],\"from\": {\"email\": \"adityaarghya0@gmail.com\"},\"subject\": \""+name+" needs help\",\"content\": [{\"type\": \"text/plain\", \"value\": \""+name+" needs urgent help at "+loc+"\"}]}";
        byte[] out=data.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(url1);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization","Bearer <SENDGRID AUTHTOKEN>");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
        String z="";
        while((z=br.readLine())!=null)
        {
            System.out.println(z);
        }
    }

    public static void main(String args[])
    {
        try
        {
            new Server().exec();
        }
        catch(Exception excep)
        {
            main(args);
        }
    }
}