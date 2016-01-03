package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Staff on 1/3/2016.
 */
public class Teleop2016 extends LinearOpMode {

    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor FLift;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    private void initialize(){
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        FLift.setDirection(DcMotor.Direction.REVERSE);   //Reversing based on default motor rotation direction


    }
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while (opModeIsActive()) {
            LBack.setPower(gamepad1.left_stick_y);
            LFront.setPower(gamepad1.left_stick_y);
            RBack.setPower(gamepad1.right_stick_y);
            RFront.setPower(gamepad1.right_stick_y);
            //These all control driving



        }
    }
}
