package com.loto.baseloto.block;

import java.util.List;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.backend.sound.DrewSoundEvents;
import com.loto.baseloto.machine.EntityMachinePrimed;
import com.loto.baseloto.reg.CreateItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO: make this do something that after 1 minecraft hour the GET OFF THE MACHINES sound plays and locks you out of it until the next minecraft day
public class ChrisMachine extends Block
{
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");

    public ChrisMachine()
    {
    	super(Material.ANVIL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, Boolean.valueOf(false)));
        this.setCreativeTab(DrewMod.tabDrew);
    }
	public void addInformation(Block block, EntityPlayer player, List lores, boolean par4)
	{
		lores.add("Probably won't support a 970!");
	}
	
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    
    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(EXPLODE)).booleanValue())
            {
                EntityMachinePrimed entitytntprimed = new EntityMachinePrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter);
                worldIn.spawnEntity(entitytntprimed);
                worldIn.playSound((EntityPlayer)null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, DrewSoundEvents.machines, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (heldItem != null)
        {
            Item item = heldItem.getItem();

            if (item == CreateItems.Gtx970)
            {
                this.explode(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)), playerIn);

                return true;
            }
            
            if (item != CreateItems.Gtx970)
            {
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(EXPLODE, Boolean.valueOf((meta & 1) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(EXPLODE)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {EXPLODE});
    }
    
}