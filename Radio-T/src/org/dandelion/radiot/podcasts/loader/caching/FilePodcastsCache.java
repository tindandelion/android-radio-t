package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;

import java.io.*;

public class FilePodcastsCache implements PodcastsCache {

    public interface Listener {
        void onUpdatedWith(PodcastList list);
    }

    // A threshold of one day
    private static final long LIFETIME_THRESHOLD = 24 * 3600 * 1000;

    private CacheFile cacheFile;
    private final int formatVersion;
    private Listener listener;

    public FilePodcastsCache(CacheFile cacheFile, int formatVersion) {
        this.cacheFile = cacheFile;
        this.formatVersion = formatVersion;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
        return new ObjectInputStream(cacheFile.openInputStream());
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
            invokeListenerWith(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeListenerWith(PodcastList data) {
        if (listener != null) {
            listener.onUpdatedWith(data);
        }
    }

    private void saveObjects(PodcastList data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(cacheFile.openOutputStream());
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
        long lifetime = cacheFile.age();
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
