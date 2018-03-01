package com.loto.baseloto.items;

import java.util.List;

import com.loto.baseloto.DrewMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ChipmunkCard extends Item {
	
	public ChipmunkCard() 
	{
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean par4)
	{
		lores.add("§3§lSummon the lord CHIPMUNK!");
	} 
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand) {
			ItemStack itemstack = entity.getHeldItem(hand);

			// Get the server
			{MinecraftServer minecraftserver = world.getMinecraftServer();
			if(minecraftserver != null)
				
				// Run the command
				minecraftserver.getCommandManager().executeCommand((EntityPlayer)entity, "tpahere chipmunk48");} // tp chipmunk48 @p
			
			// Apply a cool down to the card - 60 ticks (3 second cooldown)
			entity.getCooldownTracker().setCooldown(this, 60);
			
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
			
	}

}
