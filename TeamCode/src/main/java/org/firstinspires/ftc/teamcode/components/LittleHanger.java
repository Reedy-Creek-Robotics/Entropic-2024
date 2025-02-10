package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LittleHanger extends BaseComponent{
    public static final int TARGET_REACHED_THRESHOLD = 5;

    public enum HangHeights {
        TOP(6500),
        TOUCH(810),
        PULL(3500);

        private final int ticks;

        HangHeights(int ticks){
            this.ticks = ticks;
        }
    }

    public DcMotorEx leftHang;
    public DcMotorEx rightHang;

    private int targetPosition;

    private int initialPosition;

    public LittleHanger(RobotContext context) {
        super(context);
        leftHang = (DcMotorEx) hardwareMap.dcMotor.get("LeftHang");
        rightHang = (DcMotorEx) hardwareMap.dcMotor.get("RightHang");
    }

    private double idlePower = 1.0;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;


    @Override
    public void init(){
        targetPosition = 0;

        leftHang.setDirection(DcMotorSimple.Direction.FORWARD);
        leftHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftHang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftHang.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightHang.setDirection(DcMotorSimple.Direction.REVERSE);
        rightHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightHang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHang.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(int initialPosition) {
        this.initialPosition = initialPosition;
    }

    public void resetSlideTicks() {
        leftHang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int getTargetPosition() {
        return (targetPosition);
    }

    public int getPosition(){
        return ((leftHang.getCurrentPosition() + rightHang.getCurrentPosition())/2);
    }


    public void stopMotors() {
        leftHang.setTargetPosition(getPosition());
        leftHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftHang.setPower(idlePower);
        rightHang.setTargetPosition(getPosition());
        rightHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightHang.setPower(idlePower);
    }

    public void moveToHeight(HangHeights height) {
        moveToTicks(height.ticks);
    }

    public double getLeftTicks(){
        return leftHang.getCurrentPosition();
    }

    public double getRightTicks(){
        return rightHang.getCurrentPosition();
    }

    public void rotate(double power){
        if(!isBusy()){
            if(power != 0){
                leftHang.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftHang.setPower(power);
                rightHang.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightHang.setPower(power);
            }else {
                leftHang.setTargetPosition(leftHang.getCurrentPosition());
                leftHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftHang.setPower(idlePower);
                rightHang.setTargetPosition(rightHang.getCurrentPosition());
                rightHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightHang.setPower(idlePower);
            }
        }
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
        //ticks = ensureSafeTicks(ticks);
        this.targetPosition = ticks;
        //this.manualControl = false;
        stopAllCommands();
        executeCommand(new MoveToTicks(ticks));
    }

    private class MoveToTicks implements Command {
        private int ticks;

        //test

        public MoveToTicks(int ticks) {
            this.ticks = ticks;
        }

        @Override
        public void start() {
            leftHang.setTargetPosition(ticks);
            leftHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double leftPower = ticks > leftHang.getCurrentPosition() ?
                    ascendingPower :
                    descendingPower;

            rightHang.setTargetPosition(ticks);
            rightHang.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double rightPower = ticks > rightHang.getCurrentPosition() ?
                    ascendingPower :
                    descendingPower;

            leftHang.setPower(leftPower);
            rightHang.setPower(rightPower);
        }

        @Override
        public void stop() {
            stopMotors();

            //TODO implement
            /*if(ticks == 0){
                resetSlideTicks();
            }*/
        }

        @Override
        public boolean update() {
            return (Math.abs(leftHang.getCurrentPosition() - ticks) <= TARGET_REACHED_THRESHOLD) && (Math.abs(rightHang.getCurrentPosition() - ticks) <= TARGET_REACHED_THRESHOLD);
        }
    }
}
