  import java.util.List;
        
        /**
         * 负责抓取的线程。
         */
        public class CrawlThread implements Runnable{
        
            private List<String> proxyList;
            private String urlString;
            public CrawlThread(String url, List<String> proxyList) {
                this.proxyList =  proxyList;
                this.urlString = url;
            }
            
            @Override
            public void run() {
                for (String ip : proxyList) {
                    ProxyTest4Jsoup.visit(ip, urlString);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        
        }
