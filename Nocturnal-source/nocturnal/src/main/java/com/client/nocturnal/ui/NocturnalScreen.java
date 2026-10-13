package com.client.nocturnal.ui;

import com.client.nocturnal.module.ClientModule;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Nocturnal's actual in-game ClickGUI. */
public final class NocturnalScreen extends Screen {
    public enum Tab { COMBAT, MOVEMENT, RENDER, PLAYER, WORLD, CONFIGS }

    private static final int BACKDROP = 0xB9080911;
    private static final int PANEL = 0xFF11131D;
    private static final int PANEL_LIGHT = 0xFF1B1E2B;
    private static final int PANEL_HOVER = 0xFF28263D;
    private static final int PURPLE = 0xFFB79CFF;
    private static final int CYAN = 0xFF70E7F2;
    private static final int WHITE = 0xFFF2F3FA;
    private static final int MUTED = 0xFF9298AD;

    private final Map<Tab, List<ClientModule>> modules = new EnumMap<>(Tab.class);
    private Tab selected = Tab.RENDER;
    private int px, py, pw, ph;
    private int scroll;

    public NocturnalScreen() {
        super(Component.literal("Nocturnal"));
        for (Tab tab : Tab.values()) modules.put(tab, new ArrayList<>());
        add(Tab.COMBAT, "Aim Assist", "Targeting helper placeholder", false);
        add(Tab.COMBAT, "Trigger Assist", "Interaction helper placeholder", false);
        add(Tab.MOVEMENT, "Sprint", "Remember sprint preference", false);
        add(Tab.MOVEMENT, "Coordinates HUD", "Display player coordinates", true);
        add(Tab.RENDER, "Fullbright", "Client brightness preference", false);
        add(Tab.RENDER, "Entity ESP", "Overlay renderer placeholder", false);
        add(Tab.RENDER, "Array List", "Show enabled module list", true);
        add(Tab.PLAYER, "Auto Eat", "Inventory helper placeholder", false);
        add(Tab.PLAYER, "Totem Alert", "Inventory alert placeholder", false);
        add(Tab.WORLD, "Block Outline", "World overlay placeholder", false);
        add(Tab.CONFIGS, "Accent: Purple", "Click to switch purple / cyan accent", false);
    }

    private void add(Tab tab, String name, String description, boolean enabled) {
        modules.get(tab).add(new ClientModule(name, description, enabled));
    }

    private int accent() {
        return modules.get(Tab.CONFIGS).get(0).enabled() ? CYAN : PURPLE;
    }

    @Override
    protected void init() {
        pw = Math.min(760, width - 32);
        ph = Math.min(470, height - 32);
        px = (width - pw) / 2;
        py = (height - ph) / 2;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        g.fill(0, 0, width, height, BACKDROP);
        shadow(g, px + 4, py + 5, px + pw + 4, py + ph + 5, 0x50000000);
        g.fill(px, py, px + pw, py + ph, PANEL);
        g.fill(px, py, px + pw, py + 3, accent());

        // Header
        g.drawString(font, "N O C T U R N A L", px + 22, py + 18, WHITE, false);
        g.drawString(font, "CLIENT  /  1.21.11", px + 22, py + 32, MUTED, false);
        String close = "ESC  CLOSE";
        g.drawString(font, close, px + pw - font.width(close) - 20, py + 24, MUTED, false);
        g.fill(px + 16, py + 52, px + pw - 16, py + 53, 0xFF292B3A);

        // Sidebar
        int sideX = px + 16;
        int sideY = py + 68;
        g.drawString(font, "WORKSPACE", sideX + 8, sideY, MUTED, false);
        sideY += 20;
        for (Tab tab : Tab.values()) {
            int top = sideY;
            boolean active = selected == tab;
            boolean hover = inside(mouseX, mouseY, sideX, top, sideX + 146, top + 29);
            if (active) {
                g.fill(sideX, top, sideX + 146, top + 29, 0xFF29243B);
                g.fill(sideX, top + 4, sideX + 3, top + 25, accent());
            } else if (hover) {
                g.fill(sideX, top, sideX + 146, top + 29, PANEL_HOVER);
            }
            g.drawString(font, tab.name(), sideX + 12, top + 10, active ? accent() : WHITE, false);
            sideY += 34;
        }

        int contentX = px + 184;
        int contentRight = px + pw - 18;
        int contentWidth = contentRight - contentX;
        g.drawString(font, title(selected), contentX, py + 69, WHITE, false);
        g.drawString(font, "Toggle a module to change its state", contentX, py + 84, MUTED, false);

        List<ClientModule> entries = modules.get(selected);
        int rowY = py + 110 - scroll;
        for (ClientModule module : entries) {
            int rowBottom = rowY + 49;
            if (rowBottom >= py + 105 && rowY <= py + ph - 42) {
                boolean hover = inside(mouseX, mouseY, contentX, rowY, contentRight, rowBottom);
                g.fill(contentX, rowY, contentRight, rowBottom, hover ? PANEL_HOVER : PANEL_LIGHT);
                g.fill(contentX, rowY + 8, contentX + 2, rowY + 41,
                        module.enabled() ? accent() : 0xFF3A3D4C);
                g.drawString(font, module.name(), contentX + 13, rowY + 9, WHITE, false);
                g.drawString(font, module.description(), contentX + 13, rowY + 25, MUTED, false);
                drawToggle(g, contentRight - 51, rowY + 15, module.enabled());
            }
            rowY += 57;
        }

        g.fill(px + 16, py + ph - 31, px + pw - 16, py + ph - 30, 0xFF292B3A);
        g.drawString(font, "NOCTURNAL", px + 22, py + ph - 20, accent(), false);
        String footer = "RIGHT SHIFT  •  MENU";
        g.drawString(font, footer, px + pw - font.width(footer) - 22, py + ph - 20, MUTED, false);
        super.render(g, mouseX, mouseY, delta);
    }

    private void drawToggle(GuiGraphics g, int x, int y, boolean on) {
        int color = on ? accent() : 0xFF454858;
        g.fill(x, y, x + 37, y + 19, color);
        int knobX = on ? x + 21 : x + 3;
        g.fill(knobX, y + 3, knobX + 13, y + 16, 0xFFF8F8FF);
    }

    private void shadow(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        g.fill(x1, y1, x2, y2, color);
    }

    private boolean inside(double mx, double my, int x1, int y1, int x2, int y2) {
        return mx >= x1 && mx < x2 && my >= y1 && my < y2;
    }

    private String title(Tab tab) {
        return switch (tab) {
            case COMBAT -> "Combat";
            case MOVEMENT -> "Movement";
            case RENDER -> "Render";
            case PLAYER -> "Player";
            case WORLD -> "World";
            case CONFIGS -> "Configs & Appearance";
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int sideX = px + 16;
        int sideY = py + 88;
        for (Tab tab : Tab.values()) {
            if (inside(mouseX, mouseY, sideX, sideY, sideX + 146, sideY + 29)) {
                selected = tab;
                scroll = 0;
                return true;
            }
            sideY += 34;
        }
        int contentX = px + 184;
        int contentRight = px + pw - 18;
        int rowY = py + 110 - scroll;
        for (ClientModule module : modules.get(selected)) {
            if (inside(mouseX, mouseY, contentX, rowY, contentRight, rowY + 49)) {
                module.toggle();
                return true;
            }
            rowY += 57;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int max = Math.max(0, modules.get(selected).size() * 57 - (ph - 160));
        scroll = Math.max(0, Math.min(max, scroll - (int) (verticalAmount * 30)));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 || keyCode == 344) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
