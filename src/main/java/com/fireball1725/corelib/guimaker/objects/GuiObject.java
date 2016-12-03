package com.fireball1725.corelib.guimaker.objects;

import com.fireball1725.corelib.guimaker.GuiMaker;
import com.fireball1725.corelib.network.PacketHandler;
import com.fireball1725.corelib.network.messages.PacketGuiObjectClicked;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiObject extends Gui implements IGuiObject {

    protected int x, y, w, h;

    protected int fX = 0, fY = 0;

    protected int guiX, guiY, guiW, guiH;

    protected int mouseX, mouseY;

    protected final int controlID;

    protected GuiMaker guiMakerObj;

    protected boolean visible = true;
    protected boolean disabled = false;
    protected boolean selected = false;

    public GuiObject(int controlID) {
        this.controlID = controlID;
    }

    public void updateGuiObject(GuiMaker guiMakerObj) {
        this.guiMakerObj = guiMakerObj;
    }

    public void updateGuiSize(int x, int y, int w, int h) {
        this.guiX = x;
        this.guiY = y;
        this.guiW = w;
        this.guiH = h;
    }

    public void updateForegroundSize(int x, int y) {
        this.fX = x;
        this.fY = y;
    }

    public void updateMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void guiObjectClicked() {
        PacketHandler.INSTANCE.sendToServer(new PacketGuiObjectClicked(guiMakerObj.getGuiId(), this.controlID));
    }

    public void guiObjectUpdated() {
        this.guiMakerObj.guiObjectUpdated(this.controlID);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(GuiContainer guiContainer, int mouseX, int mouseY, float partialTicks, float zLevel) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(GuiContainer guiContainer, int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiContainer guiContainer, float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void drawSlot(Slot slotIn) {

    }

    @Override
    public void mouseClicked(GuiContainer guiContainer, int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void onGuiClosed() {

    }

    @Override
    public void updateScreen() {

    }

    @Override
    public List<Slot> initContainer(InventoryPlayer player, IInventory inventory) {
        return new ArrayList<>();
    }

    protected void drawSlot(int x, int y, int w, int h) {
        x += this.guiX;
        y += this.guiY;

        GuiUtils.drawContinuousTexturedBox(GuiMaker.resourceLocation, x, y, 0, 16, w, h, 16, 16, 1, 0);
    }

    @Override
    public void handleMouseInput() {

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
