package org.dandelion.radiot.podcasts.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilePodcastsCache implements PodcastsCache {
    private final File file;
    private final int formatVersion;

    public FilePodcastsCache(File file, int formatVersion) {
        this.file = file;
        this.formatVersion = formatVersion;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void reset() {
        file.delete();
    }

    @Override
    public List<PodcastItem> getData() {
        try {
            return readItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<PodcastItem> readItems() throws IOException, ClassNotFoundException {
        ObjectInputStream in = openInputStream();
        try {
            readVersionFrom(in);
            return readItemsUntilEnd(in);
        } finally {
            in.close();
        }
    }

    private ObjectInputStream openInputStream() throws IOException {
        return new ObjectInputStream(new FileInputStream(file));
    }

    private int readVersionFrom(ObjectInputStream in) throws IOException {
        return in.readInt();
    }

    @SuppressWarnings({"InfiniteLoopStatement", "EmptyCatchBlock"})
    private ArrayList<PodcastItem> readItemsUntilEnd(ObjectInputStream in) throws ClassNotFoundException, IOException {
        ArrayList<PodcastItem> result = new ArrayList<PodcastItem>();
        try {
            for (; ; ) {
                result.add((PodcastItem) in.readObject());
            }
        } catch (EOFException e) {
        }
        return result;
    }

    @Override
    public void updateWith(List<PodcastItem> data) {
        try {
            saveObjects(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveObjects(List<PodcastItem> data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        try {
            writeVersionInto(out);
            writeItemsInto(out, data);
        } finally {
            out.close();
        }

    }

    private void writeVersionInto(ObjectOutputStream out) throws IOException {
        out.writeInt(formatVersion);
    }

    private void writeItemsInto(ObjectOutputStream out, List<PodcastItem> data) throws IOException {
        for (PodcastItem i : data) {
            out.writeObject(i);
        }
    }

    @Override
    public boolean isValid() {
        try {
            return readVersion() == formatVersion;
        } catch (Exception ex) {
            return false;
        }
    }

    private int readVersion() throws IOException {
        ObjectInputStream in = openInputStream();
        try {
            return readVersionFrom(in);
        } finally {
            in.close();
        }
    }

}