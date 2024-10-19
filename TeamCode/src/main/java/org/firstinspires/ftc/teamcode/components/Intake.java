package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Intake extends BaseComponent{

    public static final int TARGET_REACHED_THRESHOLD = 5;

    public enum Angles {
        GROUND(0);

        private final int ticks;

        Angles(int ticks){
            this.ticks = ticks;
        }
    }

    public CRServo rightServo;
    public CRServo leftServo;
    public DcMotorEx motor;

    private int targetPosition;
    public Intake(RobotContext context) {
        super(context);
        rightServo = hardwareMap.crservo.get("RightIntake");
        leftServo = hardwareMap.crservo.get("LeftIntake");
        motor = (DcMotorEx) hardwareMap.dcMotor.get("IntakeArm");
    }

    private double idlePower = 0.4;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;

    @Override
    public void init(){
        targetPosition = Angles.GROUND.ticks;

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void resetSlideTicks() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int getPosition() {
        return (motor.getCurrentPosition());
    }

    public void stopMotors() {
        motor.setTargetPosition(motor.getCurrentPosition());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(idlePower);
    }

    public void moveToHeight(Angles angle) {
        // If the height is at the small pole or above, also reset the deliver offset to move down.
        moveToTicks(angle.ticks);
    }

    public void intake(double power){
        rightServo.setPower(power);
        leftServo.setPower(-power);
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
