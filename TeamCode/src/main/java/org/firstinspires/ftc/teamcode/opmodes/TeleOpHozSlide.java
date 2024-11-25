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
import org.firstinspires.ftc.teamcode.game.Controller;
import org.firstinspires.ftc.teamcode.geometry.Heading;

@TeleOp
public class TeleOpHozSlide extends OpMode {

    HorizontalSlide horizontalSlide;

    RobotContext robotContext;

    protected Controller driver;

    @Override
    public void init() {
        robotContext = BaseComponent.createRobotContext(this);

        horizontalSlide = new HorizontalSlide(robotContext);

        driver = new Controller(gamepad1);

        horizontalSlide.init();
    }

    @Override
    public void loop() {
        if(driver.isPressed(Controller.Button.CIRCLE)) {
            horizontalSlide.contract();
        } else if (driver.isPressed(Controller.Button.CROSS)){
            horizontalSlide.expand();
        }

        horizontalSlide.update();
    }
}
