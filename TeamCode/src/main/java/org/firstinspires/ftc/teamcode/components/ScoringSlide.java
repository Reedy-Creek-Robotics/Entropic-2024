package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ScoringSlide extends BaseComponent{
    public static final int TARGET_REACHED_THRESHOLD = 10;

    public enum Positions {
        GROUND(0), //base position used for picking elements up
        //WALL_EDGE(100), //edge of the wall for grabbing it off of it from the observation zone
        LOW_BASKET(1650),//1486
        HIGH_BASKET(3050),
        /*UNDER_LOW_BAR(400),
        LOW_BAR(500),*/
        OVER_HIGH_BAR(1650),
        HIGH_BAR(1100);

        private final int ticks;

        Positions(int ticks){
            this.ticks = ticks;
        }
    }

    public DcMotorEx leftMotor, rightMotor;

    public ScoringSlide(RobotContext context) {
        super(context);
        leftMotor = (DcMotorEx) hardwareMap.dcMotor.get("LeftScoringSlide");
        rightMotor = (DcMotorEx) hardwareMap.dcMotor.get("RightScoringSlide");
    }

    private double idlePower = 0.4;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;
    Positions targetPos;


    @Override
    public void init(){
        targetPos = Positions.GROUND;

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void resetSlideTicks() {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int getPosition() {
        return ((leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition())/2);
    }

    public void stopMotors() {
        if(leftMotor.getCurrentPosition() >= 50){
            leftMotor.setTargetPosition(getPosition());
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setPower(idlePower);

            rightMotor.setTargetPosition(getPosition());
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setPower(idlePower);
        }else{
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftMotor.setPower(0);

            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setPower(0);
        }
    }

    public void moveToHeight(Positions angle) {
        targetPos = angle;
        moveToTicks(angle.ticks);
    }

    public Positions getTarget(){
        return targetPos;
    }

    public void rotate(double power){
        if(!isBusy()){
            if(power != 0){
                leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftMotor.setPower(power);

                rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightMotor.setPower(power);
            }else {
                stopMotors();
            }
        }
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
        //ticks = ensureSafeTicks(ticks);
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
            leftMotor.setTargetPosition(ticks);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            rightMotor.setTargetPosition(ticks);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double power = ticks > leftMotor.getCurrentPosition() ?
                    ascendingPower :
                    descendingPower;

            leftMotor.setPower(power);
            rightMotor.setPower(power);
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
            return Math.abs(getPosition() - ticks) <= TARGET_REACHED_THRESHOLD;
        }
    }
}
