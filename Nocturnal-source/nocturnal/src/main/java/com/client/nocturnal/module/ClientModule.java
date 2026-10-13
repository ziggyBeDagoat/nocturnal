package com.client.nocturnal.module;

/** A UI-toggleable client utility. Add behavior in dedicated implementations. */
public final class ClientModule {
    private final String name;
    private final String description;
    private boolean enabled;

    public ClientModule(String name, String description, boolean enabled) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public String name() { return name; }
    public String description() { return description; }
    public boolean enabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
}
