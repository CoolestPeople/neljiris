package org.al.save;

import org.al.quadrisbase.MiniBoard;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class Saver implements Runnable {
    Map<ImmutablePair<MiniBoard, Integer>, ImmutablePair<Integer, Integer>> toSave;

    public Saver(Map<ImmutablePair<MiniBoard, Integer>, ImmutablePair<Integer, Integer>> toSave) {
        this.toSave = toSave;
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

        int line = 0;

        for (ImmutablePair<MiniBoard, Integer> keyPair : toSave.keySet()) {
            ImmutablePair<Integer, Integer> valuePair = toSave.get(keyPair);
            writer.println(line++ + "a:^^^" + keyPair.getLeft().toSaveString() + "^^^:a~~~~~b:^^^" + keyPair.getRight() + "^^^:b~~~~~c:^^^" + valuePair.getLeft() + "^^^:c~~~~~d:^^^" + valuePair.getRight() + "^^^:d");
        }

        writer.close();
    }
}
