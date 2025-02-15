package org.firstinspires.ftc.teamcode.bareBones;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;

@Config
@Autonomous
public class RecordReader extends OpMode {

    public static String FILE_NAME = "Recorder.txt";

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(this, RobotContext.Alliance.RED);
    }

    @Override
    public void loop() {
        telemetry.addLine(robot.loadTimingRecord(FILE_NAME));
        telemetry.update();
    }
}
