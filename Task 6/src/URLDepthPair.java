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
     * first componet is the protocol
     * second component is the hostname
     * third component is the path to the resource
     * @return components of the URL in string array
     */
    public String[] parse() {
        String[] components = new String[3];
        components[0] = "";
        components[1] = "";
        components[2] = "";
        String temp_url = URL;
        //Using string builder to speed up concat
        StringBuilder builder = new StringBuilder();
        while(true){
            builder.append(temp_url.charAt(0));
            temp_url = temp_url.substring(1);
            //If the string is empty reset it and set that there is no protocol
            if (temp_url.length() == 0)
            {
                components[0] = "";
                temp_url = URL;
                break;
            }
            if (temp_url.charAt(0) == ':')
                break;
        }
        components[0] = builder.toString();
        //If the remaining string length is less than 3 then that means there is no link
        if (temp_url.length() > 3) {
            temp_url = temp_url.substring(3);
            builder.delete(0, builder.toString().length());
            while (true) {
                builder.append(temp_url.charAt(0));
                temp_url = temp_url.substring(1);
                if (temp_url.length() == 0 || temp_url.charAt(0) == '/') {
                    break;
                }
            }
        }
        components[1] = builder.toString();
        components[2] = temp_url;
        return components;
    }
}
