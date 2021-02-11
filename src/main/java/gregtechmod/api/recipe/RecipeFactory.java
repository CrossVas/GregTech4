package gregtechmod.api.recipe;

import java.util.ArrayList;
import java.util.List;

import gregtechmod.api.util.GT_Utility;

import net.minecraft.item.ItemStack;

/**
 * A Factory class for creating Recipe instances
 * @author TheDarkDnKTv
 *
 */
public abstract class RecipeFactory<F extends RecipeFactory<F>> {
	public static final int MAX_CHANCE = 100_00; // i.m 100.00%
	
	protected RecipeMap<F> map;
	protected List<ItemStack> outputItems;
	protected List<ChancedOutput> chancedOutput;
	protected List<Ingredient> inputItems;
	protected int EUt;
	protected int startEU;
	protected int duration;
	protected boolean shaped;
	
	protected StringBuilder errors;
	// TODO maybe create a fluid I/O
	
	
	protected RecipeFactory() {
		this.outputItems 	= new ArrayList<>();
		this.chancedOutput 	= new ArrayList<>();
		this.inputItems 	= new ArrayList<>();
		this.reset();
	}
	
	protected RecipeFactory(RecipeMap<F> map) {
		this();
		this.map = map;
	}
	
	/** INTERNAL USE ONLY */
	protected void reset() {
		errors = new StringBuilder();
		outputItems.clear();
		chancedOutput.clear();
		inputItems.clear();
		EUt = -1;
		startEU = 0;
		duration = -1;
		shaped = false;
	}
	
	public void setRecipeMap(RecipeMap<F> map) {
		assert 		map != null : "Supplied a null map!";
		assert this.map == null : "It is not allowed to change recipe map if alredy set!";
		
		this.map = map;
	}
	
	public RecipeFactory<F> EUt(int energy) {
		EUt = energy;
		return this;
	}
	
	public RecipeFactory<F> duration(int amount) {
		duration = amount;
		return this;
	}
	
	public RecipeFactory<F> startEU(int amount) {
		startEU = amount;
		return this;
	}
	
	public RecipeFactory<F> setShaped(boolean value) {
		this.shaped = value;
		return this;
	}
	
	public abstract RecipeFactory<F> nonConsumable(ItemStack stack);
	
	public RecipeFactory<F> input(ItemStack stack) {
		return this.input(stack, true, false);
	}
	
	public RecipeFactory<F> input(ItemStack stack, boolean checkNBT) {
		return this.input(stack, true, checkNBT);
	}
	
	/**
	 * @param checkDamage if false, will check by wildcard (any item)
	 * @param checkNBT will check is NBT tags equals
	 */
	public abstract RecipeFactory<F> input(ItemStack stack, boolean checkDamage, boolean checkNBT);
	
	public abstract RecipeFactory<F> inputs(ItemStack...stacks);
	
	public RecipeFactory<F> input(String oreDict) {
		return this.input(oreDict, 1);
	}
	
	public abstract RecipeFactory<F> input(String oreDict, int amount);
	
	public RecipeFactory<F> input(Ingredient ingredient) {
		if (ingredient == null) {
			errors.append(" - Supplied wrong ingredient: " + ingredient);
			return this;
		}
		
		if (ingredient.getVariants().isEmpty()) {
			errors.append(" - Supplied wrong ingredient: no variants! Current: " + ingredient.getVariants());
		}
		
		if (ingredient.getCount() < 0) {
			errors.append(" - Supplied wrong ingredient: count is less than 0! Current: " + ingredient.getCount());
		}
		
		this.inputItems.add(ingredient);
		return this;
	}
	
	public RecipeFactory<F> output(ItemStack stack) {
		if (GT_Utility.isStackInvalid(stack))
			errors.append("Found non-valid stack: " + stack);
		this.outputItems.add(stack);
		return this;
	}
	
	public RecipeFactory<F> outputs(ItemStack...stacks) {
		for (ItemStack stack : stacks)
			this.output(stack);
		return this;
	}
	
	public RecipeFactory<F> chanced(ChancedOutput output) {
		if (output.getChance() < 0 || output.getChance() > MAX_CHANCE) {
			errors.append(" - Chance for " + output.getStack() + " not valid. Shall be 0 <= chance <= 10000");
		}
		
		if (GT_Utility.isStackInvalid(output.getStack())) {
			errors.append(" - Stack for chanced output is invalid: " + output.getStack());
		}
		
		this.chancedOutput.add(output);
		return this;
	}
	
	/**
	 * Chance is in format of int => 9575 == 95.75%, no more than 2 letters after comma
	 */
	public abstract RecipeFactory<F> chanced(ItemStack stack, int chance);
	
	/**
	 * Building the recipe
	 * @return instance of builded recipe
	 * @throws gregtechmod.api.util.GT_RecipeException if missed some attributes of recipe
	 */
	public abstract Recipe build();
	
	/**
	 * Building recipe and registering it in recipe map
	 * @return true if recipe was registered
	 * @throws GT_ItsNotMyFaultException if missed some attributes of recipe
	 */
	public boolean buildAndRegister() {
		Recipe result = this.build();
		if (result != null) {
			return this.map.register(result);
		}
		
		return false;
	}
}