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
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;
import org.firstinspires.ftc.teamcode.geometry.Heading;

@TeleOp
public class TeloOpMain extends OpMode {

    DriveTrain driveTrain;
    Hooker hooker;
    Intake intake;

    RobotContext robotContext;

    protected Controller driver;

    @Override
    public void init() {
        robotContext = BaseComponent.createRobotContext(this);

        driveTrain = new DriveTrain(robotContext);
        hooker = new Hooker(robotContext);
        intake = new Intake(robotContext);

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

            driveTrain.drive(drive, strafe, turn, 0.5);
        }

        if (driver.isPressed(Controller.Button.DPAD_UP)){
            intake.moveToTicks(intake.getPosition() + 100);
        } else if (driver.isPressed(Controller.Button.DPAD_DOWN)) {
            intake.moveToTicks(intake.getPosition() - 100);
        }

       /* if (driver.isPressed(Controller.Button.TRIANGLE)){
            hooker.moveToTicks(hooker.getPosition() + 10);
        } else if (driver.isPressed(Controller.Button.SQUARE)) {
            hooker.moveToTicks(hooker.getPosition() - 10);
        }*/

        if (driver.isPressed(Controller.Button.RIGHT_BUMPER)){
            intake.intake(1);
        } else if (driver.isPressed(Controller.Button.LEFT_BUMPER)) {
            intake.intake(-1);
        }else {
            intake.intake(0);
        }

        hooker.rotate(driver.rightTrigger()-driver.leftTrigger());


        driveTrain.update();
        hooker.update();
        intake.update();
    }
}