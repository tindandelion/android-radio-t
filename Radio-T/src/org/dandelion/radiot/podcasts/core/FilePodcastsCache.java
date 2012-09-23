package org.dandelion.radiot.podcasts.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilePodcastsCache implements PodcastsCache {
    private File file;

    public FilePodcastsCache(File file) {
        this.file = file;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void reset() {
        file.delete();
    }

    @Override
    public List<PodcastItem> getData() {
        ArrayList<PodcastItem> list = new ArrayList<PodcastItem>();
        try {
            readObjectsInto(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void updateWith(List<PodcastItem> data) {
        try {
            saveObjects(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid() {
        return file.exists();
    }

    private void readObjectsInto(ArrayList<PodcastItem> list) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        try {
            readUntilEnd(in, list);
        } finally {
            in.close();
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "EmptyCatchBlock"})
    private void readUntilEnd(ObjectInputStream in, ArrayList<PodcastItem> list) throws ClassNotFoundException, IOException {
        try {
            for (; ; ) {
                list.add((PodcastItem) in.readObject());
            }
        } catch (EOFException e) {
        }
    }


    private void saveObjects(List<PodcastItem> data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        try {
            for (PodcastItem i : data) {
                out.writeObject(i);
            }
        } finally {
            out.close();
        }

    }
}
