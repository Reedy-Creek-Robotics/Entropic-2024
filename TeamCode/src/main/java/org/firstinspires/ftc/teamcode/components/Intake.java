package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake extends BaseComponent{

    double startLinkagePos = 0.15;
    double endLinkagePos = 0.75;
    double startRotatorPos = 0.55;
    double endRotatorPos = 0;
    double startRotator2Pos = 0.62;
    double endRotator2Pos = 0;
    double linkagePos = startLinkagePos;
    double rotatorPos = startRotatorPos;
    double rotator2Pos = startRotator2Pos;

    public CRServo rightServo;
    public CRServo leftServo;
    public Servo rotator;
    public Servo rotator2;
    public Servo linkage;

    private int targetPosition;
    public Intake(RobotContext context) {
        super(context);
        rightServo = hardwareMap.crservo.get("RightIntake");
        leftServo = hardwareMap.crservo.get("LeftIntake");
        rotator = hardwareMap.servo.get("Rotator");
        rotator2 = hardwareMap.servo.get("Rotator2");
        linkage = hardwareMap.servo.get("Linkage");
    }


    @Override
    public void init(){
        telemetry.addLine("intake initialized");
        rotator.setPosition(rotatorPos);
        rotator2.setPosition(rotator2Pos);
        linkage.setPosition(linkagePos);
    }

    public void intake(double power){
        rightServo.setPower(power);
        leftServo.setPower(-power);
    }

    public void extend(){
        telemetry.addLine("intake extend");
        rotatorPos = endRotatorPos;
        rotator2Pos = endRotator2Pos;
        linkagePos = endLinkagePos;
        rotator.setPosition(rotatorPos);
        rotator2.setPosition(rotator2Pos);
        linkage.setPosition(linkagePos);

    }
    public void contract(){
        telemetry.addLine("intake contract");
        rotatorPos = startRotatorPos;
        rotator2Pos = startRotator2Pos;
        linkagePos = startLinkagePos;
        rotator.setPosition(rotatorPos);
        rotator2.setPosition(rotator2Pos);
        linkage.setPosition(linkagePos);
    }


    public double getRotatorPos(){
        return rotatorPos;
    }
    public double getRotator2Pos(){
        return rotator2Pos;
    }
    public double getLinkagePos(){
        return linkagePos;
    }

}
