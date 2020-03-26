import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class LinksPrinter {

    TreeMap<String, Integer> levels = new TreeMap<>();
    ArrayList <String> links = new ArrayList<>();

    public volatile File file = new File("links.txt");
    public PrintWriter writer;

    {
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void print(Set<String> linksSet) {

        links.addAll(linksSet);

        Collections.sort(links);

        getLevel(links);

        printMap (links);

        writer.flush();
        writer.close();

    }

    private void getLevel(ArrayList<String> links) {

        String previous = null;
        int level = 0;

        for (int i = 0; i < links.size(); i ++) {

            if (i == 0)
            { levels.put(links.get(i), level);
                previous = links.get(i);
                continue;
            }

            if (links.get(i).contains(previous))
            { level = level + 1;
                levels.put(links.get(i), level);
            }

            else {

                for (int j = (i - 2); j >=0; j--) {
                    if (links.get(i).contains(links.get(j)))
                    { level = levels.get(links.get(j)) + 1;
                        levels.put(links.get(i), level);
                        break;
                    }


                }

            }

            previous = links.get(i);

        }
    }

    private void printMap (ArrayList<String> links) {

        for (int i = 0; i < links.size(); i ++) {

            printSpace(levels.get(links.get(i)));
            System.out.println(links.get(i));
            writer.write(links.get(i) + "\n");
            writer.flush();

        }
    }

    private void printSpace(Integer level) {

        for (int s = 1; s <= level; s++) {

            System.out.print("\t");
            writer.write("\t");
            writer.flush();
        }


    }





}
























