import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Main {

    public static Set<Thread> poolThreads = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Set<String> visitedLinks = ConcurrentHashMap.newKeySet();

        ConcurrentLinkedQueue<String> queueLinks = new ConcurrentLinkedQueue();

        queueLinks.add("https://skillbox.ru/");    //  http://sendel.ru/   https://skillbox.ru/ https://lenta.ru/

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

        while(queueLinks.size() > 0)
            {
            Task task = new Task(visitedLinks , queueLinks, executor.getPoolSize());
            executor.execute(task);

           if (System.currentTimeMillis() - start < 1000) {  // чтобы заполнились ссылки с корневой страницы 500 для http://sendel.ru/ 1000 skb

            try {
                Thread.sleep(3000);  // 700 http://sendel.ru/  3000 https://skillbox.ru/
            } catch (InterruptedException e) {
                e.printStackTrace();
            } }

           Thread.sleep(10);  // ошибка соединение если здесь не делать паузу

        }


        for (;;) {

         if (poolThreads.stream().allMatch(s -> s.getState().toString().equals("TIMED_WAITING") == true))

         {   executor.shutdownNow();
             break;
         }
        }

        LinksPrinter linksPrinter = new LinksPrinter();
        linksPrinter.print(visitedLinks);

        long stop = System.currentTimeMillis();

        System.out.println(stop-start + " ms " );






    }


}


