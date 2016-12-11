# Magiology ![alt text][logo]
This is a mod for Minecraft.
It is orientated atround bending reality to your will with acient spirits and with help of technology.
While you are not playing with magical tech you can explore secret structures made by a smart species penguins. (Why penguins? Well it's obvius. Penguins created the world)


#### To my fellow modders looking for tips:
* Need a premade ModContainer? Search for ```GenericModContainerImpl```! (extend your ```@Mod``` class with it)
* Prefer to use ```@EventBusSubscriber``` with ```static``` event functions over ```MinecraftForge.EVENT_BUS.register(...)``` manual registering. Note: add ```@EventBusSubscriber``` to ```class``` that contains ```@SubscribeEvent```

[logo]: http://i.imgur.com/lPdrDdJ.png "Magiology logo"
