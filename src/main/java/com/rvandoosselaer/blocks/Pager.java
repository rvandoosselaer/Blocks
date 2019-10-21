package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A base implementation of a 3D pager. Based on the given center location ({@link #setLocation(Vector3f)} the pages
 * around this location in the grid are calculated. Each call to {@link #update()} will:
 * - detach one page that is outside the grid, if available
 * - attach one new page that is inside the grid, if available
 * - update a page inside the grid, if one is available.
 * Implementing classes need to implement the methods to create, attach and detach pages.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public abstract class Pager<T> implements MeshGenerationListener {

    @NonNull
    protected final BlocksManager blocksManager;

    protected final Map<Vec3i, T> attachedPages = new ConcurrentHashMap<>();
    protected final Queue<Vec3i> pagesToAttach = new LinkedList<>();
    protected final Queue<Vec3i> pagesToDetach = new LinkedList<>();
    protected final Queue<Vec3i> updatedPages = new ConcurrentLinkedQueue<>();

    protected Vec3i gridSize;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Vec3i centerPageLocation;
    protected Vector3f location;

    public void initialize() {
        if (log.isTraceEnabled()) {
            log.trace("{} initialize()", getClass().getSimpleName());
        }
        blocksManager.addMeshGenerationListener(this);
    }

    public void update() {
        if (location == null) {
            return;
        }

        Vec3i newCenterPage = BlocksManager.getChunkLocation(location);
        if (!Objects.equals(newCenterPage, centerPageLocation)) {
            log.debug("new center page: {}", newCenterPage);
            centerPageLocation = newCenterPage;

            updateQueues();
        }

        detachPages();

        updatePages();

        attachPages();
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
        blocksManager.removeMeshGenerationListener(this);
    }

    @Override
    public void generationFinished(Chunk chunk) {
        // we are only interested in updated pages in the grid
        if (attachedPages.containsKey(chunk.getLocation())) {
            updatedPages.offer(chunk.getLocation());
        }
    }

    /**
     * Calculates and returns the pages (chunks) in the grid, based on the gridSize and centerPageLocation in the grid.
     *
     * @return the set of pages in the grid
     */
    protected Set<Vec3i> getPages() {
        if (centerPageLocation == null || gridSize == null) {
            return Collections.emptySet();
        }

        int minX = centerPageLocation.x - ((gridSize.x - 1) / 2);
        int maxX = centerPageLocation.x + ((gridSize.x - 1) / 2);
        int minY = centerPageLocation.y - ((gridSize.y - 1) / 2);
        int maxY = centerPageLocation.y + ((gridSize.y - 1) / 2);
        int minZ = centerPageLocation.z - ((gridSize.z - 1) / 2);
        int maxZ = centerPageLocation.z + ((gridSize.z - 1) / 2);

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

    /**
     * Detach the next page in the pagesToDetach queue.
     */
    private void detachPages() {
        Vec3i pageLocation = pagesToDetach.poll();
        if (pageLocation == null) {
            return;
        }

        T page = attachedPages.remove(pageLocation);
        if (page == null) {
            log.warn("Trying to detach page at location {} that isn't attached.", pageLocation);
            return;
        }

        detachPage(page);
    }

    /**
     * Attach the next page in the pagesToAttach queue.
     */
    private void attachPages() {
        Vec3i pageLocation = pagesToAttach.poll();
        if (pageLocation == null) {
            return;
        }

        Chunk chunk = blocksManager.getChunk(pageLocation);
        if (chunk == null) {
            // chunk was not found, we request it and try again later
            //blocksManager.requestChunk(pageLocation);
            pagesToAttach.offer(pageLocation);
            return;
        }

        T page = createPage(chunk);
        if (page == null) {
            // something went wrong creating the page, try again later
            pagesToAttach.offer(pageLocation);
            return;
        }

        attachPage(page);
        attachedPages.put(pageLocation, page);
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
        Chunk chunk = blocksManager.getChunk(pageLocation);
        if (chunk == null) {
            log.warn("Request to update page at location {} but linked chunk {} was not found.", pageLocation, pageLocation);
            return;
        }

        T newPage = createPage(chunk);
        if (newPage == null) {
            log.warn("Unable to create new page at location {}", pageLocation);
            return;
        }

        attachPage(newPage);
        attachedPages.put(pageLocation, newPage);
    }

}
