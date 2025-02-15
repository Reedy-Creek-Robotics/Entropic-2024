package org.firstinspires.ftc.teamcode.components;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ParkingStick extends BaseComponent{
    public Servo stick;
    public double pos;
    public static double extendedPos = 1;
    public static double contractedPos = 0;

    public ParkingStick(RobotContext context) {
        super(context);
        stick = hardwareMap.servo.get("ParkingStick");
    }

    @Override
    public void init() {
        pos = 0;
    }

    public void extend(){
        pos = extendedPos;
        stick.setPosition(pos);
    }

    public void contract() {
        pos = contractedPos;
        stick.setPosition(pos);
    }

    public boolean getExtended(){
        return pos == extendedPos;
    }
}
