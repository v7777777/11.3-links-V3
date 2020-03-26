import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Task implements Runnable {

    Set<String> visitedLinks;
    ConcurrentLinkedQueue<String> queueLinks;
    int threadsNum;

    public Task(Set<String> visitedLinks, ConcurrentLinkedQueue<String> queueLinks,  int threadsNum) {
        this.visitedLinks = visitedLinks;
        this.queueLinks = queueLinks;
        this.threadsNum = threadsNum;
    }

    @Override
     public void run() {

        if (Main.poolThreads.size() < threadsNum)
        { Main.poolThreads.add(Thread.currentThread());}

        String currentLink = queueLinks.poll();

         if (currentLink  == null) {

             while (currentLink == null)
                 {
                     try {
                         Thread.sleep(500);
                     } catch (Exception e) {
                         System.out.println(Thread.currentThread().getName() + " sleep interrupted");
                         return;
                     }
                     currentLink = queueLinks.poll();
                 }
         }


         visitedLinks.add(currentLink);

         Document doc = null;

             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 System.out.println(Thread.currentThread().getName() + " sleep interrupted");
             }

             try {
                 doc = Jsoup.connect(currentLink).ignoreHttpErrors(true).timeout(0).get();
             } catch (Exception e) {
                 System.out.println(currentLink + " connection error");;
             }

             Elements links = doc.select("a[href]");

             for (Element link : links) {

                 String stringLink = link.attr("abs:href");

                 if (stringLink.contains("https://skillbox.ru/") ) {  //  http://sendel.ru/   https://skillbox.ru/ https://lenta.ru/

                     if (queueLinks.contains(stringLink)) {continue;}

                     if (visitedLinks.contains(stringLink)) {continue;}

                     if (stringLink.contains("#") || stringLink.contains(".jpg") || stringLink.contains(".jpeg") ||  stringLink.contains(".png") || stringLink.contains(".pdf"))
                     { continue;}

                     queueLinks.add(stringLink);


                 }
             }

         }




     }
















