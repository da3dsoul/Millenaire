package org.millenaire.entities;

import java.util.ArrayList;
import java.util.List;

import org.millenaire.CommonUtilities;
import org.millenaire.MillConfig;
import org.millenaire.MillCulture;
import org.millenaire.MillCulture.VillageType;
import org.millenaire.VillagerType;
import org.millenaire.blocks.BlockVillageStone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TileEntityVillageStone extends TileEntity
{
	List<EntityMillVillager> currentVillagers = new ArrayList<EntityMillVillager>();
	
	//Control Value.  Changed when using wandSummon, if left as 'biome' when onLoad called, culture decided by biome.
	public String culture = "biome";
	public boolean randomVillage = true;
	public VillageType villageType;
	public String villageName;
	public boolean willExplode = false;
	
	public int testVar = 0;
	
	@Override
	public void onLoad()
    {
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        
        if(world.getBlockState(pos).getBlock() instanceof BlockVillageStone)
        {
        	
        	if(culture.equalsIgnoreCase("biome"))
        	{
        		if (world.getBiomeGenForCoords(pos) != null)
        		{
        			//Do awesome stuff and set culture.  Below is simply for testing.
        			System.out.println("Village Culture being set by biome");
        			culture = "norman";
        		}
        	}
        	
        	try
        	{
        		if(randomVillage)
        			villageType = MillCulture.getCulture(culture).getRandomVillageType();
        		else
        			villageType = MillCulture.getCulture(culture).getVillageType(villageName);
        		
        		villageName = villageType.getVillageName();
        		
        		if(MillConfig.villageAnnouncement)
        		{
        			if(world.isRemote)
        			{
        				for(int i = 0; i < world.playerEntities.size(); i++)
        					world.playerEntities.get(i).addChatMessage(new ChatComponentText(culture + " village " + villageName + " discovered at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
        			}
        		}
        			
        		if(!world.isRemote)
        			System.out.println(culture + " village " + villageName + " crated at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
        	}
        	catch(Exception ex)
        	{
        		System.err.println("Something went catastrophically wrong creating this village");
        		ex.printStackTrace();
        		return;
        	}
        }
        else
        {
        	System.err.println("VillageStone TileEntity loaded wrong");
        }
    }
	
	//@SideOnly(Side.SERVER)
	public EntityMillVillager createVillager(World worldIn, MillCulture cultureIn, int villagerID)
	{
		VillagerType currentVillagerType;
		int currentGender;
		
		if(villagerID == 0)
		{
			int balance = 0;
			villagerID = CommonUtilities.getRandomNonzero();
			boolean checkAgain = false;

			for(int i = 0; i < currentVillagers.size(); i++)
			{
				if(currentVillagers.get(i).getGender() == 0)
					balance++;
				else
					balance--;
				
				if(villagerID == currentVillagers.get(i).villagerID)
				{
					villagerID = CommonUtilities.getRandomNonzero();
					checkAgain = true;
				}
			}
			while(checkAgain)
			{
				checkAgain = false;
				for(int i = 0; i < currentVillagers.size(); i++)
				{
					if(villagerID == currentVillagers.get(i).villagerID)
					{
						villagerID = CommonUtilities.getRandomNonzero();
						checkAgain = true;
					}
				}
			}
			
			balance += CommonUtilities.randomizeGender();
			
			if(balance < 0)
			{
				currentGender = 0;
				currentVillagerType = cultureIn.getChildType(0);
			}
			else
			{
				currentGender = 1;
				currentVillagerType = cultureIn.getChildType(1);
			}
			
			EntityMillVillager newVillager = new EntityMillVillager(worldIn, villagerID, cultureIn);
			newVillager.setTypeAndGender(currentVillagerType, currentGender);
			
			return newVillager;
		}
		else
		{
			for(int i = 0; i < currentVillagers.size(); i++)
			{
				if(villagerID == currentVillagers.get(i).villagerID)
					return currentVillagers.get(i);
			}
			
			System.err.println("Attempted to create nonspecific Villager.");
		}

		return null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		
    }
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
    {
		
    }
}