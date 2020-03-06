package com.rvandoosselaer.blocks.examples;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import lombok.Getter;

/**
 * @author rvandoosselaer
 */
@Getter
public class PostProcessingState extends BaseAppState {

    private FilterPostProcessor filterPostProcessor;

    @Override
    protected void initialize(Application app) {
        filterPostProcessor = new FilterPostProcessor(app.getAssetManager());

        setSamples(app);

        setShadowFilter(app.getAssetManager());

        setSSAO();

        setFXAA(app);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        getApplication().getViewPort().addProcessor(filterPostProcessor);
    }

    @Override
    protected void onDisable() {
        getApplication().getViewPort().removeProcessor(filterPostProcessor);
    }

    private void setFXAA(Application app) {
        if (!isAntiAliasing(app)) {
            FXAAFilter fxaaFilter = new FXAAFilter();
            fxaaFilter.setEnabled(true);
            filterPostProcessor.addFilter(fxaaFilter);
        }
    }

    private void setSSAO() {
        SSAOFilter ssaoFilter = new SSAOFilter();
        ssaoFilter.setEnabled(false);
        filterPostProcessor.addFilter(ssaoFilter);
    }

    private void setShadowFilter(AssetManager assetManager) {
        DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(assetManager, 1024, 4);
        shadowFilter.setLight(getState(LightingState.class).getDirectionalLight());
        shadowFilter.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        shadowFilter.setEdgesThickness(2);
        shadowFilter.setShadowIntensity(0.75f);
        shadowFilter.setLambda(0.65f);
        shadowFilter.setShadowZExtend(75);
        shadowFilter.setEnabled(true);

        filterPostProcessor.addFilter(shadowFilter);
    }

    private void setSamples(Application app) {
        if (isAntiAliasing(app)) {
            filterPostProcessor.setNumSamples(getSamples(app));
        }
    }

    private boolean isAntiAliasing(Application app) {
        return getSamples(app) > 0;
    }

    private int getSamples(Application app) {
        return app.getContext().getSettings().getSamples();
    }
}
