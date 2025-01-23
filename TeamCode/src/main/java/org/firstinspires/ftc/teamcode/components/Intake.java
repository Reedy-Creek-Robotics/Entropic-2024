package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends BaseComponent{

    public enum LinkagePos{
        START(0.9,0.04),
        END(0.55,0.29);

        double left, right;
        LinkagePos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public enum RotatorPos{
        START(0,0),
        END(0,0);

        double left, right;
        RotatorPos(double left, double right) {

        }
    }

    double startRotatorPos = 0.75;
    double endRotatorPos = 0.2;
    double startRotator2Pos = 0.1;
    double endRotator2Pos = 0.65;

    LinkagePos linkagePos = LinkagePos.START;


    double rotatorPos = startRotatorPos;
    double rotator2Pos = startRotator2Pos;

    public CRServo rightServo;
    public CRServo leftServo;
    public Servo leftRotator;
    public Servo rightRotator;
    public Servo leftLinkage;
    public Servo rightLinkage;

    private int targetPosition;
    public Intake(RobotContext context) {
        super(context);
        rightServo = hardwareMap.crservo.get("RightIntake");
        leftServo = hardwareMap.crservo.get("LeftIntake");
        leftRotator = hardwareMap.servo.get("RotatorLeft");
        rightRotator = hardwareMap.servo.get("RotatorRight");
        leftLinkage = hardwareMap.servo.get("LinkageLeft");
        rightLinkage = hardwareMap.servo.get("LinkageRight");
    }


    @Override
    public void init(){
        telemetry.addLine("intake initialized");
        leftRotator.setPosition(rotatorPos);
        rightRotator.setPosition(rotator2Pos);
        leftLinkage.setPosition(linkagePos.left);
        rightLinkage.setPosition(linkagePos.right);
    }

    public void intake(double power){
        rightServo.setPower(power);
        leftServo.setPower(-power);
    }

    public void timedIntake(double power, double time){
        executeCommand(new TimedIntake(power, time));
    }

    public void extend(){
        telemetry.addLine("intake extend");
        rotatorPos = endRotatorPos;
        rotator2Pos = endRotator2Pos;
        linkagePos = LinkagePos.END;


        leftRotator.setPosition(rotatorPos);
        rightRotator.setPosition(rotator2Pos);

        leftLinkage.setPosition(linkagePos.left);
        rightLinkage.setPosition(linkagePos.right);

    }
    public void contract(){
        telemetry.addLine("intake contract");
        rotatorPos = startRotatorPos;
        rotator2Pos = startRotator2Pos;
        linkagePos = LinkagePos.START;
        leftRotator.setPosition(rotatorPos);
        rightRotator.setPosition(rotator2Pos);

        leftLinkage.setPosition(linkagePos.left);
        rightLinkage.setPosition(linkagePos.right);
    }


    public double getRotatorPos(){
        return rotatorPos;
    }
    public double getRotator2Pos(){
        return rotator2Pos;
    }
    public LinkagePos getLinkagePos(){
        return linkagePos;
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
