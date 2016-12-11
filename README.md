# Magiology ![alt text][logo]
* This is a mod for Minecraft.
* It is orientated around bending reality to your will with acient spirits and with help of technology.
* While you are not playing with magical tech you can explore secret structures made by a smart species penguins. (Why penguins? Well it's obvius. Penguins created the world! Oooor I am to lazy to make an indepth decription of a mod that is being developed and can change at any moment.)
___

#### To my fellow modders looking for tips:
* Need a premade ModContainer? You can use my [```GenericModContainerImpl```](../1.10/src/main/java/com/magiology/core/GenericModContainerImpl.java)! (extend your ```@Mod``` class with it)
* Prefer to use ```@EventBusSubscriber``` with ```static``` event functions over ```MinecraftForge.EVENT_BUS.register(...)``` manual registering. Note: add ```@EventBusSubscriber``` to ```class``` that contains ```@SubscribeEvent```
* Not sure how your ```@Mod``` class should look like? For more or less fully functional class (in terms of what forge features it uses)  you can take a look at the [Magiology](../1.10/src/main/java/com/magiology/core/Magiology.java) class. Note: Why should you trust me? Well I have inspected the behind the scenes of forge mod initialization so I consider that I know what I am doing. :)
* Forge documentation sources that I know of:
  * [MCForge documentation on github](https://github.com/MinecraftForge/Documentation)
  * ["Official" MCForge documentation](https://mcforge.readthedocs.io)

___

[logo]: http://i.imgur.com/lPdrDdJ.png "Magiology logo"
