package dev.williamknowleskellett.factory_block;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryBlockMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "factory_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final Identifier FACTORY_BLOCK_IDENTIFIER = new Identifier(MODID, "factory_block");
	public static final Block FACTORY_BLOCK = Registry.register(Registries.BLOCK, FACTORY_BLOCK_IDENTIFIER, new FactoryBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f)));
	public static final BlockEntityType<FactoryBlockEntity> FACTORY_BLOCK_ENTITY = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        new Identifier(MODID, "factory_block_entity"),
        FabricBlockEntityTypeBuilder.create(FactoryBlockEntity::new, FACTORY_BLOCK).build()
    );
	public static final Item FACTORY_BLOCK_ITEM = Registry.register(Registries.ITEM, FACTORY_BLOCK_IDENTIFIER, new BlockItem(FACTORY_BLOCK, new Item.Settings()));

	@Override
	public void onInitialize() {
		
	}
}