/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.render;

import jayavery.geomastery.entities.FallingTreeBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Renderer for falling tree blocks. */
@SideOnly(Side.CLIENT)
public class RenderFallingTreeBlock extends Render<FallingTreeBlock> {

    public RenderFallingTreeBlock(RenderManager renderManager) {
        
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(FallingTreeBlock entity, double x, double y, double z,
            float entityYaw, float partialTicks) {

        if (entity.getBlockState() != null) {
            IBlockState state = entity.getBlockState();

            if (state.getRenderType() == EnumBlockRenderType.MODEL) {
                
                World world = entity.world;

                if (state != world.getBlockState(new BlockPos(entity))
                        && state.getRenderType() !=
                        EnumBlockRenderType.INVISIBLE) {
                    
                    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.disableLighting();
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder vertexbuffer = tessellator.getBuffer();

                    if (this.renderOutlines) {
                        
                        GlStateManager.enableColorMaterial();
                        GlStateManager.enableOutlineMode(this.getTeamColor(
                                entity));
                    }

                    vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
                    BlockPos blockpos = new BlockPos(entity.posX, entity.posY,
                            entity.posZ);
                    GlStateManager.translate((float) (x - blockpos
                            .getX() - 0.5D), (float) (y - blockpos.getY()),
                            (float) (z - blockpos.getZ() - 0.5D));
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft
                            .getMinecraft().getBlockRendererDispatcher();
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(
                            world, blockrendererdispatcher.getModelForState(
                                    state), state, blockpos,
                            vertexbuffer, false, MathHelper.getPositionRandom(
                                    entity.getPosition()));
                    tessellator.draw();

                    if (this.renderOutlines) {
                        GlStateManager.disableOutlineMode();
                        GlStateManager.disableColorMaterial();
                    }

                    GlStateManager.enableLighting();
                    GlStateManager.popMatrix();
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(FallingTreeBlock entity) {

        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
