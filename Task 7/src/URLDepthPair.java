import java.net.MalformedURLException;

public class URLDepthPair {
    private String URL;
    private int depth;

    /**
     * Instancing the pair
     * @param url - URL to store
     * @param depth - Depth of the URL
     */
    URLDepthPair(String url, int depth){
        this.URL = url;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "URL: "+URL+" Depth: "+depth;
    }

    @Override
    public boolean equals(Object obj) {
        URLDepthPair comp_pair = (URLDepthPair) obj;
        return comp_pair.URL.equals(URL);
    }

    public int getDepth() {
        return depth;
    }

    /**
     * first component is the protocol
     * second component is the hostname
     * third component is the path to the resource
     * @return components of the URL in string array
     */
    public String[] parse() {
        String[] components = new String[3];
        components[0] = "";
        components[1] = "";
        components[2] = "";
        //Attempting to create the URL from the given string
        try {
            java.net.URL url = new java.net.URL(URL);
            //Get the needed components
            components[0] = url.getProtocol();
            components[1] = url.getHost();
            components[2] = url.getPath();
        }catch(MalformedURLException e){
            System.out.println("Error while parsing the URL, returned empty strings");
            return components;
        }
        return components;
    }
}
