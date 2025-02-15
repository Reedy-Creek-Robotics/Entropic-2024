package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.HorizontalSlide;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class commandTester extends OpMode {
    Intake intake;
    HorizontalSlide horizontalSlide;
    Controller controller;

    RobotContext context;
    @Override
    public void init() {
        controller = new Controller(gamepad1);
        context = BaseComponent.createRobotContext(this, RobotContext.Alliance.RED);

        horizontalSlide = new HorizontalSlide(context);
        intake = new Intake(context);

        horizontalSlide.init();
        intake.init();
    }

    @Override
    public void loop() {
        if (controller.isPressed(Controller.Button.TRIANGLE)){
            horizontalSlide.extend(0.39);
        } else if (controller.isPressed(Controller.Button.CROSS)) {
            horizontalSlide.contract(0);
        } else if (controller.isPressed(Controller.Button.CIRCLE)) {
            intake.timedIntake(1,500);
        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            intake.timedIntake(-0.3,80);
        }

        //TODO: make position of linkage finetunable

        intake.update();
    }
}
