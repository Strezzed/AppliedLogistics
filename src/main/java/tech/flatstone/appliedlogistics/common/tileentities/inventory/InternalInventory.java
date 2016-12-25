/*
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 *  * Rent;
 *  * Sell;
 *  * Lease;
 *  * Offer on a pay-per-play basis;
 *  * Distribute for money or any other consideration; or
 *  * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 *  * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 *  4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 *  5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 *  Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.common.tileentities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import tech.flatstone.appliedlogistics.common.util.Platform;
import tech.flatstone.appliedlogistics.common.util.iterators.InventoryIterator;

import java.util.Iterator;

public class InternalInventory implements IInventory, Iterable<ItemStack> {
    protected final int size;
    protected final ItemStack[] inventory;
    public boolean enableClientEvents = false;
    protected IInventoryHandler inventoryHandler;
    protected int maxSize;

    public InternalInventory(IInventoryHandler inventory, int size) {
        this.size = size;
        this.inventory = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            this.inventory[i] = ItemStack.EMPTY;
        }
        this.inventoryHandler = inventory;
        this.maxSize = 64;
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.size; i++) {
            if (this.getStackInSlot(i) != null)
                return false;
        }

        return true;
    }

    protected boolean eventsEnabled() {
        return Platform.isServer() || this.enableClientEvents;
    }

    @Override
    public int getSizeInventory() {
        return this.size;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int qty) {
        if (this.inventory[slot] != null) {
            ItemStack split = this.getStackInSlot(slot);
            ItemStack newStack;

            if (qty >= split.getCount()) {
                newStack = this.inventory[slot];
                this.inventory[slot] = null;
            } else {
                newStack = split.splitStack(qty);
            }

            if (inventoryHandler != null && this.eventsEnabled()) {
                this.inventoryHandler.onChangeInventory(this, slot, InventoryOperation.decreaseStackSize, newStack, ItemStack.EMPTY);
            }

            this.markDirty();
            return newStack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        ItemStack oldStack = this.inventory[slot];
        this.inventory[slot] = itemStack;

        if (this.inventoryHandler != null && this.eventsEnabled()) {
            ItemStack removed = oldStack;
            ItemStack added = itemStack;

            if (oldStack != null && itemStack != null && Platform.isSameItem(oldStack, itemStack)) {
                if (oldStack.getCount() > itemStack.getCount()) {
                    removed = removed.copy();
                    removed.shrink(itemStack.getCount());
                } else if (oldStack.getCount() < itemStack.getCount()) {
                    added = added.copy();
                    added.shrink(oldStack.getCount());
                    removed = null;
                } else {
                    removed = added = null;
                }
            }

            this.inventoryHandler.onChangeInventory(this, slot, InventoryOperation.setInventorySlotContents, removed, added);

            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return this.maxSize > 64 ? 64 : this.maxSize;
    }

    @Override
    public void markDirty() {
        if (this.inventoryHandler != null && this.eventsEnabled()) {
            this.inventoryHandler.onChangeInventory(this, -1, InventoryOperation.markDirty, ItemStack.EMPTY, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    public void markDirty(int slotIndex) {
        if (this.inventoryHandler != null && this.eventsEnabled()) {
            this.inventoryHandler.onChangeInventory(this, slotIndex, InventoryOperation.markDirty, null, null);
        }
    }


    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public Iterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    public void setMaxStackSize(int s) {
        this.maxSize = s;
    }

    @Override
    public String getName() {
        return "internal";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}