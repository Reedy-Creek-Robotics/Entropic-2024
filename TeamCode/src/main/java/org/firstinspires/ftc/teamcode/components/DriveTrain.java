package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.RobotDescriptor.DriveTuner;
import static org.firstinspires.ftc.teamcode.components.RobotDescriptor.OdometryTuner;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.roadrunner.drive.ModifiedMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.DriveUtil;

import java.util.Arrays;
import java.util.List;

@SuppressLint("DefaultLocale")
public class DriveTrain extends BaseComponent {
    @Override
    public void update() {
        context.localizer.update();

        super.update();
    }

    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    private List<DcMotorEx> motors;

    private IMU imu;
    private VoltageSensor batteryVoltageSensor;

    public ModifiedMecanumDrive roadrunner;

    public static DriveTuner driveTuner;
    public static OdometryTuner odometryTuner;

    public ModifiedMecanumDrive getRoadrunner() {
        return roadrunner;
    }

    public DriveTrain(RobotContext context) {
        super(context);

        driveTuner = descriptor.DRIVE_TUNER;
        odometryTuner = descriptor.ODOMETRY_TUNER;

        //this.context.localizer = new StandardTrackingWheelLocalizer(hardwareMap, lastTrackingEncPositions, lastTrackingEncVels, odometryTuner);

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        // TODO: adjust the names of the following hardware devices to match your configuration
        /*
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                DriveConstants.LOGO_FACING_DIR, DriveConstants.USB_FACING_DIR));
        imu.initialize(parameters);
        */

        leftFront = hardwareMap.get(DcMotorEx.class, "FrontLeft");
        leftRear = hardwareMap.get(DcMotorEx.class, "BackLeft");
        rightRear = hardwareMap.get(DcMotorEx.class, "BackRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "FrontRight");

        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);

        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        roadrunner = new ModifiedMecanumDrive(
                context.localizer,
                motors,
                imu,
                batteryVoltageSensor,
                driveTuner
        );

        if (driveTuner.runUsingEncoder) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (driveTuner.runUsingEncoder && driveTuner.driveMotorVeloPid != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, driveTuner.driveMotorVeloPid);
        }
    }

    public MecanumDrive getRoadRunner(){
        return roadrunner;
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void drive(double drive, double strafe, double turn, double speedFactor) {
        DriveUtil.MotorPowers motorPowers = context.driveUtil.calculateWheelPowerForDrive(drive, strafe, turn, speedFactor);

        leftFront.setPower(motorPowers.frontLeft);
        leftRear.setPower(motorPowers.backLeft);
        rightFront.setPower(motorPowers.frontRight);
        rightRear.setPower(motorPowers.backRight);
    }

    public void driverRelative(double drive, double strafe, double turn, double speedFactor) {
        DriveUtil.MotorPowers motorPowers = context.driveUtil.calculateWheelPowerForDriverRelative(drive, strafe, turn, new Heading(Math.toDegrees(roadrunner.getPoseEstimate().getHeading())), speedFactor);

        // DriveUtil.MotorPowers motorPowers = context.driveUtil.calculateWheelPowerForDriverRelative(drive, strafe, turn, new Heading(), speedFactor);

        leftFront.setPower(motorPowers.frontLeft);
        leftRear.setPower(motorPowers.backLeft);
        rightFront.setPower(motorPowers.frontRight);
        rightRear.setPower(motorPowers.backRight);
    }

    public void followTrajectory(TrajectorySequence trajectory){
        executeCommand(new FollowTrajectory(trajectory));
    }

    public void followTrajectoryAlt(TrajectorySequence trajectory){
        roadrunner.followTrajectorySequence(trajectory);
    }

    public TrajectorySequenceBuilder trajectoryBuilder(Pose2d start){
        return roadrunner.trajectorySequenceBuilder(start);
    }

    public class FollowTrajectory implements Command {
        private TrajectorySequence trajectory;

        public FollowTrajectory(TrajectorySequence trajectory) {
            this.trajectory = trajectory;
        }

        @Override
        public void start() {
            roadrunner.followTrajectorySequenceAsync(trajectory);
        }

        @Override
        public void stop() {

        }

        @Override
        public boolean update() {
            roadrunner.update();
            telemetry.addData("rr:",!roadrunner.isBusy());

            return !roadrunner.isBusy();
        }
    }
}
