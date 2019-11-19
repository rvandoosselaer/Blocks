package com.rvandoosselaer.blocks.examples;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.TypeRegistry;

/**
 * @author: rvandoosselaer
 */
public class WireframeState extends BaseAppState implements ActionListener {

    private static final KeyTrigger TOGGLE_WIREFRAME_TRIGGER = new KeyTrigger(KeyInput.KEY_P);
    private static final String TOGGLE_WIREFRAME = "toggle-wireframe";

    private boolean isWireframe = false;

    @Override
    protected void initialize(Application app) {
        app.getInputManager().addMapping(TOGGLE_WIREFRAME, TOGGLE_WIREFRAME_TRIGGER);
        app.getInputManager().addListener(this, TOGGLE_WIREFRAME);
    }

    @Override
    protected void cleanup(Application app) {
        app.getInputManager().deleteMapping(TOGGLE_WIREFRAME);
        app.getInputManager().removeListener(this);
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (TOGGLE_WIREFRAME.equals(name) && !isPressed) {
            toggleWireframe();
        }
    }

    private void toggleWireframe() {
            TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
            typeRegistry.getAll().stream()
                    .map(typeRegistry::get)
                    .forEach(material -> material.getAdditionalRenderState().setWireframe(!isWireframe));
            isWireframe = !isWireframe;
    }

}
