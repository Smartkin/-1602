import java.io.*;
import java.net.Socket;

public class CrawlerTask implements Runnable {
    private final String HREF = "<a href=\"";
    private final int webPort = 80;
    private URLPool pool;
    private final int PROTOCOL = 0;
    private final int HOSTNAME = 1;
    private final int RESOURCE = 2;

    CrawlerTask(URLPool pool){
        this.pool = pool;
    }

    @Override
    public void run() {
        while(!pool.isbKillThreads()) {
            try {
                URLDepthPair pair = pool.fetch();
                String[] url_components = pair.parse();
                int cur_depth = pair.getDepth();
                Socket sock = new Socket(url_components[HOSTNAME], webPort);
                System.out.println("Attempting to connect to "+url_components[HOSTNAME]);
                sock.setSoTimeout(10000);

                OutputStream os = sock.getOutputStream();

                PrintWriter writer = new PrintWriter(os, true);

                writer.println("GET "+url_components[RESOURCE]+" HTTP/1.1");
                writer.println("Host: "+url_components[HOSTNAME]);
                writer.println("Connection: close");
                writer.println();

                InputStream is = sock.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                while(true){
                    String line = br.readLine();
                    if (line == null)
                        break;
                    if (line.contains(HREF)) {
                        while(line.contains(HREF)) {
                            int line_start = line.indexOf(HREF);
                            line = line.substring(line_start + HREF.length());
                            String new_url;
                            StringBuilder builder = new StringBuilder();
                            while (true) {
                                builder.append(line.charAt(0));
                                line = line.substring(1);
                                if (line.length() == 0 || line.charAt(0) == '\"')
                                    break;
                            }
                            new_url = builder.toString();
                            pool.add(new URLDepthPair(new_url, cur_depth+1));
                        }
                    }
                }
                sock.close();
            }
            catch (IOException e) {
                System.out.println("Couldn't connect to the specified URL!");
                System.out.println("Connecting to next URL...");
            }
            catch (NullPointerException e){
                break;
            }
        }
    }
}
