package com.deadshotmdf.GLCBank.Objects.Top;

import java.util.UUID;

public record PlayerData(String playerName, UUID playerUUID, double bankBalance, double currentBalance, double combinedBalance) {

}
