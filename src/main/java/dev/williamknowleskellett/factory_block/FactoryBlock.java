package dev.williamknowleskellett.factory_block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FactoryBlock extends Block implements BlockEntityProvider {

    public FactoryBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FactoryBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FactoryBlockEntity factoryBlockEntity) {
            // DemoBlockEntity demoBlockEntity = (DemoBlockEntity) blockEntity;
            ItemStack itemStack = player.getStackInHand(hand);
            return factoryBlockEntity.interact(itemStack, player, hand);
        }

        return ActionResult.PASS;
    }

}
