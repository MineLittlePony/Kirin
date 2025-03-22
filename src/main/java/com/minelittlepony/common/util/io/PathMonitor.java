package com.minelittlepony.common.util.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent.Kind;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class PathMonitor implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Executor EXECUTOR = CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS);

    private final Object locker = new Object();

    private final Consumer<Event> callback;

    @Nullable
    private WatchKey key;

    private Set<Path> watchedPaths = Set.of();

    private boolean paused;

    private final Runnable pollTask = () -> {
        tick();
        EXECUTOR.execute(this.pollTask);
    };

    public PathMonitor(Consumer<Event> callback) {
        this.callback = callback;
        pollTask.run();
    }

    public void set(Path newFile) {
        set(newFile, List.of());
    }

    public void set(Path first, List<Path> rest) {
        synchronized (locker) {
            try {
                close();
                watchedPaths = new HashSet<>();
                watchedPaths.add(first);
                watchedPaths.addAll(rest);
                key = first.getParent().register(first.getParent().getFileSystem().newWatchService(),
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_CREATE
                );
            } catch (IOException e) {
                close();
                LOGGER.error(e);
            }
        }
    }

    public void wrap(Runnable action) {
        synchronized (locker) {
            try {
                paused = true;
                action.run();
            } finally {
                pollEvents();
                paused = false;
            }
        }
    }

    public List<WatchEvent<?>> pollEvents() {
        synchronized (locker) {
            if (key == null || !key.isValid()) {
                return List.of();
            }

            return key.pollEvents();
        }
    }

    public void tick() {
        synchronized (locker) {
            for (WatchEvent<?> ev : pollEvents()) {
                if (!paused && ev.context() instanceof Path p && (!watchedPaths.isEmpty() && watchedPaths.stream().anyMatch(path -> path.endsWith(p)))) {
                    Kind<?> kind = ev.kind();

                    if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
                        callback.accept(Event.DELETE);
                    } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind) || StandardWatchEventKinds.ENTRY_CREATE.equals(kind)) {
                        callback.accept(Event.MODIFY);
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        if (key != null) {
            pollEvents();
            key.cancel();
            key = null;
        }
        watchedPaths = Set.of();
    }

    public enum Event {
        MODIFY,
        DELETE
    }
}
