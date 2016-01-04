package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Test New Technology Servos
 * Created by James on 9/26/2015.
 */
public class Skynet extends LinearOpMode {
    DcMotor RFront;
    DcMotor RBack;
    DcMotor LFront;
    DcMotor LBack;
    DcMotor BLift;
    DcMotor FLift;
    DcMotor Bucket;
    Servo LClaw;
    Servo RClaw;

    private void Doctor() {
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        BLift = hardwareMap.dcMotor.get("BLift");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        //sets up DC motors
        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");
        //sets up servos
        RBack.setDirection(DcMotor.Direction.REVERSE);
        RFront.setDirection(DcMotor.Direction.REVERSE);
        Bucket.setDirection(DcMotor.Direction.REVERSE);
        //inverts things that need inverting

        //sets up servos properly with math
                                             //Sets things up
    }
    @Override
    public void runOpMode() throws InterruptedException {
        Doctor();
        waitForStart();

        LBack.setPower(0.5);
        LFront.setPower(0.5);
        RFront.setPower(0.5);
        RBack.setPower(0.5);
        sleep(2000);
        //drives robot forwards 2 full seconds
        LBack.setPower(1.0);
        LFront.setPower(1.0);
        RFront.setPower(-1.0);
        RBack.setPower(-1.0);
        sleep(5000);
        //makes the robot do a cool clockwise donut for 5 full seconds
        LBack.setPower(-1.0);
        LFront.setPower(-1.0);
        RFront.setPower(1.0);
        RBack.setPower(1.0);
        sleep(5000);
        //makes the robot do a cool counterclockwise donut for 5 full seconds
        LBack.setPower(-0.5);
        LFront.setPower(-0.5);
        RFront.setPower(-0.5);
        RBack.setPower(-0.5);
        sleep(2000);
        //drives robot backwards 2 full seconds
        LBack.setPower(0.0);
        LFront.setPower(0.0);
        RFront.setPower(0.0);
        RBack.setPower(0.0);
        sleep(1);
        //stops everything
    }

}