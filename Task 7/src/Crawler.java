import java.util.LinkedList;

public class Crawler {
    public static int max_depth;

    public static void main(String[] args){
        if (args.length < 3){
            System.out.println("Not enough arguments provided. URL, maximum crawl depth and thread number is required!");
            return;
        }
        //Set the max depth of crawling
        max_depth = Integer.parseInt(args[1]);
        //Set the amount of threads to use
        int threads = Integer.parseInt(args[2]);
        //Set the site to crawl through
        URLPool pool = new URLPool(args[0]);
        //Init thread array
        Thread[] thrds = new Thread[threads];
        //Start all the threads
        for(int i=0;i<threads;i++){
            CrawlerTask task = new CrawlerTask(pool);
            thrds[i] = new Thread(task,"Crawler "+i);
            thrds[i].start();
        }
        //Check for threads for their work being done
        while (pool.sleep_threads < threads) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        //Make threads quit their work since there is none left
        pool.exitThreads();
        for(int i=0;i<threads;i++){
            thrds[i].interrupt();
        }
        //Get all the links that threads generated and output them
        LinkedList<URLDepthPair> sites = pool.getSites();
        while(!sites.isEmpty()) {
            System.out.println(sites.removeFirst());
        }
    }
}
