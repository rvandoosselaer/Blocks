package com.rvandoosselaer.blocks.examples;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.util.MemoryUtils;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

/**
 * @author rvandoosselaer
 */
public class MemoryDebugState extends BaseAppState {

    private static final int MB = 1024 * 1024;

    private Node node;
    private Container container;
    private Label heapLabel;
    private Label directLabel;

    @Override
    protected void initialize(Application app) {
        container = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Even));
        container.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 0.9f)));
        container.addChild(new Label("Memory", new ElementId(Container.ELEMENT_ID).child("title")));

        Container heap = container.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Last, FillMode.Even), new ElementId(Container.ELEMENT_ID).child("entry")));
        heap.addChild(new Label("heap: "));
        heapLabel = heap.addChild(new Label(getHeapString()));
        heapLabel.setTextHAlignment(HAlignment.Right);
        Container direct = container.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Last, FillMode.Even), new ElementId(Container.ELEMENT_ID).child("entry")));
        direct.addChild(new Label("direct: "));
        directLabel = direct.addChild(new Label(getDirectString()));
        directLabel.setTextHAlignment(HAlignment.Right);

        refreshLayout();
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        if (node == null) {
            node = ((SimpleApplication) getApplication()).getGuiNode();
        }

        node.attachChild(container);
    }

    @Override
    protected void onDisable() {
        container.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        heapLabel.setText(getHeapString());
        directLabel.setText(getDirectString());

        refreshLayout();
    }

    private String getDirectString() {
        return String.format("%d MB / %d objects", MemoryUtils.getDirectMemoryUsage() / MB, MemoryUtils.getDirectMemoryCount());
    }

    private String getHeapString() {
        return String.format("%d MB / %d MB", getUsedHeap() / MB, getTotalHeap() / MB);
    }

    private void refreshLayout() {
        container.setLocalTranslation(getApplication().getCamera().getWidth() - container.getPreferredSize().x, getApplication().getCamera().getHeight(), 1);
    }

    private long getTotalHeap() {
        return Runtime.getRuntime().totalMemory();
    }

    private long getUsedHeap() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

}
