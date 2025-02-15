package org.firstinspires.ftc.teamcode.components;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HorizontalSlide extends BaseComponent{

    public enum LinkagePos{
        START(0.93,0.03),
        FLIP(0.84,0.12),
        END(0.78,0.18);

        double left, right;
        LinkagePos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public enum RotatorPos{
        START(0.73,0.4),
        END(0.21,0.92);

        double left, right;
        RotatorPos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    double linkagePos = 0;
    double rotatorPos = 0;

    public Servo leftRotator;
    public Servo rightRotator;
    public Servo leftLinkage;
    public Servo rightLinkage;

    private static final double MIN_SAFE_LINKAGE_EXTENSION = 0.15;

    private int targetPosition;
    public HorizontalSlide(RobotContext context) {
        super(context);
        leftRotator = hardwareMap.servo.get("RotatorLeft");
        rightRotator = hardwareMap.servo.get("RotatorRight");
        leftLinkage = hardwareMap.servo.get("LinkageLeft");
        rightLinkage = hardwareMap.servo.get("LinkageRight");
    }


    @Override
    public void init(){
        telemetry.addLine("intake initialized");
    }

    public void start(){
        leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
        rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
    }

    public void extend(double length){
        rotatorPos = 1;
        leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
        rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));

        linkagePos = length;
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
        //executeCommand(new Extend(length));

       /* telemetry.addLine("intake extend");
        rotatorPos = endRotatorPos;
        rotator2Pos = endRotator2Pos;
        linkagePos = LinkagePos.END;

        leftLinkage.setPosition(linkagePos.left);
        rightLinkage.setPosition(linkagePos.right);

        leftRotator.setPosition(rotatorPos);
        rightRotator.setPosition(rotator2Pos);*/

    }
    public void contract(double length){
        rotatorPos = 0;
        leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
        rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));

        linkagePos = length;
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
        //executeCommand(new Contract(length));

        /*telemetry.addLine("intake contract");
        rotatorPos = startRotatorPos;
        rotator2Pos = startRotator2Pos;
        linkagePos = LinkagePos.START;


        leftRotator.setPosition(rotatorPos);
        rightRotator.setPosition(rotator2Pos);

        leftLinkage.setPosition(linkagePos.left);
        rightLinkage.setPosition(linkagePos.right);*/
    }


    //Convert percentage extension to servo position
    public double getLinkagePosFromPercent(double length, int side){
        if(side == 1){
            return LinkagePos.START.right + length*(LinkagePos.END.right - LinkagePos.START.right);
        } else if (side == -1) {
            return LinkagePos.START.left + length*(LinkagePos.END.left - LinkagePos.START.left);
        }
        return 0.5;
    }

    public double getRotatorPosFromPercent(double length, int side){
        if(side == 1){
            return RotatorPos.START.right + length*(RotatorPos.END.right - RotatorPos.START.right);
        } else if (side == -1) {
            return RotatorPos.START.left + length*(RotatorPos.END.left -RotatorPos.START.left);
        }
        return 0.5;
    }

    public void linkageMove(double length){
        linkagePos = length;
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
    }

    public void linkageContract(){
        linkagePos = 0;
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
    }

    public void linkageExtend(){
        linkagePos = 1;
        leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
        rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
    }

    public void rotatorContract(){
        rotatorPos = 0;
        leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
        rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));
    }

    public void rotatorExtend(){
        rotatorPos = 1;
        leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
        rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));
    }

    public double getRotatorPos(){
        return rotatorPos;
    }
    public double getLinkagePos(){
        return linkagePos;
    }


    public class Extend implements Command{
        ElapsedTime timer = new ElapsedTime();
        double length;
        public Extend(double length) {
            this.length = length;
        }
        @Override
        public void start() {
            telemetry.addLine("intake extend");
            linkagePos = length;
            telemetry.addData("leftLinkage Pos: ", getLinkagePosFromPercent(linkagePos,-1));
            telemetry.addData("rightLinkage Pos: ", getLinkagePosFromPercent(linkagePos,1));

            leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
            rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));

            timer.reset();
        }

        @Override
        public void stop() {
            if(length >= MIN_SAFE_LINKAGE_EXTENSION){
                rotatorPos = 1;
                telemetry.addData("leftRotator Pos: ", getRotatorPosFromPercent(rotatorPos,-1));
                telemetry.addData("rightRotator Pos: ", getRotatorPosFromPercent(rotatorPos,1));

                leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
                rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));
            }

        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= 10;
        }
    }

    public class Contract implements Command{
        ElapsedTime timer = new ElapsedTime();
        double length;

        public Contract(double length) {
            this.length = length;
        }

        @Override
        public void start() {
            telemetry.addLine("intake extend");
            rotatorPos = 0;

            if(length <= MIN_SAFE_LINKAGE_EXTENSION){
                leftRotator.setPosition(getRotatorPosFromPercent(rotatorPos,-1));
                rightRotator.setPosition(getRotatorPosFromPercent(rotatorPos,1));
            }
            timer.reset();
        }

        @Override
        public void stop() {
            linkagePos = 0;

            leftLinkage.setPosition(getLinkagePosFromPercent(linkagePos,-1));
            rightLinkage.setPosition(getLinkagePosFromPercent(linkagePos,1));
        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= 300;
        }
    }

}
