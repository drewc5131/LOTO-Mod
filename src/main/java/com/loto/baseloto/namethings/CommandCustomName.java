package com.loto.baseloto.namethings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.loto.baseloto.DrewMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandCustomName extends CommandBase implements ICommand {

	private final List<String> aliases;
	
	public CommandCustomName()
	{
		aliases = new ArrayList();
		aliases.add("customname");aliases.add("cm");
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "customname";
	}
	
	@Override
	public String getUsage(ICommandSender sender){
		return "/customname <set|clear> <name>";
	}
	
	@Override
	public List<String> getAliases(){
		return this.aliases;}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos){
		if(args.length == 1){
			return CommandBase.getListOfStringsMatchingLastWord(args, "set", "clear");
		}
		else{
			return Collections.<String>emptyList();
		}
	}
	
	@Override public int getRequiredPermissionLevel(){
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
		
		if(args.length > 0){
			if(args[0].toLowerCase().equals("set")){
				// Setting own nickname (/cm set <name>)
				if(args.length == 2){
					String customName = args[1];
					EntityPlayerMP player = CommandBase.getPlayer(server, sender, sender.getName());
					NBTTagCompound tag = player.getEntityData();
					customName = customName.replace("&", "\u00a7"); // Replace & with § for format codes
					customName = customName.replace("##", " "); // Replace ## with a space if wanted
					tag.setString("loto_customName", customName);
					sender.sendMessage(new TextComponentString("You renamed yourself to " + customName));
					DrewMod.network.sendToAll(new CustomNamePacket(customName, player.getEntityId(), 0));
					player.refreshDisplayName();
				}
			}
			else if(args[0].toLowerCase().equals("clear")){
				if(args.length == 1){
					EntityPlayerMP player = (EntityPlayerMP)sender;
					if(player.getEntityData() != null && player.getEntityData().hasKey("loto_customName")){
						player.getEntityData().removeTag("loto_customName");
						sender.sendMessage(new TextComponentString("Your custom name was removed"));
						DrewMod.network.sendToAll(new CustomNamePacket("lol", player.getEntityId(), 1));
						player.refreshDisplayName();
					}
					else{sender.sendMessage(new TextComponentString("\u00a7c You do not have a custom name!"));}
				}
				else{
					throw new WrongUsageException("\u00a7c/customname clear\u00a7r, that is it, do not over complicate it");
				}
				
			}
			else{
				throw new WrongUsageException(this.getUsage(sender));
			}
		}
		else{
			throw new WrongUsageException(this.getUsage(sender));
		}
	}
	

}
