package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends BaseComponent{
    public CRServo rightServo;
    public CRServo leftServo;

    public Intake(RobotContext context) {
        super(context);
        rightServo = hardwareMap.crservo.get("RightIntake");
        leftServo = hardwareMap.crservo.get("LeftIntake");
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
