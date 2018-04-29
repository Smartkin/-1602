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

    /**
     * Main method where all the threads are performing the crawling task.
     * Links are being fetched until pool says to stop or until they get
     * nothing (null) from the fetch method
     */
    @Override
    public void run() {
        while(!pool.isbKillThreads()) {
            try {
                //Get the pending link
                URLDepthPair pair = pool.fetch();
                //If no actual link was given that means we can stop working
                if (pair == null) break;
                //Get the url_components
                String[] url_components = pair.parse();
                //Get the current depth
                int cur_depth = pair.getDepth();
                //Connect to the site and start getting data
                Socket sock = new Socket(url_components[HOSTNAME], webPort);
                System.out.println("Attempting to connect to "+url_components[HOSTNAME]);
                //Set the timeout (10 seconds)
                sock.setSoTimeout(10000);

                OutputStream os = sock.getOutputStream();

                PrintWriter writer = new PrintWriter(os, true);
                //Send the request
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
                        //Read the line for every link
                        while(line.contains(HREF)) {
                            //Get rid of whitespaces and get the entirety of the link
                            int line_start = line.indexOf(HREF);
                            line = line.substring(line_start + HREF.length());
                            line = line.substring(0,line.indexOf("\""));
                            //Add the link to the pool
                            pool.add(new URLDepthPair(line, cur_depth+1));
                        }
                    }
                }
                sock.close();
            }
            catch (IOException e) {
                System.out.println("Couldn't connect to the specified URL!");
                System.out.println("Connecting to next URL...");
            }
        }
    }
}
