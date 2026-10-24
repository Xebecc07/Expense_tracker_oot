package com.expensetracker.core;

/**
 * INTERFACE: the contract every feature implements.
 * The default method run() gives plan gating to all implementations for free.
 */
public interface Insight {
    String getName();
    Feature getRequiredFeature();
    String generate(User user);

    default String run(User user) {
        if (!user.getPlan().allows(getRequiredFeature()))
            return "Locked: " + getName() + " is not in your " + user.getPlan().getName() + " plan. Upgrade to unlock.";
        return generate(user);
    }
}
