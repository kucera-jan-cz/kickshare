package com.github.kickshare;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public class UserServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
    public static final String LAST_PAGE_ATTR = "data-last_page";
    public static final String PAGE_NUM_ATTR = "data-page_number";

    public static final String PROJECT_ANCHOR_XPATH = "descendant::div[@class=\"project-card-content\"]/descendant::a";
    public static final String CANCEL_PROJECT_IMAGE_ANCHOR_XPATH = "descendant::div[@class=\"project-thumbnail\"]/descendant::a";
    public static final String SUCCESSFUL_PROJECT_IMAGE_ANCHOR_XPATH = "descendant::div[@class=\"project-profile-feature-image\"]/descendant::a";
    public static final String PROJECT_IMAGE_ANCHOR_XPATH = MessageFormat.format("{0} | {1}", SUCCESSFUL_PROJECT_IMAGE_ANCHOR_XPATH, CANCEL_PROJECT_IMAGE_ANCHOR_XPATH);
    public static final String PROJECTS_XPATH = "//*[@id=\"list\"]/div/ul/li";

    @Test
    public void parseProfile() throws Exception{
//        InputStream html = new URL("http://www.kickstarter.com/profile/xatrix/backed.html").openStream();
        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/profile.html");
        TagNode tagNode = new HtmlCleaner().clean(html);
        DomSerializer serializer = new DomSerializer(new CleanerProperties());

        Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));


        Element element = (Element) dom4j.selectSingleNode(PROJECTS_XPATH);
        String lastPage = element.attributeValue(LAST_PAGE_ATTR);
        String pageNumber = element.attributeValue(PAGE_NUM_ATTR);
        LOGGER.info("Projects: {}", element.elements().size());
        for(Element campaignElem : (List<Element>) element.elements()) {
            Element anchor = (Element) campaignElem.selectSingleNode(PROJECT_ANCHOR_XPATH);
            String name = anchor.getText();

            Element imageAnchor = (Element) campaignElem.selectSingleNode(PROJECT_IMAGE_ANCHOR_XPATH);
            String imageUrl = imageAnchor.attributeValue("href");
            LOGGER.info("{}: {}", name, imageUrl);
        }
    }
}
