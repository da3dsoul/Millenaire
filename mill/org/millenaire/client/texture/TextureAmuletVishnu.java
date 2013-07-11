package org.millenaire.client.texture;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.millenaire.common.Point;
import org.millenaire.common.core.MillCommonUtilities;

public class TextureAmuletVishnu extends TextureAtlasSprite {

	private static final int radius=20;
	
	public TextureAmuletVishnu(String iconName) {
		super(iconName);
	}

	@Override
	public void updateAnimation()
	{

		int iconPos=getScore(Minecraft.getMinecraft());
		
		if (iconPos>15)
			iconPos=15;
		if (iconPos<0)
			iconPos=0;

		if (iconPos != this.field_110973_g)
        {
            this.field_110973_g = iconPos;
            TextureUtil.func_110998_a((int[])this.field_110976_a.get(this.field_110973_g), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
        }
	}
	
	private int getScore(Minecraft mc) {
		double level=0;

		double closestDistance=Double.MAX_VALUE;

		if((mc.theWorld != null) && (mc.thePlayer != null)) {

			final World world=mc.theWorld;

			final Point p=new Point(mc.thePlayer);

			final List<Entity> entities=MillCommonUtilities.getEntitiesWithinAABB(world, EntityMob.class, p, 20, 20);



			for (final Entity ent : entities) {
				if (p.distanceTo(ent)<closestDistance) {
					closestDistance=p.distanceTo(ent);
				}
			}
		}

		if (closestDistance>radius) {
			level=0;
		} else {
			level=((radius-closestDistance)/radius);
		}
		
		return (int) (level*15);
		
	}

}
