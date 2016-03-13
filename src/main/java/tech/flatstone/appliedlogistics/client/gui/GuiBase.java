/*
 *
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.common.container.slot.SlotBase;
import tech.flatstone.appliedlogistics.common.util.LogHelper;
import tech.flatstone.appliedlogistics.common.util.OverlayHelper;

public abstract class GuiBase extends GuiContainer {
    OverlayHelper overlayHelper = new OverlayHelper();

    public GuiBase(Container container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouse_x, int mouse_y, float btn) {
        super.drawScreen(mouse_x, mouse_y, btn);
    }

    public void drawTooltip(int par2, int par3, int forceWidth, String Msg) {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        String[] var4 = Msg.split("\n");
        if (var4.length > 0) {
            int var5 = 0;
            for (int var6 = 0; var6 < var4.length; var6++) {
                int var7 = this.fontRendererObj.getStringWidth(var4[var6]);
                if (var7 > var5) {
                    var5 = var7;
                }
            }
            int var6 = par2 + 12;
            int var7 = par3 - 12;
            int var9 = 8;
            if (var4.length > 1) {
                var9 += 2 + (var4.length - 1) * 10;
            }
            if (this.guiTop + var7 + var9 + 6 > this.height) {
                var7 = this.height - var9 - this.guiTop - 6;
            }
            if (forceWidth > 0) {
                var5 = forceWidth;
            }
            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int var10 = -267386864;
            drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 0xFEFEFE) >> 1 | var11 & 0xFF000000;
            drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);
            for (int var13 = 0; var13 < var4.length; var13++) {
                String var14 = var4[var13];
                if (var13 == 0) {
                    var14 = "§" + Integer.toHexString(15) + var14;
                } else {
                    var14 = "§7" + var14;
                }
                this.fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                if (var13 == 0) {
                    var7 += 2;
                }
                var7 += 10;
            }
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }
        GL11.glPopAttrib();
    }

    public abstract void drawBG(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract void drawFG(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public void bindTexture(String base, String file) {
        ResourceLocation resourceLocation = new ResourceLocation(base, "textures/" + file);
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    public void bindTexture(String file) {
        ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MOD_ID, "textures/" + file);
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    protected void drawItem(int x, int y, ItemStack itemStack) {
        this.zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        overlayHelper.drawTransparentItemStack(itemStack, x, y, itemRender);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    protected final void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawBG(ox, oy, x, y);
    }

    protected final void drawGuiContainerForegroundLayer(int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawFG(ox, oy, x, y);
    }

    protected Slot getSlot(int mouseX, int mouseY) {
        for (int j1 = 0; j1 < this.inventorySlots.inventorySlots.size(); j1++) {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(j1);
            if (isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public void drawSlot(Slot s) {
        try {
            ItemStack is = s.getStack();
            if (s instanceof SlotBase && ((SlotBase) s).renderIconWithItem()) {
                SlotBase aes = (SlotBase) s;
                drawItem(aes.defX, aes.defY, aes.overlayIcon);
            }
            if ((s instanceof SlotBase)) {
                ((SlotBase) s).isDisplay = true;
                safeDrawSlot(s);
            } else {
                safeDrawSlot(s);
            }
            return;
        } catch (Exception err) {
            safeDrawSlot(s);
        }
    }

    private void safeDrawSlot(Slot s) {
        try {
            super.drawSlot(s);
        } catch (Exception err) {
            LogHelper.error(err.getMessage());
        }
    }
}
