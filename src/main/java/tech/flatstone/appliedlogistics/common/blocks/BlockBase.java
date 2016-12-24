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

package tech.flatstone.appliedlogistics.common.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.common.network.PacketHandler;
import tech.flatstone.appliedlogistics.common.network.messages.PacketBlockRotated;
import tech.flatstone.appliedlogistics.common.tileentities.TileEntityBase;
import tech.flatstone.appliedlogistics.common.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockBase extends Block implements IBlockRenderer {
    protected boolean isInventory = false;
    protected String resourcePath = "";
    protected String internalName = "";
    protected boolean fallInstantly = false;

    protected BlockBase(Material material, String resourcePath) {
        super(material);

        setHardness(2.2F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
        this.resourcePath = resourcePath;
    }

    private static boolean canFallThrough(IBlockState blockState) {
        Block block = blockState.getBlock();
        Material material = blockState.getMaterial();
        return block == net.minecraft.init.Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (hasGravity(worldIn, pos))
            worldIn.scheduleUpdate(pos, this, 2);

        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (hasGravity((World) world, pos))
            ((World) world).scheduleUpdate(pos, this, 2);

        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        if (!worldIn.isRemote)
            this.checkFallable(worldIn, pos);
    }

    private void checkFallable(World worldIn, BlockPos pos) {
        if (!hasGravity(worldIn, pos))
            return;

        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
            int i = 32;

            if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
                if (!worldIn.isRemote) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntity(entityfallingblock);
                }
            } else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
                    ;
                }

                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlock fallingEntity) {

    }

    protected void onEndFalling(World worldIn, BlockPos pos) {

    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public boolean hasGravity(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public String getUnlocalizedName() {
        String blockName = getUnwrappedUnlocalizedName(super.getUnlocalizedName());

        return String.format("tile.%s.%s", ModInfo.MOD_ID, blockName);
    }

    private String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityBase tileEntity = TileHelper.getTileEntity(world, pos, TileEntityBase.class);
        if (tileEntity != null && tileEntity.hasCustomName()) {
            final ItemStack itemStack = new ItemStack(this, 1, tileEntity.getBlockMetadata());
            itemStack.setStackDisplayName(tileEntity.getCustomName());

            ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
            drops.add(itemStack);

            return drops;
        }
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        final IOrientable rotatable = this.getOrientable(world, pos);
        if (rotatable != null && rotatable.canBeRotated()) {
            if (this.hasCustomRotation()) {
                this.customRotateBlock(rotatable, axis);
                sendTileRotation(world, pos);
                return true;
            } else {
                EnumFacing forward = rotatable.getForward();

                for (int rs = 0; rs < 4; rs++) {
                    forward = Platform.rotateAround(forward, axis);

                    if (this.isValidOrientation(world, pos, forward)) {
                        rotatable.setOrientation(forward);
                        sendTileRotation(world, pos);
                        return true;
                    }
                }
            }
        }

        return super.rotateBlock(world, pos, axis);
    }

    private void sendTileRotation(World world, BlockPos pos) {
        TileEntityBase tileEntity = TileHelper.getTileEntity(world, pos, TileEntityBase.class);
        if (tileEntity != null)
            tileEntity.onRotated();
        PacketHandler.INSTANCE.sendToAllAround(new PacketBlockRotated(pos), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 100));
    }

    private boolean hasCustomRotation() {
        return false;
    }

    private void customRotateBlock(final IOrientable rotatable, final EnumFacing axis) {

    }

    private boolean isValidOrientation(final World world, final BlockPos pos, final EnumFacing forward) {
        return true;
    }

    public IOrientable getOrientable(final IBlockAccess world, final BlockPos pos) {
        if (this instanceof IOrientableBlock)
            return this.getOrientable(world, pos);
        return null;
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return new EnumFacing[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockRenderer() {
        final String resourcePath = String.format("%s:%s", ModInfo.MOD_ID, this.resourcePath);

        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
            @SideOnly(Side.CLIENT)
            @Override
            @MethodsReturnNonnullByDefault
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(resourcePath, getPropertyString(state.getProperties()));
            }
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockItemRenderer() {
        final String resourcePath = String.format("%s:%s", ModInfo.MOD_ID, this.resourcePath);

        NonNullList<ItemStack> subBlocks = NonNullList.create();
        getSubBlocks(Item.getItemFromBlock(this), null, subBlocks);

        for (ItemStack itemStack : subBlocks) {
            IBlockState blockState = this.getStateFromMeta(itemStack.getItemDamage());
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), itemStack.getItemDamage(), new ModelResourceLocation(resourcePath, Platform.getPropertyString(blockState.getProperties())));
        }
    }
}
