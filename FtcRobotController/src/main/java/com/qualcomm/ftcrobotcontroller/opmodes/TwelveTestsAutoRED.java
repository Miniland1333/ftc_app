package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Extras.AutoGamepad;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Henry on 1/21/2016.
 */
public class TwelveTestsAutoRED extends LinearOpMode {
    final String filename = "AutoRED.txt";

    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    Servo LClaw;
    Servo RClaw;
    Servo LHook;
    Servo RHook;

    Boolean isClawOpen;
    Boolean isHookOpen;
    int index =0;
    ArrayList<AutoGamepad> Recording=new ArrayList<AutoGamepad>();
    protected Context context;

    private ElapsedTime recordTime = new ElapsedTime();
    AutoGamepad Gamepad1 = new AutoGamepad();
    AutoGamepad Gamepad2 = new AutoGamepad();
    private boolean prevStateX = false;
    private boolean prevStateY = false;


    //Initalization Code
    private void initialize(){
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction

        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");
        LHook = hardwareMap.servo.get("LHook");
        RHook = hardwareMap.servo.get("RHook");

        context = FtcRobotControllerActivity.mainContext;

        //Motor and Servo Initialization
        makeClawOpen();
        makeHookOpen();

    }
    //Starting point for OpModes
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        recordTime.reset();
        Open();

        //Main Loop
        while (opModeIsActive()) {
            Play();

            LFront.setPower(Gamepad1.left_stick_y);
            LBack.setPower(Gamepad1.left_stick_y);
            RFront.setPower(Gamepad1.right_stick_y*.9);
            RBack.setPower(Gamepad1.right_stick_y*.9);
            BLift.setPower(-(Gamepad2.left_stick_y));

            //Special Functions
            TiltMotor();
            Hook();
            Claw();


            Telemetry();
        }
    }

    //Added functionality for Bucket Motor tilt
    private void TiltMotor(){
        final int MAX_DEGREES = 45;
        final int TOLERANCE_DEGREES = 5;
        final int SLOW_RANGE = 15;

        double motorValue = Gamepad2.right_stick_x*MAX_DEGREES;
        double currentValue= LBack.getCurrentPosition()/4f; //Bucket Encoder is on LBack
        double difference = Math.abs(motorValue-currentValue);
        if (difference<=TOLERANCE_DEGREES){Bucket.setPower(0);
        }
        else if (motorValue>currentValue){
            if (difference<=SLOW_RANGE) {Bucket.setPower(-.1);}
            else {Bucket.setPower(-.2);}
        }
        else{
            if (difference<=SLOW_RANGE){Bucket.setPower(.1);}
            else {Bucket.setPower(.2);}
        }
    }


    //Claw Servo controls
    private void Claw() {  //This class control the toggle to open/close the Claw
        if (!Gamepad2.x){prevStateX=false;}
        if (Gamepad2.x&&!prevStateX) {
            prevStateX=true;
            if (isClawOpen) {
                makeClawClosed();
            } else {
                makeClawOpen();
            }
        }

    }
    private void makeClawOpen(){
        RClaw.setPosition(0);
        LClaw.setPosition(1);
        isClawOpen = true;
    }
    private void makeClawClosed(){
        RClaw.setPosition(.7);
        LClaw.setPosition(.3);
        isClawOpen = false;
    }
    //Hook Servo Controls
    private void Hook() {  //This class control the toggle to open/close the Hook
        if (!gamepad2.y){
            prevStateY =false;}
        if (gamepad2.y&&!prevStateY) {
            prevStateY =true;
            if (isHookOpen) {
                makeHookClosed();
            } else {
                makeHookOpen();
            }
        }

    }
    private void makeHookOpen(){
        RHook.setPosition(.25);
        LHook.setPosition(.25);
        isHookOpen = true;
    }
    private void makeHookClosed(){
        RHook.setPosition(.5);
        LHook.setPosition(.5);
        isHookOpen = false;
    }
    //Autonomous Play/Open

    private void Play() {
        if (recordTime.time()>= .1 && index <= Recording.size() - 2) {
            recordTime.reset();
            Gamepad1 = Recording.get(index);
            index++;
            Gamepad2 = Recording.get(index);
            index++;
        }
    }
    private void Open() {
        try {
            FileInputStream inStream;
            ObjectInputStream in;
            inStream = context.openFileInput(filename);
            in = new ObjectInputStream(inStream);
            while(inStream.available()>0){Recording.add((AutoGamepad) in.readObject());}
            inStream.close();
            in.close();
            Recording.add(new AutoGamepad());
            Recording.add(new AutoGamepad());
            Log.i("Henry", "Opened Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //Customize telemetry data
    private void Telemetry(){
        telemetry.addData("Left",Gamepad1.left_stick_y);
        telemetry.addData("Right",Gamepad1.right_stick_y);
        telemetry.addData("Bucket",LBack.getCurrentPosition()/4);
        telemetry.addData("Record size",String.format("%d/%d",index,Recording.size()));
    }
}