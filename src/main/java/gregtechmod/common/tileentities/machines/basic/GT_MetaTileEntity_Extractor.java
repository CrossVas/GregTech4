package gregtechmod.common.tileentities.machines.basic;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtechmod.api.recipe.RecipeMap;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;

public class GT_MetaTileEntity_Extractor extends GT_MetaTileEntity_BasicMachine {
	
	public GT_MetaTileEntity_Extractor(int aID, String mName, RecipeMap<?> recipeMap) {
		super(aID, mName, recipeMap);
		loopLen = 290;
	}
	
	public GT_MetaTileEntity_Extractor(RecipeMap<?> recipeMap) {
		super(recipeMap);
		loopLen = 290;
	}
	
	@Override public void onRightclick(EntityPlayer aPlayer)		{getBaseMetaTileEntity().openGUI(aPlayer, 132);}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Extractor(recipeLogic.recipeMap);
	}
	
	@Override
	public void doSound(byte aIndex, double aX, double aY, double aZ) {
		super.doSound(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(200), 10, 1.0F, aX, aY, aZ);
		}
	}
	
	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}
	
	@Override
	public int getSideFacingInactive() {
		return 242;
	}
	
	@Override
	public int getSideFacingActive() {
		return 227;
	}
	
	@Override
	public int getFrontFacingInactive() {
		return 246;
	}
	
	@Override
	public int getFrontFacingActive() {
		return 247;
	}
	
	@Override
	public int getTopFacingInactive() {
		return 230;
	}
	
	@Override
	public int getTopFacingActive() {
		return 231;
	}
	
	@Override
	public String getDescription() {
		return "metatileentity.GT_Extractor.tooltip";
	}
}
