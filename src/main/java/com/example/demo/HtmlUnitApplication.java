package com.example.demo;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class HtmlUnitApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HtmlUnitApplication.class, args);
		homePage_Firefox();
	}

	public static void homePage_Firefox() throws Exception {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		final HtmlPage page = webClient.getPage("https://sgpokemap.com/");
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		while(true) {
            try {
                //get div which has a 'name' attribute of 'John'
                final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@id='map']").get(0);
                Iterable<DomElement> domIterator = div.getChildElements();
                DomElement firstDom = domIterator.iterator().next();
                Map<String, DomAttr> map = firstDom.getAttributesMap();
                for (Map.Entry<String, DomAttr> entry : map.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + "\nValue : " + entry.getValue().asXml() + "\n");
                }
                Iterator<DomElement> childIterator = firstDom.getChildElements().iterator();
                DomElement target = null;
                boolean found = false;
                while (childIterator.hasNext()) {
                    target = childIterator.next();
                    Map<String, DomAttr> childMap = target.getAttributesMap();
                    for (Map.Entry<String, DomAttr> entry : childMap.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
                        if (entry.getValue().getValue().trim().equals("leaflet-pane leaflet-marker-pane".trim())) {
                            found = true;
                        }
                    }
                    if (found) break;
                }

                System.out.println("Found Element : " + target.getChildElementCount());

                Iterator<DomElement> domElements = target.getChildElements().iterator();
                while(domElements.hasNext()) {
                    DomElement domElement = domElements.next();
                    Map<String, DomAttr> childMap = domElement.getAttributesMap();
                    /*for (Map.Entry<String, DomAttr> entry : childMap.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
                    }
                    System.out.println();
                    */
                    Iterator<DomElement> pokemonList = domElement.getChildElements().iterator();
                    while(pokemonList.hasNext()) {
                        DomElement pokemon = pokemonList.next();
                        Iterator<DomElement> poke = pokemon.getChildElements().iterator();
                        while(poke.hasNext()) {
                            DomElement p = poke.next();
                            Map<String, DomAttr> pokeMap = p.getAttributesMap();
                            for (Map.Entry<String, DomAttr> entry : pokeMap.entrySet()) {
                                System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
                            }
                        }
                    }
                    System.out.println();
                }

                sleep(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
                sleep(5000);
            }
		}
	}
}
