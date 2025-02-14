package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.MachineVisionSubmersible;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TeleOp
public class ColorTester extends OpMode {

    protected Controller driver;
    RobotContext robotContext;
    Intake intake;
    boolean spinning = false;

    @Override
    public void init() {

        driver = new Controller(gamepad1);
        robotContext = BaseComponent.createRobotContext(this);
        robotContext.setAlliance(RobotContext.Alliance.BLUE);
        intake = new Intake(robotContext);
    }

    @Override
    public void loop() {
        float[] hsvValues = intake.getHSVValues();
        float hue = hsvValues[0];
        float saturation = hsvValues[1];
        float value = hsvValues[2];
        double distance = intake.getDistance();
        telemetry.addLine("Press CROSS to toggle intake");
        telemetry.addLine("Press SQUARE to reject if RED");
        telemetry.addLine("Press CIRCLE to reject if BLUE");
        telemetry.addData("intake spinning: ", spinning);
        telemetry.addLine("-----COLOR SENSOR-----");
        telemetry.addData("distance (mm): ", distance);
        telemetry.addData("hue: ", hue);
        telemetry.addData("saturation: ", saturation);
        telemetry.addData("value: ", value);
        telemetry.addLine("----------------------");

        if (driver.isPressed(Controller.Button.CROSS)) {
            if (spinning) {
                telemetry.addLine("Stopping Intake...");
                spinning = false;
                intake.intake(0);
            } else {
                telemetry.addLine("Starting Intake...");
                spinning = true;
                intake.intake(1);
            }
        }

        if (driver.isPressed(Controller.Button.SQUARE)) {
            if (distance < 50){
                telemetry.addLine("DISTANCE too far.");
            }
            else if(hue < 60 || hue > 330){
                telemetry.addLine("Rejecting RED...");
                intake.timedIntake(-1, 1000);
                intake.intake(spinning ? 1:0);
            } else {
                telemetry.addLine("RED not found.");
            }
        }

        if (driver.isPressed(Controller.Button.CIRCLE)){
            if (distance < 50){
                telemetry.addLine("DISTANCE too far.");
            }
            else if(hue > 160 && hue < 290){
                telemetry.addLine("Rejecting BLUE...");
                intake.timedIntake(-1, 1000);
                intake.intake(spinning ? 1:0);
            } else {
                telemetry.addLine("BLUE not found.");
            }
        }


        telemetry.update();

    }




}
