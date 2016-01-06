package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;

/**
 * Created by Staff on 1/4/2016.
 */
public class JamesTeleopProgram extends LinearOpMode{
    DcMotor RFront;
    DcMotor RBack;
    DcMotor LFront;
    DcMotor LBack;
    DcMotor BLift;
    DcMotor FLift;
    DcMotor Bucket;
    Servo LClaw;
    Servo RClaw;
    Boolean toggle = true;
    int targetPosition = 0;

    private void MotorInit() {
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
        LClaw.setPosition(0.7);
        RClaw.setPosition(0.3);
        //sets up servos
        LBack.setDirection(DcMotor.Direction.REVERSE);
        LFront.setDirection(DcMotor.Direction.REVERSE);
        FLift.setDirection(DcMotor.Direction.REVERSE);
        //inverts things that need inverting

        //sets up servos properly with math
    }
    @Override
    public void runOpMode()throws InterruptedException{
        MotorInit();
        waitForStart();
        while (opModeIsActive()) {
            LFront.setPower(gamepad1.left_stick_y);
            LBack.setPower(gamepad1.left_stick_y);
            RFront.setPower(gamepad1.right_stick_y);
            RBack.setPower(gamepad1.right_stick_y);
            //provides control for driver
            BLift.setPower(gamepad2.right_stick_y);
            if (Math.abs(gamepad2.right_stick_y)>.2){
                FLift.setPower(gamepad2.right_stick_y*0.4);
            }
            else{
                Flift.setPowerFloat();
            }
            Tilt();
            BumperToggle();
            //provides control for co-pilot
        }
    }
    private void BumperToggle() throws InterruptedException{
        if (gamepad2.right_bumper) {
            do{
                waitForNextHardwareCycle();
            }while (gamepad2.right_bumper);
            //makes it wait until button is depressed, to prevent it from looping and thus spazzing out.
            if (toggle) {
                LClaw.setPosition(0.7);
                RClaw.setPosition(0.3);
                toggle=false;
                //If claw is open, closes it and alerts boolean "toggle" that it is.
            }else {
                LClaw.setPosition(0.3);
                RClaw.setPosition(0.7);
                toggle=true;
                //if claw is closed, opens it and alerts boolean "toggle" that it is.
            }
        }
    }
    private void Tilt() throws InterruptedException{
        double motorValue = gamepad2.left_stick_x*85;
        double currentValue = LBack.getCurrentPosition()/4f;//Bucket Encoder on Port S2, like LBack
        if (motorValue > currentValue) {
            Bucket.setPower(-0.2);
        }else{
            Bucket.setPower(0.2);
        }
    }
}
