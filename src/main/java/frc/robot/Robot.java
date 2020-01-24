/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; //The robot

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.Joystick; //The controller
import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //The VictorSPX motor controllers
import edu.wpi.first.wpilibj.TimedRobot; //The class that a user program is based on -- not much other info is given
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //used for driving differential drive/skid-steer drive platforms

import edu.wpi.first.wpilibj.I2C; //the I2C port on the Roborio
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot { 
  private SpeedControllerGroup leftMotors; //Creates the object for the left motors
  private SpeedControllerGroup rightMotors; //Creates the object for the right motors
  private SpeedControllerGroup winchMotors; //Creates the object for the winch motors
  private WPI_VictorSPX controlPanelMotor; //Creates the object for the WoF motors
  private WPI_VictorSPX liftMotor; //Creates the object for the lift motors
  private WPI_VictorSPX conveyerMotor; //creates the object for the conveyer
  private WPI_VictorSPX intakeMotor; //Creates the object for the intake motor
  private DifferentialDrive driveTrain; //Creates the object for the drivetrain
  private Gamepad controller; //Creates the object for the contoller

  private int liftMotorValue; //Creating a variable for the speed of the lift motor controller
  private DoubleSolenoid intakeSolenoid = new DoubleSolenoid(1, 2); //Creates the new double solenoids for the intake.
  private Compressor compressor;

  private final I2C.Port i2cPort = I2C.Port.kOnboard; //Change the I2C port below to match the connection of your color sensor
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort); 
  private final ColorMatch colorMatcher = new ColorMatch();
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  private String previousColor;
  private int colorChanges;
  private String currentColor;
  private Color detectedColor;
  private boolean isSpinActive;
  private ColorMatchResult match;

  private Timer timer;

  @Override
  public void robotInit() {

    compressor = new Compressor(11);
    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4)); // assigns the left motors on CAN 1 and CAN 4
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3)); // assigns the right motors on CAN 2 and CAN 3
    winchMotors = new SpeedControllerGroup(new WPI_VictorSPX(9), new WPI_VictorSPX(10)); // assigns the winch motors on CAN 9 and CAN 10
    intakeMotor = new WPI_VictorSPX(8); // intake motor is assigned on CAN 8
    conveyerMotor = new WPI_VictorSPX(6); // conveyer motor will be assigned on CAN 6
    liftMotor = new WPI_VictorSPX(7); // assigns liftMotor to CAN 7
    controlPanelMotor = new WPI_VictorSPX(5); // assigns controlPanelMotor to CAN 8
    driveTrain = new DifferentialDrive(leftMotors, rightMotors); // makes the drivetrain a differential drive made of the left and right motors
   
    // Creates the controller on USB 0
    controller = new Gamepad(new Joystick(0));

    timer = new Timer();

    // adds colors to the colormatcher
    colorMatcher.addColorMatch(kBlueTarget);
    colorMatcher.addColorMatch(kGreenTarget);
    colorMatcher.addColorMatch(kRedTarget);
    colorMatcher.addColorMatch(kYellowTarget);
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    driveTrain.tankDrive((controller.getLJoystickY()), (controller.getRJoystickY()));

    //color sensor code - current color is stored in currentColor
    detectedColor = m_colorSensor.getColor();
    match = colorMatcher.matchClosestColor(detectedColor);
    if (match.color == kBlueTarget) {
      currentColor = "Blue";
    } else if (match.color == kRedTarget) {
      currentColor = "Red";
    } else if (match.color == kGreenTarget) {
      currentColor = "Green";
    } else if (match.color == kYellowTarget) {
      currentColor = "Yellow";
    } else {
      currentColor = "Unknown";
    }

    if(controller.getDPadAngle() > 45 && controller.getDPadAngle() < 135){ 
      controlPanelMotor.set(1);
    //If the A button is pressed, then the control panel motor will be set to -1
    } else if(controller.getDPadAngle() > 225 && controller.getDPadAngle() < 315){ 
      controlPanelMotor.set(-1);
      //If neither A or Y button is pressed, then the control panel motor will be set to 0
    } else if (!isSpinActive){ // prevent the motor from being set to 0 if it is currently spinning
      controlPanelMotor.set(0);
    }

    /*
    if (controller.getX()){ // when the X button is clicked, it turns on the WoFMotor, then resets 
      controlPanelMotor.set(1);
      colorChanges = 0;
      previousColor = "Unknown";
      isSpinActive = true;
    }
    if (isSpinActive) { //it checks if the WoFMotor is still spinning due to the X button
      if (!currentColor.equals(previousColor)){ // if the color changes, it increases the number of color changes by one
        colorChanges++;
        previousColor = currentColor;
      }
      if (colorChanges >= 32){ // if the color has changed more than 32 times, it will stop the motor
        controlPanelMotor.set(0);
        isSpinActive = false;
      }
    }
    */

    //solenoid controls
    if(controller.getDPadAngle() > 135 && controller.getDPadAngle() < 225){ 
      intakeSolenoid.set(DoubleSolenoid.Value.kForward);
    //If the up of the dpad is pressed, then the conveyor and intake motors speed will be set to -1
    } else if(controller.getDPadAngle() > 315 && controller.getDPadAngle() < 45){ 
      intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
    } else{
      intakeSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    //winch motor controls
    if (controller.getB()){
      winchMotors.set(controller.getRT());
    } else {
      winchMotors.set(controller.getRT() * -1);
    }
    
    // Converts a boolean to a 1 or 0 based on the A button state
    if(controller.getA()){ 
      intakeMotor.set(1);  
    // Converts a boolean to a -1 or 0 based on the Y button state
    } if(controller.getY()){ 
      intakeMotor.set(-1);   //sets the motor to run in reverse when Y is pressed
    } else { //The motor power will be sat to 0 when neither button is pressed
      intakeMotor.set(0);
    }

    //Sets the motor value based on the value of the X button and the B button
    if(controller.getX()){
      conveyerMotor.set(1);
    } if(controller.getB()){
      conveyerMotor.set(-1);
    } else {
      conveyerMotor.set(0);
    }
   
    if(controller.getLB()){  //If the LB Button is pressed, then the lift motor speed will be set to 1
      liftMotorValue = 1;
    } else if(controller.getRB()){ //If the RB Button is pressed, then the lift motor speed will be set to -1
      liftMotorValue = -1;
    } else if(!controller.getRB() && !controller.getLB()){  //If neither LB or RB button is pressed, then the lift motor speed will be set to 0
      liftMotorValue = 0;
    } else {
      liftMotor.set(liftMotorValue); //Sets the lift motor speed to the variable liftMotorValue
    }
  }

  @Override
  public void autonomousInit(){
    timer.reset();
    timer.start();
  }

  @Override
  public void autonomousPeriodic(){
    if (timer.get() < 7){
      driveTrain.tankDrive(0.6,0.6);
    } else { 
      driveTrain.stopMotor();
    }
  }
}