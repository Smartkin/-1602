import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Crawler extends Thread {
    private LinkedList<URLDepthPair> found_urls; //List of processed URLS
    private LinkedList<URLDepthPair> non_processed_urls; //List of pending to be processed URLs
    private final int webPort = 80; //Port to connect to
    private int max_depth; //Max crawling depth, passed as parameter
    private final String PROTO_SEARCH = "http"; //Protocol to search for in links
    //Indexing in url parts
    private final int PROTOCOL = 0;
    private final int HOSTNAME = 1;
    private final int RESOURCE = 2;

    private final String HREF = "href=\""; //HTML tag
    private final String redirect = "Location: "; //Detecting redirect

    /**
     * Program's entry point
     * @param args - Console arguments (At least 1 URL link and crawl depth is required)
     */
    public static void main(String[] args){
        if (args.length < 2){
            System.out.println("Not enough arguments provided. At least 1 URL link and maximum crawl depth is required!");
            return;
        }
        String[] links = args; //Store reference to args in links
        int depth = Integer.parseInt(args[args.length-1]); //Get the crawl depth
        String[] url_pool = new String[links.length-1]; //Generate the links to crawl through
        for(int i=0;i<links.length-1;i++){
            url_pool[i] = links[i]; //Copy links to the array
        }
        Crawler[] crawler = new Crawler[url_pool.length]; //Create the crawlers
        //Start crawling
        for(int i=0;i<crawler.length;i++){
            crawler[i] = new Crawler(url_pool[i],depth);
            crawler[i].start();
        }
    }

    /**
     * Create the crawler
     * @param url - URL to crawl through
     * @param depth - maximum crawling depth
     */
    Crawler(String url, int depth){
        found_urls = new LinkedList<>();
        non_processed_urls = new LinkedList<>();
        non_processed_urls.add(new URLDepthPair(url,0));
        max_depth = depth;
    }

    /**
     * Thread's task
     * Get list of sites and print them out
     */
    @Override
    public void run() {
        LinkedList<URLDepthPair> list = getSites();
        while(!list.isEmpty()) {
            System.out.println(list.removeFirst());
        }
    }

    /**
     * Crawls through every site until no sites to search for or reaching too big of a depth
     * @return list of crawled through sites
     */
    private LinkedList<URLDepthPair> getSites() {
        //Check for list emptiness and current crawl depth
        while(!non_processed_urls.isEmpty() && non_processed_urls.getFirst().getDepth() <= max_depth) {
            try {
                //Get the components of the URL link
                String[] url_components = non_processed_urls.getFirst().parse();
                //Check if the protocol the link has is the one we are searching for
                if (!url_components[PROTOCOL].equalsIgnoreCase(PROTO_SEARCH)) {
                    non_processed_urls.removeLast();
                    continue;
                }
                //Get the current depth of the crawl
                int cur_depth = non_processed_urls.getFirst().getDepth();
                //Create a socket connection
                Socket sock = new Socket(url_components[HOSTNAME], webPort);
                //Set the timeout to 10 seconds
                sock.setSoTimeout(10000);

                //Create the outputstream
                OutputStream os = sock.getOutputStream();
                //Get the printwriter from the os
                PrintWriter writer = new PrintWriter(os, true);
                //Create a request
                writer.println("GET "+url_components[RESOURCE]+" HTTP/1.1");
                writer.println("Host: "+url_components[HOSTNAME]);
                writer.println("Connection: close");
                writer.println();

                //Prepare the response reader
                InputStream is = sock.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                while(true){
                    //Read the line of the html document
                    String line = br.readLine();
                    //Exit the loop if document ended
                    if (line == null)
                        break;

                    //Check whether the read line contains a link tag or redirect link
                    if (line.contains(HREF) || line.contains(redirect)) {
                        //Check whether redirect was encountered
                        if (line.contains(redirect)){
                            //Skip whitespaces
                            int line_start = line.indexOf(redirect);
                            //Remove anything before the link to get it
                            line = line.substring(line_start + redirect.length());
                            String new_url = line;
                            //Create the pair and check if is contained in processed or to be processed lists
                            URLDepthPair new_pair = new URLDepthPair(new_url, cur_depth+1);
                            if (!found_urls.contains(new_pair) && !non_processed_urls.contains(new_pair)) {
                                non_processed_urls.add(new_pair);
                            }
                        }
                        //Keep reading this line in a loop in case there is more than one link tag
                        while(line.contains(HREF)) {
                            //Skip whitespaces
                            int line_start = line.indexOf(HREF);
                            //Set to the start of the link
                            line = line.substring(line_start + HREF.length());
                            String new_url;
                            //Use builder to speed up concat
                            StringBuilder builder = new StringBuilder();
                            while(true){
                                builder.append(line.charAt(0));
                                line = line.substring(1);
                                if (line.charAt(0) == '\"')
                                    break;
                            }
                            new_url = builder.toString();
                            //Create the pair and check if is contained in processed or to be processed lists
                            URLDepthPair new_pair = new URLDepthPair(new_url, cur_depth+1);
                            if (!found_urls.contains(new_pair) && !non_processed_urls.contains(new_pair)) {
                                non_processed_urls.add(new_pair);
                            }
                        }
                    }
                }
                //Add the searched link to the processed ones
                found_urls.add(non_processed_urls.getFirst());
                //Remove processed link from the unprocessed ones
                non_processed_urls.removeFirst();
                //Close the connection
                sock.close();
            }
            catch (IOException e) {
                //If we timed out or received an improper response move onto next URL
                System.out.println("Couldn't connect to the specified URL!");
                System.out.println("Connecting to next URL...");
                found_urls.add(non_processed_urls.getFirst());
                non_processed_urls.removeFirst();
            }
        }
        return found_urls;
    }
}
