import java.util.LinkedList;

public class URLPool {
    private LinkedList<URLDepthPair> pending_urls;
    private LinkedList<URLDepthPair> scanned_urls;
    public int sleep_threads;
    private boolean bKillThreads = false;

    URLPool(String url){
        pending_urls = new LinkedList<>();
        pending_urls.add(new URLDepthPair(url,0));
        scanned_urls = new LinkedList<>();
        sleep_threads = 0;
    }

    public synchronized URLDepthPair fetch(){
        while(pending_urls.isEmpty()){
            try {
                sleep_threads++;
                wait();
                sleep_threads--;
            }
            catch (InterruptedException e) {
                if (bKillThreads)
                    return null;
            }
        }
        URLDepthPair pair = pending_urls.getFirst();
        scanned_urls.add(pair);
        pending_urls.removeFirst();
        return pair;
    }

    public synchronized void add(URLDepthPair pair) {
        if (scanned_urls.contains(pair) || pending_urls.contains(pair)) {
            return;
        }
        if (pair.getDepth() > Crawler.max_depth) {
            scanned_urls.add(pair);
            return;
        }
        pending_urls.add(pair);
        notify();
    }

    public LinkedList<URLDepthPair> getSites(){
        return scanned_urls;
    }

    public void exitThreads() {
        bKillThreads = true;
    }

    public boolean isbKillThreads() {
        return bKillThreads;
    }
}
