/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; //The robot

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick; //The controller
//import edu.wpi.first.wpilibj.Servo; //servo controls
import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //The VictorSPX motor controllers
import edu.wpi.first.wpilibj.TimedRobot; //The class that a user program is based on -- not much other info is given
import edu.wpi.first.wpilibj.Timer; //timer for auton
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //used for driving differential drive/skid-steer drive platforms
import edu.wpi.first.wpilibj.I2C; //the I2C port on the Roborio
import com.revrobotics.ColorSensorV3; //color sensor

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.Compressor; //compressor
import edu.wpi.first.wpilibj.DigitalInput; //proximity sensors
import edu.wpi.first.cameraserver.CameraServer; //USB camera
//import edu.wpi.first.vision.VisionRunner;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; //SmartDashboard 

//  import edu.wpi.cscore.CvSink;
//  import edu.wpi.cscore.CvSource;
//  import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
// import edu.wpi.cscore.VideoMode.PixelFormat;

public class Robot extends TimedRobot { 
  private SpeedControllerGroup leftMotors; //Creates the object for the left motors
  private SpeedControllerGroup rightMotors; //Creates the object for the right motors
  //private Servo servo;
  private DifferentialDrive driveTrain; //Creates the object for the drivetrain

  private Gamepad controller; //Creates the object for the contoller
  private Joystick buttonPanel; //button panel with joystick

  private IntakeConveyer intakeConveyer; //class for the intake conveyor controls
  private IntakeSolenoid intakeSolenoid;//class for the intake cylinder controls
  private WinchNLift winchNLift;//class for the controls for the winch lift
  private Compressor compressor;// class to control the solenoid
  private ColorSensorCode colorSensorCode;//class for color sensor code

  private SendableChooser<String> autoChoice;//for choosing the autonomous in the driver station
  private SimpleAuto simpleAuto;//simple testing autonomous
  private AdvancedAuto1 advancedAuto1;//advanced autonomous code that dumps balls
  // private GripPipeline gripPipeline;
  private VisionThread visionThread;
  // private MjpegServer mjpegServer2;
  private UsbCamera usbCamera;
  // private CvSink cvSink;
  private Timer timer; //creates timer
  private String currentAuto;//autonomous that is being run

  private static final int IMG_WIDTH = 320;
  private static final int IMG_HEIGHT = 240;
  private double centerX = 0.0;

  private final Object imgLock = new Object();

  @Override
  public void robotInit() {

    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4)); // assigns the left motors on CAN 1 and CAN 4
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3)); // assigns the right motors on CAN 2 and CAN 3
    driveTrain = new DifferentialDrive(leftMotors, rightMotors); // makes the drivetrain a differential drive made of the left and right motors
    compressor = new Compressor();//creates the compressor as an object
   
    controller = new Gamepad(new Joystick(0)); // Creates the controller on USB 0
    buttonPanel = new Joystick(1); // Creates the button panel on USB 1

    winchNLift = new WinchNLift(new WPI_VictorSPX(8), new WPI_VictorSPX(9), controller);  // assigns the lift motors on CAN 8 and CAN 9 - 8 is lift1 and 9 is lift2
    intakeConveyer = new IntakeConveyer(new WPI_VictorSPX(6), new WPI_VictorSPX(7), buttonPanel, new DigitalInput(0), new DigitalInput(1), new DigitalInput(2), new DoubleSolenoid(2, 3), controller);//creates the intake conveyor as an object 
    //intakeSolenoid = new IntakeSolenoid(new DoubleSolenoid(2, 3), controller);//creates the cylinder for the intake as an object
    colorSensorCode = new ColorSensorCode(new WPI_VictorSPX(5), buttonPanel, new ColorSensorV3(I2C.Port.kOnboard));//Assigns the WoF motor to mprt 5 and the I2C port on the roborio to the color sensor
    usbCamera = CameraServer.getInstance().startAutomaticCapture("camera_serve_0", 1); // adds a source to the cameraserver from the camera on port 1
    usbCamera.setResolution(320, 240);

  //   visionThread = new VisionThread(usbCamera, new VisionContour(), pipeline -> {
  //     if (!pipeline.filterContoursOutput().isEmpty()) {
  //         Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
  //         synchronized (imgLock) {
  //             centerX = r.x + (r.width / 2);
  //         }
  //     }
  // });
  //    visionThread.start();

    // usbCamera2 = CameraServer.getInstance().startAutomaticCapture("camera_serve_0", 0); // adds a source to the cameraserver from the camera on port 

    
    // // Creates the CvSink and connects it to the UsbCamera
    // cvSink = new CvSink("opencv_USB Camera 1");
    // cvSink.setSource(usbCamera);
    // // Creates the CvSource and MjpegServer [2] and connects them
    // CvSource outputStream = new CvSource("Blur", PixelFormat.kMJPEG, 640, 480, 30);

    // mjpegServer2 = new MjpegServer("serve_Blur", 1182);
    // mjpegServer2.setSource(outputStream);

    //SmartDashboard.putRaw(usbCamera);

    timer = new Timer(); //timer method for autonomous
    simpleAuto = new SimpleAuto(timer, driveTrain); //autonomous method for simple auton
    advancedAuto1 = new AdvancedAuto1(driveTrain, timer, intakeConveyer);  //autonomous method for advanced auton

   // compressor.start(); //starts compressor in initialization
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    driveTrain.tankDrive((controller.getLJoystickY()), (controller.getRJoystickY()));
    intakeConveyer.teleOpRun(); //method for intake and conveyer controls
   // intakeSolenoid.teleOpRun(); //method for solenoid controls
    colorSensorCode.teleOpRun(); //method for color sensor
    winchNLift.teleOpRun(); //method for winch and lift controls
    compressor.start(); //starts compressor in the start of teleop
  
    // servo.setAngle();
  }

@Override
  public void autonomousInit(){
    compressor.start();//turns on compressor when the robot is initialized in autonomous
    //intakeSolenoid.extend();
    //intakeSolenoid.stop();
    timer.reset();
    timer.start();
    currentAuto = autoChoice.getSelected();
  }

  @Override
  public void disabledInit(){
    compressor.stop(); //stops compressor when robot is disabled
  }

  @Override
  public void autonomousPeriodic(){
    // gripPipeline.process();
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