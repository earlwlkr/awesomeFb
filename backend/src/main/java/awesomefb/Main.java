/**
 *
 */
package awesomefb;

/**
 * @author earl
 */
public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // Topics: samsung, iphone, coffee, galaxy
        FacebookFocusedCrawler crawler = new FacebookFocusedCrawler();
        crawler.run();
    }
}
