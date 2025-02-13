package org.firstinspires.ftc.teamcode.bareBones;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.MachineVisionSubmersible;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TeleOp
public class MVTest extends OpMode {

    protected Controller driver;
    RobotContext robotContext;
    MachineVisionSubmersible mvs;

    List<List<Integer>> counts;
    List<Double> totals;

    @Override
    public void init() {

        driver = new Controller(gamepad1);
        robotContext = BaseComponent.createRobotContext(this);
        robotContext.setAlliance(RobotContext.Alliance.BLUE);
        mvs = new MachineVisionSubmersible(robotContext);

        mvs.init();
    }

    @Override
    public void loop() {

        if(driver.isPressed(Controller.Button.CROSS)){

            counts = mvs.getElementCounts();
            telemetry.addData("region0 team: ", counts.get(0).get(0));
            telemetry.addData("region1 team: ", counts.get(0).get(1));
            telemetry.addData("region2 team: ", counts.get(0).get(2));
            telemetry.addData("region0 yellow: ", counts.get(1).get(0));
            telemetry.addData("region1 yellow: ", counts.get(1).get(1));
            telemetry.addData("region2 yellow: ", counts.get(1).get(2));

            totals = new ArrayList<>();
            totals.add(counts.get(0).get(0) + counts.get(1).get(0) * 1.1);
            totals.add(counts.get(0).get(1) + counts.get(1).get(1) * 1.1);
            totals.add(counts.get(0).get(2) + counts.get(1).get(2) * 1.1);

            telemetry.addData("totals: ", totals);
            telemetry.addData("Best Region: ", totals.indexOf(Collections.max(totals)));

        }



        telemetry.update();

    }
}
