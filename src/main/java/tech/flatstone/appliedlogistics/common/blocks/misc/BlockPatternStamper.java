package tech.flatstone.appliedlogistics.common.blocks.misc;

import com.fireball1725.firelib.guimaker.GuiMaker;
import com.fireball1725.firelib.guimaker.IImplementsGuiMaker;
import com.fireball1725.firelib.guimaker.objects.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tech.flatstone.appliedlogistics.AppliedLogistics;
import tech.flatstone.appliedlogistics.AppliedLogisticsCreativeTabs;
import tech.flatstone.appliedlogistics.common.blocks.BlockTileBase;
import tech.flatstone.appliedlogistics.common.blocks.Blocks;
import tech.flatstone.appliedlogistics.common.tileentities.misc.TileEntityPatternStamper;
import tech.flatstone.appliedlogistics.common.util.IProvideRecipe;
import tech.flatstone.appliedlogistics.common.util.LanguageHelper;
import tech.flatstone.appliedlogistics.common.util.TileHelper;

import javax.annotation.Nullable;

public class BlockPatternStamper extends BlockTileBase implements IProvideRecipe, IImplementsGuiMaker {
    private GuiMaker guiMaker;

    // Gui Object for Machine Tab
    private GuiLabel labelSlotDetails = new GuiLabel(34, 6, 0xffffff, "Hello World...");

    public BlockPatternStamper() {
        super(Material.ROCK, "misc/blockPatternStamper");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTileEntity(TileEntityPatternStamper.class);
        this.setCreativeTab(AppliedLogisticsCreativeTabs.MACHINES);
        this.setInternalName("pattern_stamper");

        guiMaker = new GuiMaker(this, 194, 232) {
            @Override
            public void guiObjectClicked(int controlID) {

            }

            @Override
            public void guiObjectUpdated(int controlID) {

            }
        };
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityPatternStamper tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityPatternStamper.class);

        if (worldIn.isRemote)
            return true;

        guiMaker.setGuiTitle(tileEntity.hasCustomName() ? tileEntity.getCustomName() : LanguageHelper.NONE.translateMessage(tileEntity.getUnlocalizedName()));
        guiMaker.show(AppliedLogistics.instance, worldIn, playerIn, pos);

        return true;
    }

    @Override
    public void drawGui(TileEntity tileEntity) {

    }

    @Override
    public void initGui(TileEntity tileEntity, InventoryPlayer inventoryPlayer) {
        guiMaker.clearGuiTabs();

        GuiTab tabMachine = new GuiTab(this.guiMaker, "Pattern Stamper", Blocks.BLOCK_PATTERN_STAMPER.getStack());
        GuiTab tabAbout = new GuiTab(this.guiMaker, "About", 1);

        tabMachine.addGuiObject(new GuiSlot(4, 4, 28, 28, 0));
        tabMachine.addGuiObject(labelSlotDetails);

        tabMachine.addGuiObject(new GuiWindow(34, 18, 156, 14)); // Progress Bar (for weight)...

        tabMachine.addGuiObject(new GuiButton(-999, 4, 34, 16, "<"));
        tabMachine.addGuiObject(new GuiButton(-999, 120, 34, 16, ">"));
        tabMachine.addGuiObject(new GuiButton(-999, 138, 34, 52, "Stamp"));

        tabMachine.addGuiObject(new GuiCenteredLabel(22, 38, 96, 0xFFFFFF, "Hello World..."));

        tabMachine.addGuiObject(new GuiLine(164, 14, 1, 18, 0xffa1a1a1));
        tabMachine.addGuiObject(new GuiLabel(158, 14, 0xa1a1a1, 0.5f, "Ok"));
        tabMachine.addGuiObject(new GuiLabel(166, 14, 0xa1a1a1, 0.5f, "Error"));

        tabMachine.addGuiObject(new GuiScrollBox(this.guiMaker, 4, 52, 186, 80));

        tabMachine.addGuiObject(new GuiLabel(5, 134, 0xffffff, "Total Building Time: 0:00:00"));
        tabMachine.addGuiObject(new GuiLabel(5, 143, 0xffffff, "Total Experence Cost: 0L"));

        tabMachine.addGuiObject(new GuiInventorySlots(4, 152));

        guiMaker.addGuiTab(tabMachine);
        guiMaker.addGuiTab(tabAbout);
    }

    @Override
    public void RegisterRecipes() {

    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityPatternStamper tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityPatternStamper.class);
        if (tileEntity != null)
            return state.withProperty(FACING, tileEntity.getForward());
        return state.withProperty(FACING, EnumFacing.NORTH);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
}
