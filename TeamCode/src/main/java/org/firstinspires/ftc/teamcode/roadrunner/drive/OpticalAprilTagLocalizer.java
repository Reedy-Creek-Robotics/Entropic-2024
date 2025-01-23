package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotContext;

public class OpticalAprilTagLocalizer implements Localizer {

    AprilTagLocalizer aprilTag;
    OpticalLocalizer optical;
    private Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);
    Pose2d position = new Pose2d(0,0,0);

    public OpticalAprilTagLocalizer(RobotContext context, AprilTagLocalizer aprilTagLocalizer, OpticalLocalizer opticalLocalizer) {
        //aprilTag = new AprilTagLocalizer(cameraPosition, cameraOrientation, context.getWebcam());
        this.aprilTag = aprilTagLocalizer;
        this.optical = opticalLocalizer;
    }

    @Override
    public void update() {
        aprilTag.update();
        optical.update();

        position = optical.getPoseEstimate();

        if(aprilTag.hasGoodDetection()){
            position = aprilTag.getPoseEstimate();
            optical.setPoseEstimate(position);
        }
    }

    @NonNull
    @Override
    public Pose2d getPoseEstimate() {
        return position;
    }

    @Override
    public void setPoseEstimate(@NonNull Pose2d pose2d) {
        this.position = pose2d;
        optical.setPoseEstimate(pose2d);
        aprilTag.setPoseEstimate(pose2d);
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return optical.getPoseVelocity();
    }
}
