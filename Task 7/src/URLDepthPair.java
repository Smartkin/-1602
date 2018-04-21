public class URLDepthPair {
    private String URL;
    private int depth;

    URLDepthPair(String url, int depth){
        this.URL = url;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object obj) {
        URLDepthPair comp_pair = (URLDepthPair) obj;
        return comp_pair.URL.equals(this.URL);
    }

    @Override
    public String toString() {
        return "URL: "+URL+" Depth: "+depth;
    }

    public int getDepth() {
        return depth;
    }

    public String[] parse() {
        String[] components = new String[3];
        components[0] = "";
        components[1] = "";
        components[2] = "";
        String temp_url = URL;
        StringBuilder builder = new StringBuilder();
        while(true){
            builder.append(temp_url.charAt(0));
            temp_url = temp_url.substring(1);
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
