package org.al.save;

import org.al.quadrisbase.MiniBoard;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class Saver implements Runnable {
    private Map<ImmutablePair<MiniBoard, Integer>, ImmutablePair<Integer, Integer>> toSave;
    private int numGames;

    public Saver(Map<ImmutablePair<MiniBoard, Integer>, ImmutablePair<Integer, Integer>> toSave, int numGames) {
        this.toSave = toSave;
        this.numGames = numGames;
    }

    @Override
    public void run() {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        PrintWriter writer = new PrintWriter("qMatrix.qmap", "UTF-8");

        writer.println("----------->" + numGames + "<-----------");

        int line = 0;

        for (ImmutablePair<MiniBoard, Integer> keyPair : toSave.keySet()) {
            ImmutablePair<Integer, Integer> valuePair = toSave.get(keyPair);
            writer.println(line++ + "a:^^^" + keyPair.getLeft().toSaveString() + "^^^:a b:^^^" + keyPair.getRight() + "^^^:b c:^^^" + valuePair.getLeft() + "^^^:c d:^^^" + valuePair.getRight() + "^^^:d");
        }

        writer.println();
        writer.println();
        writer.println("            *     ,MMM8&&&.            *\n" +
                "                  MMMM88&&&&&    .\n" +
                "                 MMMM88&&&&&&&\n" +
                "     *           MMM88&&&&&&&&\n" +
                "                 MMM88&&&&&&&&\n" +
                "                 'MMM88&&&&&&'\n" +
                "                   'MMM8&&&'      *    \n" +
                "          |\\___/|     /\\___/\\\n" +
                "          )     (     )    ~( .              '\n" +
                "         =\\     /=   =\\~    /=\n" +
                "           )===(       ) ~ (\n" +
                "          /     \\     /     \\\n" +
                "          |     |     ) ~   (\n" +
                "         /       \\   /     ~ \\\n" +
                "         \\       /   \\~     ~/\n" +
                "  jgs_/\\_/\\__  _/_/\\_/\\__~__/_/\\_/\\_/\\_/\\_/\\_\n" +
                "  |  |  |  |( (  |  |  | ))  |  |  |  |  |  |\n" +
                "  |  |  |  | ) ) |  |  |//|  |  |  |  |  |  |\n" +
                "  |  |  |  |(_(  |  |  (( |  |  |  |  |  |  |\n" +
                "  |  |  |  |  |  |  |  |\\)|  |  |  |  |  |  |\n" +
                "  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |");

        writer.close();
    }
}
