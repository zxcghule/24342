package com.maxim.bodymarket.client;

import com.maxim.bodymarket.MaximEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MaximRenderer extends MobEntityRenderer<MaximEntity, PlayerEntityModel<MaximEntity>> {
    private static final Identifier TEXTURE = new Identifier("bodymarket", "textures/entity/maxim.png");

    public MaximRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.7f);
    }

    @Override
    public Identifier getTexture(MaximEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(MaximEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.35f, 1.3f, 1.35f); // высокий и широкий качок
    }
}
