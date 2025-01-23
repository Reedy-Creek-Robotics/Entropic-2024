package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.HorizontalSlide;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

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
