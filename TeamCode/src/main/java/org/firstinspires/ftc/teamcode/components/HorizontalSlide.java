package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalSlide extends BaseComponent{

    public double CONTRACTED = 0;
    public double EXPANDED = 0.75;
    public double target = 0;

    public Servo servo;

    public HorizontalSlide(RobotContext context) {
        super(context);
        servo = hardwareMap.servo.get("HorizontalSlide");
    }


    @Override
    public void init(){
        servo.setPosition(this.target);
    }


    public void contract(){
        target = CONTRACTED;
        servo.setPosition(this.target);
    }

    public void expand(){
        target = EXPANDED;
        servo.setPosition(this.target);
    }

    public void toggle(){
        target = target == EXPANDED ? CONTRACTED : EXPANDED;
        servo.setPosition(this.target);
    }
}
