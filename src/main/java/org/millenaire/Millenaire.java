package org.millenaire;

import java.util.List;

import org.millenaire.blocks.BlockAlchemists;
import org.millenaire.blocks.BlockDecorative;
import org.millenaire.blocks.BlockMillChest;
import org.millenaire.blocks.BlockMillCrops;
import org.millenaire.blocks.BlockMillPath;
import org.millenaire.blocks.BlockMillSign;
import org.millenaire.blocks.BlockVillageStone;
import org.millenaire.blocks.StoredPosition;
import org.millenaire.entities.EntityMillVillager;
import org.millenaire.gui.MillAchievement;
import org.millenaire.gui.MillGuiHandler;
import org.millenaire.items.ItemMillAmulet;
import org.millenaire.items.ItemMillArmor;
import org.millenaire.items.ItemMillFood;
import org.millenaire.items.ItemMillParchment;
import org.millenaire.items.ItemMillTool;
import org.millenaire.items.ItemMillWallet;
import org.millenaire.items.ItemMillWand;
import org.millenaire.items.MillItems;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Millenaire.MODID, name = Millenaire.NAME, version = Millenaire.VERSION, guiFactory = Millenaire.GUIFACTORY)
public class Millenaire 
{
	public static final String MODID = "millenaire";
	public static final String NAME = "Mill\u00e9naire";
	public static final String VERSION = "7.0.0";
	public static final String GUIFACTORY = "org.millenaire.gui.MillGuiFactory";
	
	public List<Block> forbiddenBlocks;
	
	@Instance
	public static Millenaire instance = new Millenaire();
	public static SimpleNetworkWrapper simpleNetworkWrapper;
	
	public static final CreativeTabs tabMillenaire = new CreativeTabs("MillTab")
	{
		public Item getTabIconItem() 
		{
			return MillItems.denierOr;
		}
	};
	
	@EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
		MillConfig.preinitialize();
		MinecraftForge.EVENT_BUS.register(new RaidEvent.RaidEventHandler());
		
		setForbiddenBlocks();
		
		MillItems.preinitialize();
		ItemMillFood.preinitialize();
		ItemMillArmor.preinitialize();
		ItemMillWand.preinitialize();
		ItemMillTool.preinitialize();
		ItemMillAmulet.preinitialize();
		ItemMillWallet.preinitialize();
		ItemMillParchment.preinitialize();
		BlockDecorative.preinitialize();
		BlockMillCrops.preinitialize();
		BlockMillChest.preinitialize();
		BlockMillSign.preinitialize();
		BlockMillPath.preinitialize();
		BlockAlchemists.preinitialize();
		BlockVillageStone.preinitialize();
		StoredPosition.preinitialize();
		EntityMillVillager.preinitialize();
		
		MillCulture.preinitialize();
		
		MillAchievement.preinitialize();
		
		if(event.getSide() == Side.CLIENT)
		{
			ItemMillTool.prerender();
			ItemMillAmulet.prerender();
			ItemMillParchment.prerender();
			BlockDecorative.prerender();
			BlockMillPath.prerender();
			EntityMillVillager.prerender();
			
			MillConfig.eventRegister();
		}
		
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MillChannel");
		simpleNetworkWrapper.registerMessage(PacketHandlerOnServer.class, MillPacket.class, 69, Side.SERVER);
    }
	
	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new MillGuiHandler());
		
		if(event.getSide() == Side.CLIENT)
    	{	
			MillItems.render();
			ItemMillFood.render();
			ItemMillArmor.render();
			ItemMillWand.render();
			ItemMillTool.render();
			ItemMillWallet.render();
			BlockDecorative.render();
			BlockMillCrops.render();
			BlockMillChest.render();
			BlockMillSign.render();
			BlockAlchemists.render();
			BlockVillageStone.render();
			StoredPosition.render();
    	}
    }
	
	@EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
		
    }
	
	private void setForbiddenBlocks()
	{
		String parsing = MillConfig.forbiddenBlocks.substring(11);
		for (final String name : parsing.split(", |,"))
		{
			if(Block.blockRegistry.containsKey(new ResourceLocation(name)))
			{
				forbiddenBlocks.add(Block.blockRegistry.getObject(new ResourceLocation(name)));
			}
		}
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new MillCommand());
	}
}