package gregtechmod.common.containers;

import gregtechmod.api.gui.GT_ContainerMetaTile_Machine;
import gregtechmod.api.gui.GT_Slot_Holo;
import gregtechmod.api.gui.GT_Slot_Output;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.common.tileentities.GT_MetaTileEntity_Electrolyzer;

import java.util.Iterator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GT_Container_Electrolyzer extends GT_ContainerMetaTile_Machine {

	public GT_Container_Electrolyzer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID) {
		super(aInventoryPlayer, aTileEntity, aID);
	}

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot				(mTileEntity, 0,  80,  46));
        addSlotToContainer(new Slot				(mTileEntity, 1,  50,  46));
        addSlotToContainer(new GT_Slot_Output	(mTileEntity, 2,  50,  16));
        addSlotToContainer(new GT_Slot_Output	(mTileEntity, 3,  70,  16));
        addSlotToContainer(new GT_Slot_Output	(mTileEntity, 4,  90,  16));
        addSlotToContainer(new GT_Slot_Output	(mTileEntity, 5, 110,  16));
        addSlotToContainer(new GT_Slot_Holo		(mTileEntity, 6, 110,  46, false, false, 64));
    }
    
    public int mProgress, mMaxProgress, mProgressScale;
    
    @SuppressWarnings("rawtypes")
	public void detectAndSendChanges() {
        super.detectAndSendChanges();
    	if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
    	mProgress = ((GT_MetaTileEntity_Electrolyzer)mTileEntity.getMetaTileEntity()).getProgresstime();
    	mMaxProgress = ((GT_MetaTileEntity_Electrolyzer)mTileEntity.getMetaTileEntity()).maxProgresstime();
    	mProgressScale = Math.max(0, Math.min(10, (mProgress>0?1:0) + (mProgress * 10) / (mMaxProgress<1?1:mMaxProgress)));
    	
        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting)var2.next();
            var1.sendProgressBarUpdate(this, 100, mProgress);
            var1.sendProgressBarUpdate(this, 101, mMaxProgress);
            var1.sendProgressBarUpdate(this, 102, mProgressScale);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	super.updateProgressBar(par1, par2);
    	switch (par1) {
    	case 100: mProgress = par2; break;
    	case 101: mMaxProgress = par2; break;
    	case 102: mProgressScale = par2; break;
    	}
    }
    
    public int getSlotCount() {
    	return 6;
    }

    public int getShiftClickSlotCount() {
    	return 2;
    }
}
