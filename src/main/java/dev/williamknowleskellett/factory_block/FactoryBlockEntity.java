package dev.williamknowleskellett.factory_block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FactoryBlockEntity extends BlockEntity implements SidedInventory {
    public static int ORIGINAL_STACK = 0;
    public static int CLONE_STACK = 1;
    private static int[] SLOTS = new int[] { 0, 1 };
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public FactoryBlockEntity(BlockPos pos, BlockState state) {
        super(FactoryBlockMod.FACTORY_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.markDirty();
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.get(CLONE_STACK).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == CLONE_STACK) {
            return this.inventory.get(slot).copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        // Mimic Inventories::splitStack
        if (slot == CLONE_STACK) {
            return this.inventory.get(slot).copyWithCount(amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot == CLONE_STACK) {
            return this.inventory.get(slot).copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot != ORIGINAL_STACK)
            return;
        this.markDirty();
        stack.setCount(stack.getMaxCount());
        this.inventory.set(CLONE_STACK, stack);
        // this.inventory.set(CLONE_STACK, stack.copy());
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        ItemStack itemStack = this.inventory.get(CLONE_STACK);
        if (itemStack.isEmpty())
            return;

        NbtCompound nbtCompound = new NbtCompound();
        itemStack.writeNbt(nbtCompound);
        nbt.put("Item", nbtCompound);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (nbt.contains("Item")) {
            NbtCompound itemStackNbt = nbt.getCompound("Item");
            ItemStack itemStack = ItemStack.fromNbt(itemStackNbt);
            // this.inventory.set(0, itemStack);
            this.inventory.set(CLONE_STACK, itemStack);
        }
    }

    @Override
    public int[] getAvailableSlots(Direction var1) {
        return SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return slot == ORIGINAL_STACK;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return slot == CLONE_STACK;
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean factoryIsEmpty = this.inventory.get(CLONE_STACK).isEmpty();
        boolean handIsEmpty = itemStack.isEmpty();
        FactoryBlockMod.LOGGER.info(factoryIsEmpty ? "Factory is empty" : "Factory is not empty");
        FactoryBlockMod.LOGGER.info(handIsEmpty ? "Hand is empty" : "Hand is not empty");
        if (this.world.isClient) {
            return ActionResult.CONSUME;
        }
        if (handIsEmpty) {
            if (factoryIsEmpty) {
                return ActionResult.PASS;
            }
            player.setStackInHand(hand, this.inventory.get(CLONE_STACK).copy());
            FactoryBlockMod.LOGGER.info("Filled hand " + hand.name());
        } else {
            this.setStack(ORIGINAL_STACK, itemStack.copy());
            // this.emitGameEvent(GameEvent.BLOCK_CHANGE, player);
            if (!player.getAbilities().creativeMode) {
                itemStack.setCount(0);
            }
            FactoryBlockMod.LOGGER.info("Emptied hand " + hand.name());
        }
        return ActionResult.SUCCESS;
    }
}
