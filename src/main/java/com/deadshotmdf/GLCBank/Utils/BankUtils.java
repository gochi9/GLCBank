package com.deadshotmdf.GLCBank.Utils;

import com.deadshotmdf.GLCBank.ConfigSettings;

import java.util.UUID;

public class BankUtils {

    public static  double getInterest(long playtime){
        if (playtime <= 0)
            return ConfigSettings.getInterestMin();

        if (playtime >= ConfigSettings.getInterestMax())
            return ConfigSettings.getInterestMax();

        double min = ConfigSettings.getInterestMin(), max = ConfigSettings.getInterestMax();

        if(min == max)
            return max;

        double percentage = (double) playtime / ConfigSettings.getMinutesPlayedForMax();
        return min + (percentage * (max - min));
    }

    public static UUID getUUID(String s){
        try{
            return UUID.fromString(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static Double getDouble(String s){
        try{
            return Double.parseDouble(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

}
