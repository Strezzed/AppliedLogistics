package com.fireball1725.firelib.render.obj;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class GroupObject {
    public String name;
    public ArrayList<Face> faces = new ArrayList<>();
    public int glDrawingMode;

    public GroupObject() {
        this("");
    }

    public GroupObject(String name) {
        this(name, -1);
    }

    public GroupObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @SideOnly(Side.CLIENT)
    public void render(int glDrawingMode) {
        if (!faces.isEmpty()) {
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(glDrawingMode, DefaultVertexFormats.POSITION_TEX);
            render(tessellator);
            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(Tessellator tessellator) {
        if (!faces.isEmpty()) {
            for (Face face : faces) {
                face.addFaceForRender(tessellator);
            }
        }
    }
}