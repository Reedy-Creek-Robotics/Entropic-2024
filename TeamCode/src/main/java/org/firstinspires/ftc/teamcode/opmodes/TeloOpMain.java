package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_TRIGGER;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Hooker;
import org.firstinspires.ftc.teamcode.components.HorizontalSlide;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.game.Controller;
import org.firstinspires.ftc.teamcode.geometry.Heading;

@TeleOp
public class TeloOpMain extends OpMode {

    DriveTrain driveTrain;
    Hooker hooker;
    Intake intake;
    HorizontalSlide horizontalSlide;
    ScoringSlide scoringSlide;
    double speed = 1;

    RobotContext robotContext;

    protected Controller driver;

    @Override
    public void init() {
        robotContext = BaseComponent.createRobotContext(this);

        driveTrain = new DriveTrain(robotContext);
        hooker = new Hooker(robotContext);
        intake = new Intake(robotContext);
        horizontalSlide = new HorizontalSlide(robotContext);
        scoringSlide = new ScoringSlide(robotContext);

        driver = new Controller(gamepad1);

        driveTrain.init();
        hooker.init();
        intake.init();
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
            intake.moveToTicks(intake.getPosition() - 50);
        } else if (driver.isPressed(Controller.Button.DPAD_DOWN)) {
            intake.moveToTicks(intake.getPosition() + 50);
        }

       /* if (driver.isPressed(Controller.Button.TRIANGLE)){
            hooker.moveToTicks(hooker.getPosition() + 10);
        } else if (driver.isPressed(Controller.Button.SQUARE)) {
            hooker.moveToTicks(hooker.getPosition() - 10);
        }*/

        if (driver.rightTrigger() > 0.2){
            intake.intake(1);
        } else if (driver.leftTrigger() > 0.2) {
            intake.intake(-1);
        }else {
            intake.intake(0);
        }

        if(driver.isPressed(Controller.Button.CIRCLE)) {
            horizontalSlide.contract();
        } else if (driver.isPressed(Controller.Button.CROSS)){
            horizontalSlide.expand();
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
        hooker.update();
        intake.update();
    }
}
