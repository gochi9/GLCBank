package com.deadshotmdf.GLCBank.Objects.Sign;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.SignGUIFinishHandler;
import de.rapha149.signgui.SignGUIResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SignActionFinish implements SignGUIFinishHandler {

    private static final List<SignGUIAction> EMPTY = Collections.emptyList();

    private final GLCB main;
    private final ModifyType modifyType;

    public SignActionFinish(GLCB main, ModifyType modifyType) {
        this.main = main;
        this.modifyType = modifyType;
    }

    @Override
    public List<SignGUIAction> onFinish(Player player, SignGUIResult result) {
        Double amount = BankUtils.getDouble(result.getLineWithoutColor(0));

        if(amount == null){
            player.sendMessage(ConfigSettings.getInvalidAmount());
            return EMPTY;
        }

        return List.of(SignGUIAction.runSync(main, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bank consoledepositwithdraw " + player.getName() + " " + amount + " " + modifyType)));
    }

}
