package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * A listener that can be registered to the {@link Pager}. Use this to get notified when pages are attached, detached
 * or updated.
 *
 * @author: rvandoosselaer
 */
public interface PagerListener<T> {

    void onPageDetached(Vec3i location, T page);

    void onPageAttached(Vec3i location, T page);

    void onPageUpdated(Vec3i location, T oldPage, T newPage);

}
