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
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TeloOpMain extends OpMode {

    DriveTrain driveTrain;
    Intake intake;
    ScoringSlide scoringSlide;
    LittleHanger littleHanger;
    double speed = 1;

    RobotContext robotContext;

    protected Controller driver;

    @Override
    public void init() {
        robotContext = BaseComponent.createRobotContext(this);

        driveTrain = new DriveTrain(robotContext);
        intake = new Intake(robotContext);
        scoringSlide = new ScoringSlide(robotContext);
        littleHanger = new LittleHanger(robotContext);

        driver = new Controller(gamepad1);

        driveTrain.init();
        intake.init();
        scoringSlide.init();
        littleHanger.init();
    }

    @Override
    public void loop() {
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !driveTrain.isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            driveTrain.drive(drive, strafe, turn, speed);
        }

        if (driver.isPressed(Controller.Button.DPAD_UP)){
            intake.extend();
        } else if (driver.isPressed(Controller.Button.DPAD_DOWN)) {
            intake.contract();
        }

        if (driver.isPressed(Controller.Button.TRIANGLE)){
            littleHanger.moveToHeight(LittleHanger.HangHeights.TOP);
        } else if (driver.isPressed(Controller.Button.SQUARE)) {
            littleHanger.moveToHeight(LittleHanger.HangHeights.PULL);
        }

        if (driver.rightTrigger() > 0.2){
            intake.intake(1);
        } else if (driver.leftTrigger() > 0.2) {
            intake.intake(-1);
        }else {
            intake.intake(0);
        }

        if(driver.isPressed(Controller.Button.DPAD_RIGHT)) {
            switch (scoringSlide.getTarget()) {
                case GROUND:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.WALL_EDGE);
                    break;
                case WALL_EDGE:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.LOW_BASKET);
                    break;
                case LOW_BASKET:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.LOW_BAR);
                    break;
                case LOW_BAR:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.HIGH_BAR);
                    break;
                case HIGH_BAR:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    break;
                case HIGH_BASKET:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.GROUND);
                    break;
            }

        } else if (driver.isPressed(Controller.Button.DPAD_LEFT)) {
            switch (scoringSlide.getTarget()) {
                case GROUND:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    break;
                case WALL_EDGE:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.GROUND);
                    break;
                case LOW_BASKET:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.WALL_EDGE);
                    break;
                case LOW_BAR:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.LOW_BASKET);
                    break;
                case HIGH_BAR:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.LOW_BAR);
                    break;
                case HIGH_BASKET:
                    scoringSlide.moveToHeight(ScoringSlide.Positions.HIGH_BAR);
                    break;
            }
        }


        if(driver.isPressed(Controller.Button.LEFT_STICK_BUTTON)){
            speed = (speed == 1) ? 0.3 : 1;
        }


        driveTrain.update();
        intake.update();
        scoringSlide.update();
        littleHanger.update();
        telemetry.update();
    }
}
