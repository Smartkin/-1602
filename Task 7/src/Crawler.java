import java.util.LinkedList;

public class Crawler {
    public static int max_depth;

    public static void main(String[] args){
        if (args.length < 3){
            System.out.println("Not enough arguments provided. URL, maximum crawl depth and thread number is required!");
            return;
        }
        max_depth = Integer.parseInt(args[1]);
        int threads = Integer.parseInt(args[2]);
        URLPool pool = new URLPool(args[0]);
        Thread[] thrds = new Thread[threads];
        for(int i=0;i<threads;i++){
            CrawlerTask task = new CrawlerTask(pool);
            thrds[i] = new Thread(task,"Crawler "+i);
            thrds[i].start();
        }
        while (pool.sleep_threads < threads) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        pool.exitThreads();
        for(int i=0;i<threads;i++){
            thrds[i].interrupt();
        }
        LinkedList<URLDepthPair> sites = pool.getSites();
        while(!sites.isEmpty()) {
            System.out.println(sites.removeFirst());
        }
    }
}
