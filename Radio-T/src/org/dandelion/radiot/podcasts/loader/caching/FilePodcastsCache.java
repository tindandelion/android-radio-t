package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;

import java.io.*;

public class FilePodcastsCache implements PodcastsCache {
    // A threshold of one day
    private static final long LIFETIME_THRESHOLD = 24 * 3600 * 1000;

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
    public PodcastList getData() {
        try {
            return readItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PodcastList readItems() throws IOException, ClassNotFoundException {
        if (!hasData()) {
            return emptyList();
        }

        ObjectInputStream in = openInputStream();
        try {
            readVersionFrom(in);
            return readItemsUntilEnd(in);
        } finally {
            in.close();
        }
    }

    private PodcastList emptyList() {
        return new PodcastList();
    }

    private ObjectInputStream openInputStream() throws IOException {
        return new ObjectInputStream(new FileInputStream(file));
    }

    private int readVersionFrom(ObjectInputStream in) throws IOException {
        return in.readInt();
    }

    @SuppressWarnings({"InfiniteLoopStatement", "EmptyCatchBlock"})
    private PodcastList readItemsUntilEnd(ObjectInputStream in) throws ClassNotFoundException, IOException {
        PodcastList result = new PodcastList();
        try {
            for (; ; ) {
                result.add((PodcastItem) in.readObject());
            }
        } catch (EOFException e) {
        }
        return result;
    }

    @Override
    public void updateWith(PodcastList data) {
        try {
            saveObjects(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveObjects(PodcastList data) throws IOException {
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

    private void writeItemsInto(ObjectOutputStream out, PodcastList data) throws IOException {
        for (PodcastItem i : data) {
            out.writeObject(i);
        }
    }

    @Override
    public boolean hasValidData() {
        return hasData() && !hasExpired();
    }

    private boolean hasData() {
        try {
            return isValidVersion();
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean hasExpired() {
        long lifetime = System.currentTimeMillis() - file.lastModified();
        return lifetime >= LIFETIME_THRESHOLD;
    }

    private boolean isValidVersion() throws IOException {
        return readVersion() == formatVersion;
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
