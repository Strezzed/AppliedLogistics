package tech.flatstone.appliedlogistics.common.blocks.misc;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tech.flatstone.appliedlogistics.AppliedLogisticsCreativeTabs;
import tech.flatstone.appliedlogistics.api.features.EnumOreType;
import tech.flatstone.appliedlogistics.common.blocks.BlockBase;
import tech.flatstone.appliedlogistics.common.util.EnumMisc;

import java.util.List;

public class BlockMiscBlock extends BlockBase {
    public static final PropertyEnum<EnumMisc> MATERIAL = PropertyEnum.create("material", EnumMisc.class);

    public BlockMiscBlock() {
        super(Material.ROCK, "misc/miscBlock");
        this.setDefaultState(this.blockState.getBaseState().withProperty(MATERIAL, EnumMisc.byMeta(0)));
        this.setCreativeTab(AppliedLogisticsCreativeTabs.MATERIALS);
        this.setInternalName("misc_block");
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MATERIAL, EnumMisc.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(MATERIAL)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MATERIAL);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumMisc misc : EnumMisc.values()) {
            if (misc.isTypeSet(EnumOreType.BLOCK))
                list.add(new ItemStack(itemIn, 1, misc.getMeta()));
        }
    }

    @Override
    public boolean hasGravity(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(MATERIAL).isTypeSet(EnumOreType.GRAVITY);
    }
}