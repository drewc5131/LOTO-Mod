package com.loto.baseloto.proxy;

import com.loto.baseloto.reg.CreateBlocks;
import com.loto.baseloto.reg.CreateItems;
import com.loto.baseloto.reg.CreateMobs;

public class ServerProxy extends CommonProxy
{

	@Override
	public void createMobs()
	{
		CreateMobs.createMobs();
	}
}
