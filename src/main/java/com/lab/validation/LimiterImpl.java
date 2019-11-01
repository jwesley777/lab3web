package com.lab.validation;

public class LimiterImpl implements Limiter {

    public boolean isInLimits(double x, double y, double r) {
        if (x <= -5 || x >= 3) {
            return false;
        }
        if (!(y==-4 || y==-3 || y==-2 || y==-1 || y==0 || y==1 || y==2 || y==3 || y==4)) {
            return false;
        }
        if (!(r==1 || r==2 || r==3 || r==4 || r==5)) {
            return false;
        }
        return true;
    }

}