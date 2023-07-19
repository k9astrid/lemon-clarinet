package dev.lemon.api.bot.proxy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Scraper {
    protected final String[] urls = new String[] {
            "https://raw.githubusercontent.com/roosterkid/openproxylist/main/SOCKS5_RAW.txt",
            "https://raw.githubusercontent.com/ALIILAPRO/Proxy/main/socks4.txt",
            "https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks4.txt"
    };

    public int number;

    public final List<Proxy> proxies = new ArrayList<>();

    public void initialize() {
        this.proxies.clear();
        System.out.println("[ Scraper ] initializing.");

        try {
            (new Thread(() -> {
                try {
                    for (String map : this.urls) {
                        Document proxyList = Jsoup.connect(map).ignoreHttpErrors(true).get();
                        for (String proxy : proxyList.text().split(" ")) {
                            String[] proxySplit = proxy.split(":");

                            if (proxySplit.length >= 2) {
                                this.proxies.add(new Proxy(Proxy.ProxyType.SOCKS4, new InetSocketAddress(
                                        proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1])
                                )));
                            }
                        }
                    }
                } catch (Throwable throwable) { }
            })).run();

            System.out.println("[ Scraper ] added " + this.proxies.size() + " proxies.");
        } catch (Exception ignored) { }

    }
}
