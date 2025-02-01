package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ScoringSlide extends BaseComponent{
    public static final int TARGET_REACHED_THRESHOLD = 5;

    public enum Positions {
        GROUND(0), //base position used for picking elements up
        //WALL_EDGE(100), //edge of the wall for grabbing it off of it from the observation zone
        LOW_BASKET(1650),//1486
        HIGH_BASKET(3000),
        /*UNDER_LOW_BAR(400),
        LOW_BAR(500),*/
        OVER_HIGH_BAR(1650),
        HIGH_BAR(1180);

        private final int ticks;

        Positions(int ticks){
            this.ticks = ticks;
        }
    }

    public DcMotorEx motor;

    public ScoringSlide(RobotContext context) {
        super(context);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("ScoringSlide");
    }

    private double idlePower = 0.4;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;
    Positions targetPos;


    @Override
    public void init(){
        targetPos = Positions.GROUND;

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void resetSlideTicks() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int getPosition() {
        return (motor.getCurrentPosition());
    }

    public void stopMotors() {
        if(motor.getCurrentPosition() >= 50){
            motor.setTargetPosition(motor.getCurrentPosition());
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(idlePower);
        }else{
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
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
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor.setPower(power);
            }else {
                motor.setTargetPosition(motor.getCurrentPosition());
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setPower(idlePower);
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
            motor.setTargetPosition(ticks);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            double power = ticks > motor.getCurrentPosition() ?
                    ascendingPower :
                    descendingPower;

            motor.setPower(power);
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
            return Math.abs(motor.getCurrentPosition() - ticks) <= TARGET_REACHED_THRESHOLD;
        }
    }
}
