/*
 *
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.common.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;
import tech.flatstone.appliedlogistics.AppliedLogisticsCreativeTabs;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.common.blocks.BlockTileBase;
import tech.flatstone.appliedlogistics.common.blocks.Blocks;
import tech.flatstone.appliedlogistics.common.tileentities.misc.TileEntityCrank;
import tech.flatstone.appliedlogistics.common.util.*;

import java.util.List;

public class BlockCrank extends BlockTileBase implements IProvideRecipe, IBlockRenderer, IProvideEvent {
    public BlockCrank() {
        super(Material.wood, "misc/crank");
        this.setTileEntity(TileEntityCrank.class);
        this.setHarvestLevel("Axe", 0);
        this.setCreativeTab(AppliedLogisticsCreativeTabs.tabGeneral);
        this.setInternalName("misc_crank");
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(7 / 16f, 0, 7 / 16f, 9 / 16f, .75f, 9 / 16f);
    }

    @Override
    public void RegisterRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this),
                "sss",
                " s ",
                " s ",
                's', "stickWood"
        ));
    }

    @Override
    public void registerBlockItemRenderer() {
        final String resourcePath = String.format("%s:%s", ModInfo.MOD_ID, this.resourcePath);

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(resourcePath, "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //todo: check not fake player
        //todo: check that there is work to do

        TileEntityCrank tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityCrank.class);
        if (tileEntity == null)
            return true;

        tileEntity.doCrank();
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        TileEntity tileEntity = TileHelper.getTileEntity(worldIn, pos.down(), TileEntity.class);
        if (tileEntity == null)
            return false;

        if (!(tileEntity instanceof ICrankable))
            return false;

        return ((ICrankable) tileEntity).canAttachCrank();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity tileEntity = TileHelper.getTileEntity(worldIn, pos.down(), TileEntity.class);
        if (tileEntity == null)
            breakCrank(worldIn, pos, true);

        if (!(tileEntity instanceof ICrankable))
            breakCrank(worldIn, pos, true);
    }

    public void breakCrank(World worldIn, BlockPos pos, boolean dropItem) {
        if (worldIn.isRemote)
            return;

        worldIn.destroyBlock(pos, dropItem);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        TileEntityCrank tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityCrank.class);
        EnumFacing crankRotation = tileEntity.getCrankRotation();

        AxisAlignedBB crankTop = new AxisAlignedBB(7 / 16d, 10 / 16d, 2 / 16d, 9 / 16d, 12 / 16d, 14 / 16d);
        AxisAlignedBB crankShaft = new AxisAlignedBB(7 / 16d, 0, 7 / 16d, 9 / 16d, 10 / 16d, 9 / 16d).offset(pos.getX(), pos.getY(), pos.getZ());

        crankTop = RotationHelper.rotateBB(crankTop, crankRotation).offset(pos.getX(), pos.getY(), pos.getZ());

        RayTraceResult crankTopPos = crankTop.calculateIntercept(start, end);
        RayTraceResult crankShaftPos = crankShaft.calculateIntercept(start, end);

        if (crankTopPos != null) {
            return new RayTraceResult(start.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), crankTopPos.sideHit, pos);
        }

        if (crankShaftPos != null)
            return new RayTraceResult(start.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), crankShaftPos.sideHit, pos);

        return null;
    }

    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        if (!(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && ItemStack.areItemsEqual(new ItemStack(event.getPlayer().worldObj.getBlockState(event.getTarget().getBlockPos()).getBlock()), Blocks.BLOCK_MISC_CRANK.getStack())))
            return;

        event.setCanceled(true);

        TileEntityCrank tileEntity = TileHelper.getTileEntity(event.getPlayer().worldObj, event.getTarget().getBlockPos(), TileEntityCrank.class);

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        BlockPos posBlock = event.getTarget().getBlockPos();

        AxisAlignedBB crankTop = new AxisAlignedBB(7 / 16d, 10 / 16d, 2 / 16d, 9 / 16d, 12 / 16d, 14 / 16d).offset(-0.5, -0.5, -0.5);
        AxisAlignedBB crankShaft = new AxisAlignedBB(7 / 16d, 0, 7 / 16d, 9 / 16d, 10 / 16d, 9 / 16d).offset(-0.5, -0.5, -0.5);

        GL11.glPushMatrix();
        GlStateManager.translate(posBlock.getX() - renderManager.viewerPosX + 0.5, posBlock.getY() - renderManager.viewerPosY + 0.5, posBlock.getZ() - renderManager.viewerPosZ + 0.5);
        if (tileEntity.isRotating())
            GlStateManager.rotate(tileEntity.getRotation() + 15 * event.getPartialTicks(), 0, 1, 0);
        if (!tileEntity.isRotating())
            GlStateManager.rotate(tileEntity.getRotation(), 0, 1, 0);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.minY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.minY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.minY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.minY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankTop.minX, crankTop.maxY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.maxY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.maxY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.maxY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.maxY, crankTop.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankTop.minX, crankTop.minY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.maxY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.minY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.maxY, crankTop.minZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.minY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.maxX, crankTop.maxY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.minY, crankTop.maxZ).endVertex();
        worldrenderer.pos(crankTop.minX, crankTop.maxY, crankTop.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.minX, crankShaft.minY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.minY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.minY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.minY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.minY, crankShaft.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(crankShaft.minX, crankShaft.minY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.minY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.minZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.minY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.maxX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.minY, crankShaft.maxZ).endVertex();
        worldrenderer.pos(crankShaft.minX, crankShaft.maxY, crankShaft.maxZ).endVertex();
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GL11.glPopMatrix();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_) {
        TileEntityCrank tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityCrank.class);
        EnumFacing crankRotation = tileEntity.getCrankRotation();

        AxisAlignedBB crankTop = new AxisAlignedBB(7 / 16d, 10 / 16d, 2 / 16d, 9 / 16d, 12 / 16d, 14 / 16d);
        AxisAlignedBB crankShaft = new AxisAlignedBB(7 / 16d, 0, 7 / 16d, 9 / 16d, 10 / 16d, 9 / 16d).offset(pos.getX(), pos.getY(), pos.getZ());

        crankTop = RotationHelper.rotateBB(crankTop, crankRotation).offset(pos.getX(), pos.getY(), pos.getZ());

        if (mask != null && crankTop.intersectsWith(mask))
            list.add(crankTop);

        if (mask != null && crankShaft.intersectsWith(mask))
            list.add(crankShaft);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return this.getDefaultState();
    }
}
