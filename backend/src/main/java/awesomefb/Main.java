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
        if (args.length < 1) {
            System.out.println("USAGE: java -jar [jar-name] [topic]");
            return;
        }
        // Topics: samsung, iphone, coffee, galaxy
        FacebookFocusedCrawler crawler = new FacebookFocusedCrawler();
        crawler.run(args[0]);
    }
}
