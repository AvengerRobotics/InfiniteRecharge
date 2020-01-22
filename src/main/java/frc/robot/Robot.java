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
import edu.wpi.first.wpilibj.buttons.JoystickButton; //The A B Y and X buttons
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
  private SpeedControllerGroup intakeMotors; //Creates the object for the intake motors
  private SpeedControllerGroup winchMotors; //Creates the object for the winch motors
  private WPI_VictorSPX WoFMotor; //Creates the object for the WoF motors
  private WPI_VictorSPX liftMotor; //Creates the object for the lift motors
  private DifferentialDrive driveTrain; //Creates the object for the drivetrain
  private Joystick controller; //Creates the object for the contoller

  //private WPI_VictorSPX intakeMotor; //Creates the object for the intake motor
  private JoystickButton buttonA; //Creates a container to receive a boolean from the A Button
  private JoystickButton buttonB; //Creates a container to receive a boolean from the B Button
  private int intakeMotorsValue; //Creating a variable for the speed of the intake motor controller
  private int liftMotorValue; //Creating a variable for the speed of the lift motor controller
  private JoystickButton buttonX; //Creates a container to receive a boolean from the X Button
  private JoystickButton buttonY; //Creates a container to receive a boolean from the Y Button
  private JoystickButton buttonLb; //Creates a container to receive a boolean form the LB button
  private JoystickButton buttonRb; //Creates a container to receive a boolean form the RB button
  //private WPI_VictorSPX conveyerMotor; //creates the object for the conveyer
  //private int conveyerMotorValue;

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
  private boolean isSpinActive;

  Timer timer;


  @Override
  public void robotInit() {

    compressor = new Compressor(11);
    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4));
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3));
    intakeMotors = new SpeedControllerGroup(new WPI_VictorSPX(8), new WPI_VictorSPX(6));
    winchMotors = new SpeedControllerGroup(new WPI_VictorSPX(9), new WPI_VictorSPX(10));
    liftMotor = new WPI_VictorSPX(7); // assigns liftMotor to CAN 7
    WoFMotor = new WPI_VictorSPX(5); // assigns WoFMotor to CAN 8

    driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    // Creates the controller on USB 0
    controller = new Joystick(0);
    
    buttonA = new JoystickButton(controller, 1); //Assigns the buttonA variable to the A button on Controller 0
    buttonB = new JoystickButton(controller, 2); //Assigns the buttonB variable to the B button on Controller 0
    buttonX = new JoystickButton(controller, 3); //Assigns button X on the controller
    buttonY = new JoystickButton(controller, 4); //Assigns button Y on the controller
    buttonLb = new JoystickButton(controller, 5); //left bumper of the controller
    buttonRb = new JoystickButton(controller, 6); //right bumper of the controller

    timer = new Timer();

    // adds colors to the colormatcher
    colorMatcher.addColorMatch(kBlueTarget);
    colorMatcher.addColorMatch(kGreenTarget);
    colorMatcher.addColorMatch(kRedTarget);
    colorMatcher.addColorMatch(kYellowTarget);
  }

  @Override
  public void teleopPeriodic() {

    compressor.start();

    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    driveTrain.tankDrive((controller.getRawAxis(1)*-1), (controller.getRawAxis(5)*-1));
    
    // controller.getPOV();
    // if(controller.getPOV() > 45 && controller.getPOV() < 135){ 
    //   WoFMotor.set(1);
    // //If the A button is pressed, then the conveyor and intake motors speed will be set to -1
    // } else if(controller.getPOV() > 225 && controller.getPOV() < 315){ 
    //   WoFMotor.set(-1);
    //   //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
    // } else{
    //   WoFMotor.set(0);
    // }

    //solenoid controls
    if(controller.getPOV() > 135 && controller.getPOV() < 225){ 
      intakeSolenoid.set(DoubleSolenoid.Value.kForward);
    //If the A button is pressed, then the conveyor and intake motors speed will be set to -1
    } else if(controller.getPOV() > 315 && controller.getPOV() < 45){ 
      intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
      //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
    } else{
      intakeSolenoid.set(DoubleSolenoid.Value.kOff);
    }


    //winch motor controls
    if (buttonB.get()){
      winchMotors.set(-1*controller.getRawAxis(3));
    } else {
      winchMotors.set(controller.getRawAxis(3));
    }

    if (buttonB.get()){
      winchMotors.set(-1*controller.getRawAxis(3));
    } else {
      winchMotors.set(controller.getRawAxis(3));
    }
    
    /*Converts a boolean to a 1 or 0 based on the A button state
    if(buttonA.get() == true){ 
      intakeMotorValue = 1;   
    // Converts a boolean to a -1 or 0 based on the B button state
    } if(bButton.get() == true){ 
      intakeMotorValue = (-1);   //sets the motor to run in reverse when B is pressed
    //The motor power will be sat to 0 when both A and B are not pressed
    } else if(buttonA.get() == false && bButton.get() == false){
      intakeMotorValue = 0;
    //Sets the intake motor speed to the variable intakeMotorValue
    intakeMotors.set(intakeMotorValue); 


    //Sets the motor value based on the value of buttonX and buttonY
    if(buttonX.get() == true){ 
      conveyerMotorValue = 1;
    } if(buttonY.get() == true){
      conveyerMotorValue = (-1);
    } else if(buttonX.get() == false && buttonY.get() == false){
      conveyerMotorValue = 0;
    }
    */

    //If the Y button is pressed, then the conveyor and intake motors speed will be set to 1
    if(buttonY.get() == true){ 
      intakeMotorsValue = 1;
    //If the A button is pressed, then the conveyor and intake motors speed will be set to -1
    } else if(buttonA.get() == true){
      intakeMotorsValue = (-1);
      //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
    } else if(buttonY.get() == false && buttonA.get() == false){
      intakeMotorsValue = 0;
    }
    //Sets the intake motors speed to the variable intakeMotorsValue..
    
    intakeMotors.set(intakeMotorsValue);
   
    if(buttonLb.get() == true){  //If the LB Button is pressed, then the lift motor speed will be set to 1
      liftMotorValue = 1;
    } else if(buttonRb.get() == true){ //If the RB Button is pressed, then the lift motor speed will be set to -1
      liftMotorValue = -1;
    } else if(buttonLb.get() == false && buttonRb.get() == false){  //If neither LB or RB button is pressed, then the lift motor speed will be set to 0
      liftMotorValue = 0;
    }
    liftMotor.set(liftMotorValue); //Sets the lift motor speed to the variable liftMotorValue

    //color sensor code
    Color detectedColor = m_colorSensor.getColor();
    String colorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

    if (buttonX.get()) { // when the x button is clicked, it turns on the WoFMotor, then resets 
      WoFMotor.set(1);
      colorChanges = 0;
      previousColor = "Unknown";
      isSpinActive = true;
    }
    if (isSpinActive) { //it checks if the WoFMotor is still spinning due to the X button
      if (!colorString.equals(previousColor)){ // if the color changes, it increases the number of color changes by one
        colorChanges++;
        previousColor = colorString;
      }
      if (colorChanges >= 32){ // if the color has changed more than 25 times, it will stop the motor
        WoFMotor.set(0);
        isSpinActive = false;
      }
    }
  }

  @Override
  public void disabledInit() {
    compressor.stop();
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