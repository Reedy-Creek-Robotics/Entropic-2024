package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TeloOpMain extends OpMode {

    Robot robot;

    double speed = 1;

    RobotContext robotContext;

    protected Controller driver;

    @Override
    public void init() {
        robot = new Robot(this);
        driver = new Controller(gamepad1);

        robot.init();
    }

    @Override
    public void loop() {
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !robot.getDriveTrain().isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            robot.getDriveTrain().drive(drive, strafe, turn, speed);
        }

        if (driver.isPressed(Controller.Button.DPAD_UP)){
            robot.getIntake().extend();
        } else if (driver.isPressed(Controller.Button.DPAD_DOWN)) {
            robot.getIntake().contract();
        }

        if (driver.isPressed(Controller.Button.TRIANGLE)){
            robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.TOP);
        } else if (driver.isPressed(Controller.Button.SQUARE)) {
            robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.PULL);
        }

        if (driver.rightTrigger() > 0.2){
            robot.getIntake().intake(1);
        } else if (driver.leftTrigger() > 0.2) {
            robot.getIntake().intake(-1);
        }else {
            robot.getIntake().intake(0);
        }

       /* if(driver.isPressed(Controller.Button.DPAD_RIGHT)) {
            switch (robot.getScoringSlide().getTarget()) {
                case GROUND:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.LOW_BASKET);
                    break;
                case LOW_BASKET:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.LOW_BAR);
                    break;
                case LOW_BAR:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
                    break;
                case HIGH_BAR:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    break;
                case HIGH_BASKET:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                    break;
            }

        } else if (driver.isPressed(Controller.Button.DPAD_LEFT)) {
            switch (robot.getScoringSlide().getTarget()) {
                case GROUND:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    break;
                case LOW_BASKET:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                    break;
                case LOW_BAR:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.LOW_BASKET);
                    break;
                case HIGH_BAR:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.LOW_BAR);
                    break;
                case HIGH_BASKET:
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
                    break;
            }
        }*/


        if(driver.isPressed(Controller.Button.LEFT_STICK_BUTTON)){
            speed = (speed == 1) ? 0.3 : 1;
        }
        telemetry.addData("left hang", robot.getLittleHanger().getLeftTicks());
        telemetry.addData("right hang", robot.getLittleHanger().getRightTicks());

        robot.update();
    }
}
