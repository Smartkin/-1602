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

    /**
     * @return pair - returns a URLDepthPair from the pending urls list
     */
    public synchronized URLDepthPair fetch(){
        //If pending list is found empty go sleep until more links are found
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

    /**
     *
     * @param pair - new url to add to crawling
     */
    public synchronized void add(URLDepthPair pair) {
        //Check whether this link was already checked or is in the pending list already
        if (scanned_urls.contains(pair) || pending_urls.contains(pair)) {
            return;
        }
        //Check for crawl depth
        if (pair.getDepth() >= Crawler.max_depth) {
            scanned_urls.add(pair);
            return;
        }
        //Add it to pending list and wake up a sleeping thread to start working again
        pending_urls.add(pair);
        notify();
    }

    /**
     *
     * @return - list of sites that were crawled through
     */
    public LinkedList<URLDepthPair> getSites(){
        return scanned_urls;
    }

    /**
     * Set the flag for threads to exit
     */
    public void exitThreads() {
        bKillThreads = true;
    }

    /**
     * Check for thread exit flag
     * @return - state of flag
     */
    public boolean isbKillThreads() {
        return bKillThreads;
    }
}
