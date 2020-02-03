/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; //The robot

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick; //The controller
// import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //The VictorSPX motor controllers
import edu.wpi.first.wpilibj.TimedRobot; //The class that a user program is based on -- not much other info is given
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //used for driving differential drive/skid-steer drive platforms
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C; //the I2C port on the Roborio
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;

public class Robot extends TimedRobot { 
  private SpeedControllerGroup leftMotors; //Creates the object for the left motors
  private SpeedControllerGroup rightMotors; //Creates the object for the right motors
  // private Servo servo;
  private DifferentialDrive driveTrain; //Creates the object for the drivetrain

  private Gamepad controller; //Creates the object for the contoller
  private Joystick buttonPanel;

  private IntakeConveyer intakeConveyer;
  private IntakeSolenoid intakeSolenoid;
  private WinchNLift winchNLift;
  private Compressor compressor;
  private ColorSensorCode colorSensorCode;
  
  private SimpleAuto simpleAuto;
  private AdvancedAuto1 advancedAuto1;
  private MjpegServer mjpegServer1;
  private MjpegServer mjpegServer2;
  private UsbCamera usbCamera;
  private CvSink cvSink;
  private Timer timer; //creates timer
  private final String currentAuto = "Advanced1";

  @Override
  public void robotInit() {

    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4)); // assigns the left motors on CAN 1 and CAN 4
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3)); // assigns the right motors on CAN 2 and CAN 3
    driveTrain = new DifferentialDrive(leftMotors, rightMotors); // makes the drivetrain a differential drive made of the left and right motors
    compressor = new Compressor();
   
    controller = new Gamepad(new Joystick(0)); // Creates the controller on USB 0
    buttonPanel = new Joystick(1); // Creat2es the button panel on USB 1

    winchNLift = new WinchNLift(new WPI_VictorSPX(9), new WPI_VictorSPX(10), new WPI_VictorSPX(7), controller);  // assigns the winch motors on CAN 9 and CAN 10
    intakeConveyer = new IntakeConveyer(new WPI_VictorSPX(6), new WPI_VictorSPX(8), buttonPanel, new DigitalInput(0), new DigitalInput(1), new DigitalInput(2));
    intakeSolenoid = new IntakeSolenoid(new DoubleSolenoid(2, 3), controller);
    colorSensorCode = new ColorSensorCode(new WPI_VictorSPX(5), buttonPanel, new ColorSensorV3(I2C.Port.kOnboard));
    // Creates UsbCamera and MjpegServer [1] and connects them
    usbCamera = new UsbCamera("USB Camera 1", 1);
    mjpegServer1 = new MjpegServer("serve_USB Camera 1", 1181);
    mjpegServer1.setSource(usbCamera);
    // Creates the CvSink and connects it to the UsbCamera
    cvSink = new CvSink("opencv_USB Camera 1");
    cvSink.setSource(usbCamera);
    // Creates the CvSource and MjpegServer [2] and connects them
    CvSource outputStream = new CvSource("Blur", PixelFormat.kMJPEG, 640, 480, 30);
    mjpegServer2 = new MjpegServer("serve_Blur", 1182);
    mjpegServer2.setSource(outputStream);
   // SmartDashboard.;

    timer = new Timer(); //timer method for autonomous
    simpleAuto = new SimpleAuto(timer, driveTrain);
    advancedAuto1 = new AdvancedAuto1(driveTrain, timer, intakeConveyer);

    compressor.start();
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    driveTrain.tankDrive((controller.getLJoystickY()), (controller.getRJoystickY()));
    intakeConveyer.teleOpRun(); //method for intake and conveyer controls
    intakeSolenoid.teleOpRun(); //method for solenoid controls
    colorSensorCode.teleOpRun(); //method for color sensor
    winchNLift.teleOpRun();
    compressor.start();
    // servo.setAngle();
  }

@Override
  public void autonomousInit(){
    compressor.start();
    intakeSolenoid.extend();
    intakeSolenoid.stop();
    timer.reset();
    timer.start();
  }

  @Override
  public void disabledInit(){
    compressor.stop();
  }

  @Override
  public void autonomousPeriodic(){
    switch (currentAuto){
      case "Simple":
        simpleAuto.run();
        break;
      case "Advanced1":
        advancedAuto1.run();
        break;
    }
  }
}