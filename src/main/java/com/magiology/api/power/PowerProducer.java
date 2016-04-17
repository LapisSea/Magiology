package com.magiology.api.power;

public interface PowerProducer extends PowerCore{
	/**adds fuel to the buffer*/
	public void addFuel(int Int);
	/**returns if generator can produce power*/
	public boolean canGeneratePower(int Add);
	/**
	 * add your custom booleans for canGeneratePower()
	 */
	public boolean canGeneratePowerAddon();
	/**used every time a generator spends fuel to work*/
	public void generateFunction();
	/**returns power of the object*/
	public int getFuel();
	/**returns maximal possible amount of the power in the fuel buffer*/
	public int getMaxFuel();
	/**sets fuel*/
	public void setFuel(int Int);
	/**sets maximal amount of fuel in the buffer*/
	public void setMaxFuel(int Int);
	/**subtracts fuel from the buffer*/
	public void subtractFuel(int Int);
}
