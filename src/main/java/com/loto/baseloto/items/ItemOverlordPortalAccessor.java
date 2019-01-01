package com.loto.baseloto.items;

import com.loto.baseloto.block.BlockOverlordPortal;
import com.loto.baseloto.reg.CreateBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemOverlordPortalAccessor extends Item {

	
	public ItemOverlordPortalAccessor() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer entity, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ) {
		pos = pos.offset(facing);
		ItemStack itemstack = entity.getHeldItem(hand);
		if (!entity.canPlayerEdit(pos, facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			if (world.isAirBlock(pos)) {
				System.out.println("CREATING PORTAL AT" + pos.toString());
				((BlockOverlordPortal) CreateBlocks.overlordPortal).trySpawnPortal(world, pos);
			}
			return EnumActionResult.SUCCESS;
		}
	}
}
