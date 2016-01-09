package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Henry on 1/9/2016.
 */
public class HenryAuto extends LinearOpMode{
    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor FLift;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    Servo LClaw;
    Servo RClaw;

    Boolean isClawOpen;


    //Initalization Code
    private void initialize(){
        //Twelve = hardwareMap.servo.get("Twelve");
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        RFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        RBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        FLift.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction

        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");


/*        one=hardwareMap.servo.get("one");
        two=hardwareMap.servo.get("two");
        three=hardwareMap.servo.get("three");
        four=hardwareMap.servo.get("four");
        five=hardwareMap.servo.get("five");
        six=hardwareMap.servo.get("six");*/

        //Motor and Servo Initialization
        makeClawOpen();

    }
    @Override
    public void runOpMode() throws InterruptedException{
        initialize();
        waitForStart();
        Bucket.setPower(0);
        task(.5, .5, 1100, "Forward");
        task(-.5, .5, 500, "Turn Left");
        task(.5, .5, 1100, "Forward");
        task(-.5, .5, 425, "Turn Left");
        task(.20, .20, 600, "Forward");
        task(0, 0, 2000,"Wait");
        makeClawClosed();
        telemetry.addData("Task", "Dump");



    }

    private void makeClawOpen(){
        RClaw.setPosition(.3);
        LClaw.setPosition(.7);
        isClawOpen = true;
    }
    private void makeClawClosed(){
        RClaw.setPosition(.7);
        LClaw.setPosition(.3);
        isClawOpen = false;
    }
    private void task (double left, double right, long time) throws InterruptedException{
        LBack.setPower(left);
        LFront.setPower(left);
        RBack.setPower(right);
        RFront.setPower(right);
        sleep(time);
    }
    private void task (double left, double right, long time,String Message) throws InterruptedException{
        LBack.setPower(left);
        LFront.setPower(left);
        RBack.setPower(right);
        RFront.setPower(right);
        sleep(time);
        telemetry.addData("Task", Message);
    }
}
