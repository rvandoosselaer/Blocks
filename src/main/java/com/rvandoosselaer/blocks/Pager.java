package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An abstract implementation of a 3D pager. Based on the given center location ({@code setLocation()} the pages
 * around this center page in the grid are calculated. Each call to {@link #update()} will:
 * - detach one page that is outside the grid, if available
 * - attach one new page that is inside the grid, if available
 * - update a page inside the grid, if one is available.
 * Implementing classes need to implement the methods to create, attach and detach pages.
 * The boundaries of the grid can be set by setting {@code setLocation()} and {@code setLocation()}.
 *
 * @author rvandoosselaer
 */
@Slf4j
@RequiredArgsConstructor
public abstract class Pager<T> {

    @Getter
    @NonNull
    protected final ChunkManager chunkManager;

    @Getter
    protected final Map<Vec3i, T> attachedPages = new ConcurrentHashMap<>();
    protected final Queue<Vec3i> pagesToAttach = new LinkedList<>();
    protected final Queue<Vec3i> pagesToDetach = new LinkedList<>();
    protected final Queue<Vec3i> updatedPages = new ConcurrentLinkedQueue<>();
    protected final Set<Vec3i> requestedPages = ConcurrentHashMap.newKeySet();

    @Getter
    @Setter
    protected Vec3i gridSize;
    protected Vec3i centerPage;
    @Getter
    @Setter
    protected Vector3f location = new Vector3f();
    @Getter
    @Setter
    protected Vec3i gridLowerBounds = new Vec3i(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    @Getter
    @Setter
    protected Vec3i gridUpperBounds = new Vec3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    private ChunkManagerListener listener = new ChunkPagerListener();
    protected List<PagerListener<T>> pagerListeners = new CopyOnWriteArrayList<>();

    public void initialize() {
        if (log.isTraceEnabled()) {
            log.trace("{} initialize()", getClass().getSimpleName());
        }
        chunkManager.addListener(listener);
    }

    public void update() {
        if (location == null) {
            return;
        }

        updateCenterPage();

        detachNextPage();

        updatePages();

        attachNextPage();
    }

    public void cleanup() {
        if (log.isTraceEnabled()) {
            log.trace("{} cleanup()", getClass().getSimpleName());
        }
        attachedPages.forEach((loc, page) -> detachPage(page));
        attachedPages.clear();
        pagesToAttach.clear();
        pagesToDetach.clear();
        updatedPages.clear();
        requestedPages.clear();
        chunkManager.removeListener(listener);
        listener = null;
    }

    public void addListener(@NonNull PagerListener<T> listener) {
        pagerListeners.add(listener);
    }

    public void removeListener(@NonNull PagerListener<T> listener) {
        pagerListeners.remove(listener);
    }

    /**
     * Calculates and returns the pages (chunks) in the grid, based on the gridSize and centerPageLocation in the grid.
     *
     * @return the set of pages in the grid
     */
    protected Set<Vec3i> getPages() {
        if (centerPage == null || gridSize == null) {
            return Collections.emptySet();
        }

        int minX = Math.max(centerPage.x - ((gridSize.x - 1) / 2), gridLowerBounds.x);
        int maxX = Math.min(centerPage.x + ((gridSize.x - 1) / 2), gridUpperBounds.x);
        int minY = Math.max(centerPage.y - ((gridSize.y - 1) / 2), gridLowerBounds.y);
        int maxY = Math.min(centerPage.y + ((gridSize.y - 1) / 2), gridUpperBounds.y);
        int minZ = Math.max(centerPage.z - ((gridSize.z - 1) / 2), gridLowerBounds.z);
        int maxZ = Math.min(centerPage.z + ((gridSize.z - 1) / 2), gridUpperBounds.z);

        Set<Vec3i> pages = new HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pages.add(new Vec3i(x, y, z));
                }
            }
        }

        return pages;
    }

    /**
     * Updates the queues of the Pager. This should be called when the gridSize or centerPageLocation has changed.
     */
    protected void updateQueues() {
        Set<Vec3i> newPages = getPages();

        // clear the attach and detach queues
        pagesToDetach.clear();
        pagesToAttach.clear();

        // attach new pages in the grid
        for (Vec3i page : newPages) {
            // skip pages that are already attached
            if (!attachedPages.containsKey(page)) {
                pagesToAttach.offer(page);
            }
        }

        // detach pages outside of the grid
        for (Vec3i page : attachedPages.keySet()) {
            if (!newPages.contains(page)) {
                pagesToDetach.offer(page);
            }
        }

        // remove updated pages that are outside of the grid
        updatedPages.removeIf(page -> !newPages.contains(page));
    }

    /**
     * Creates and returns the page based on the passed chunk.
     *
     * @param chunk
     * @return page of the chunk or null
     */
    protected abstract T createPage(Chunk chunk);

    /**
     * Detach the page.
     *
     * @param page to detach
     */
    protected abstract void detachPage(T page);

    /**
     * Attach the page.
     *
     * @param page to attach
     */
    protected abstract void attachPage(T page);

    private void updateCenterPage() {
        Vec3i newCenterPage = ChunkManager.getChunkLocation(location);
        if (!Objects.equals(newCenterPage, centerPage)) {
            setCenterPage(newCenterPage);
            updateQueues();
        }
    }

    /**
     * Detach the next page in the pagesToDetach queue.
     */
    private void detachNextPage() {
        Vec3i pageLocation = pagesToDetach.poll();
        if (pageLocation == null) {
            return;
        }

        detachPageAtLocation(pageLocation);
    }

    private void detachPageAtLocation(Vec3i pageLocation) {
        T page = attachedPages.get(pageLocation);
        if (page == null) {
            log.warn("Trying to detach page at location {} that isn't attached.", pageLocation);
            return;
        }

        detachPage(page);

        attachedPages.remove(pageLocation);

        // the cache eviction mechanism of the ChunkCache isn't always evicting a chunk that isn't used. This can
        // cause problems when a chunk that is attached to the scenegraph is removed from the cache. By manually
        // evicting a chunk that is safely detached, we try to counter this behaviour.
        chunkManager.removeChunk(pageLocation);

        notifyListenersPageDetached(pageLocation, page);
    }

    private void notifyListenersPageDetached(Vec3i pageLocation, T page) {
        pagerListeners.forEach(listener -> listener.onPageDetached(pageLocation, page));
    }

    /**
     * Attach the next page in the pagesToAttach queue.
     */
    private void attachNextPage() {
        Vec3i pageLocation = pagesToAttach.poll();
        if (pageLocation == null) {
            return;
        }

        attachPageAtLocation(pageLocation);
    }

    private void attachPageAtLocation(Vec3i pageLocation) {
        Optional<Chunk> chunk = chunkManager.getChunk(pageLocation);
        if (!chunk.isPresent()) {
            requestPage(pageLocation);
            return;
        }

        T page = createPage(chunk.get());
        if (page == null) {
            // something went wrong creating the page, try again later
            pagesToAttach.offer(pageLocation);
            return;
        }

        attachPage(page);
        attachedPages.put(pageLocation, page);

        notifyListenersPageAttached(pageLocation, page);
    }

    private void notifyListenersPageAttached(Vec3i pageLocation, T page) {
        pagerListeners.forEach(listener -> listener.onPageAttached(pageLocation, page));
    }

    private void requestPage(Vec3i pageLocation) {
        // chunk was not found, we request it and try again later
        if (!requestedPages.contains(pageLocation)) {
            if (log.isTraceEnabled()) {
                log.trace("Requesting page " + pageLocation);
            }
            chunkManager.requestChunk(pageLocation);
            requestedPages.add(pageLocation);
        }
        pagesToAttach.offer(pageLocation);
    }

    /**
     * Update (detach and attach) the next page in the updatePages queue.
     */
    private void updatePages() {
        Vec3i pageLocation = updatedPages.poll();
        if (pageLocation == null) {
            return;
        }

        T oldPage = attachedPages.remove(pageLocation);
        if (oldPage == null) {
            log.warn("Trying to update page at location {} that isn't attached.", pageLocation);
            return;
        }

        // detach the old page
        detachPage(oldPage);

        // create and attach the new page
        Optional<Chunk> chunk = chunkManager.getChunk(pageLocation);
        if (!chunk.isPresent()) {
            log.warn("Request to update page at location {} but linked chunk {} was not found.", pageLocation, pageLocation);
            return;
        }

        T newPage = createPage(chunk.get());
        if (newPage == null) {
            log.warn("Unable to create new page at location {}", pageLocation);
            return;
        }

        attachPage(newPage);
        attachedPages.put(pageLocation, newPage);

        notifyListenersPageUpdated(pageLocation, oldPage, newPage);
    }

    private void notifyListenersPageUpdated(Vec3i pageLocation, T oldPage, T newPage) {
        pagerListeners.forEach(listener -> listener.onPageUpdated(pageLocation, oldPage, newPage));
    }

    private void setCenterPage(Vec3i centerPage) {
        if (log.isTraceEnabled()) {
            log.trace("New center page: {}", centerPage);
        }
        this.centerPage = centerPage;
    }

    private class ChunkPagerListener implements ChunkManagerListener {

        @Override
        public void onChunkUpdated(Chunk chunk) {
            if (attachedPages.containsKey(chunk.getLocation())) {
                updatedPages.offer(chunk.getLocation());
            }
        }

        @Override
        public void onChunkAvailable(Chunk chunk) {
            requestedPages.remove(chunk.getLocation());
        }

    }

}
