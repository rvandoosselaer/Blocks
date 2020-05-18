package com.rvandoosselaer.blocks.examples;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author rvandoosselaer
 */
@Getter
public class LightingState extends BaseAppState {

    private final ColorRGBA ambientLightColor = new ColorRGBA(0.25f, 0.25f, 0.25f, 1);
    private final ColorRGBA directionalLightColor = new ColorRGBA(1.5f, 1.5f, 1.5f, 1);
    private final Vector3f directionalLightDir = new Vector3f(-0.2f, -1, -0.2f).normalizeLocal();

    private Node node;
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    @Setter
    private boolean updateDirection;

    @Override
    protected void initialize(Application app) {
        app.getRenderManager().setPreferredLightMode(TechniqueDef.LightMode.SinglePass);
        app.getRenderManager().setSinglePassLightBatchSize(2);

        ambientLight = new AmbientLight(ambientLightColor);
        if (updateDirection) {
            setDirectionalLightDir(app.getCamera().getDirection());
        }
        directionalLight = new DirectionalLight(directionalLightDir, directionalLightColor);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        if (node == null) {
            node = ((SimpleApplication) getApplication()).getRootNode();
        }

        attachLights();
    }

    @Override
    protected void onDisable() {
        detachLights();
    }

    @Override
    public void update(float tpf) {
        if (updateDirection) {
            setDirectionalLightDir(getApplication().getCamera().getDirection());
        }
    }

    public void setAmbientLightColor(@NonNull ColorRGBA color) {
        this.ambientLightColor.set(color);
    }

    public void setDirectionalLightColor(@NonNull ColorRGBA color) {
        this.directionalLightColor.set(color);
    }

    public void setDirectionalLightDir(@NonNull Vector3f direction) {
        this.directionalLightDir.set(direction).normalizeLocal();
        refreshDirectionalLight();
    }

    public void setNode(@NonNull Node node) {
        if (this.node != null) {
            detachLights();
        }
        this.node = node;
        attachLights();
    }

    protected void attachLights() {
        node.addLight(ambientLight);
        node.addLight(directionalLight);
    }

    protected void detachLights() {
        node.removeLight(ambientLight);
        node.removeLight(directionalLight);
    }

    protected void refreshDirectionalLight() {
        if (directionalLight != null) {
            directionalLight.setDirection(directionalLightDir);
        }
    }

}
