package org.firstinspires.ftc.teamcode.roadrunner.drive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotContext;

public class OpticalAprilTagLocalizer extends OpticalLocalizer {

    AprilTagLocalizer aprilTag;
    private Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    public OpticalAprilTagLocalizer(RobotContext context) {
        super(context);

        aprilTag = new AprilTagLocalizer(cameraPosition, cameraOrientation, context.getWebcam());
    }

    @Override
    public void update() {
        super.update();
        aprilTag.update();
    }
}
