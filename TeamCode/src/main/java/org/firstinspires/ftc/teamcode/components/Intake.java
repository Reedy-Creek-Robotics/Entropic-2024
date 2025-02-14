package org.firstinspires.ftc.teamcode.components;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake extends BaseComponent{
    public CRServo rightServo;
    public CRServo leftServo;
    public ColorSensor colorSensor;
    float[] hsvValues = {0F,0F,0F};
    final float[] values = hsvValues;

    public Intake(RobotContext context) {
        super(context);
        rightServo = hardwareMap.crservo.get("RightIntake");
        leftServo = hardwareMap.crservo.get("LeftIntake");
        colorSensor = hardwareMap.colorSensor.get("ColorSensor");
    }

    @Override
    public void init() {

    }

    public void intake(double power){
        rightServo.setPower(power);
        leftServo.setPower(-power);
    }

    public void timedIntake(double power, double time){
        executeCommand(new TimedIntake(power, time));
    }

    public float[] getHSVValues(){
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);
        return hsvValues;
    }

    public double getDistance(){
        return ((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM);
    }
    public double getLightDetected(){
        return ((OpticalDistanceSensor) colorSensor).getLightDetected();
    }

    public void setLED(boolean enabled){
        colorSensor.enableLed(enabled);
    }

    public class TimedIntake implements Command {

        double power,time;

        ElapsedTime timer;

        public TimedIntake(double power, double time) {
            this.power = power;
            this.time = time;
        }

        @Override
        public void start() {
            intake(power);
            timer = new ElapsedTime();
        }

        @Override
        public void stop() {
            intake(0);
        }

        @Override
        public boolean update() {
            return timer.milliseconds() > time;
        }
    }
}
