package com.loto.baseloto.items;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class ItemOberlordSword extends ItemSword {

	private float attackSpeed;
	private float attackDamage;

	public ItemOberlordSword(ToolMaterial material, float Speed) {
		super(material);
		this.attackSpeed = Speed;
		this.attackDamage = material.getDamageVsEntity();
	}

	
	public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean par4)
	{
		lores.add("§3§lThe Oversword\n§r§oShoots fireballs.\nLaunches the holder when they are using an elytra.");
	} 
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(
			EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super
				.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER,
							"Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER,
							"Weapon modifier", -2.6D, 0));
		}

		return multimap;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn,
			EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		
		// If the player is using an elytra, do the firework launch thing
		if (playerIn.isElytraFlying()) {
			if (!worldIn.isRemote) {
				EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(
						worldIn, itemstack, playerIn);
				worldIn.spawnEntity(entityfireworkrocket);

			}

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,
					playerIn.getHeldItem(handIn));
		}
		// Otherwise, just shoot a fireball
		else {
			double d1 = 4.0D;
			Vec3d looking = playerIn.getLookVec();
			if (looking != null) {
				Vec3d vec3d = playerIn.getLook(1.0F);
				EntityLargeFireball fireball = new EntityLargeFireball(worldIn);
				fireball.setPositionAndUpdate(playerIn.posX + vec3d.x * d1,
						playerIn.posY, playerIn.posZ + vec3d.z * d1);
				fireball.motionX = looking.x;
				fireball.motionY = looking.y;
				fireball.motionZ = looking.z;
				fireball.accelerationX = fireball.motionX * 0.2D;
				fireball.accelerationY = fireball.motionY * 0.2D;
				fireball.accelerationZ = fireball.motionZ * 0.2D;
				fireball.explosionPower = 4;
				worldIn.spawnEntity(fireball);
				playerIn.getCooldownTracker().setCooldown(this, 3);
				return new ActionResult(EnumActionResult.SUCCESS, itemstack);
			}
		}

		return new ActionResult(EnumActionResult.FAIL, itemstack);

	}

}
