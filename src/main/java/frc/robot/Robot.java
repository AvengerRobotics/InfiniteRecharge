/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; //The robot

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick; //The controller
import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //The VictorSPX motor controllers
import edu.wpi.first.wpilibj.TimedRobot; //The class that a user program is based on -- not much other info is given
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //used for driving differential drive/skid-steer drive platforms
import edu.wpi.first.wpilibj.I2C; //the I2C port on the Roborio
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot { 
  private SpeedControllerGroup leftMotors; //Creates the object for the left motors
  private SpeedControllerGroup rightMotors; //Creates the object for the right motors
  // private SpeedControllerGroup winchMotors; //Creates the object for the winch motors
  //private WPI_VictorSPX liftMotor; //Creates the object for the lift motors
  private DifferentialDrive driveTrain; //Creates the object for the drivetrain

  private Gamepad controller; //Creates the object for the contoller
  private Joystick buttonPanel;

  //private int liftMotorValue; //Creating a variable for the speed of the lift motor controller

  private IntakeConveyer intakeConveyer;
  private IntakeSolenoid intakeSolenoid;
  private WinchNLift winchNLift;
  private Compressor compressor;
  private ColorSensorCode colorSensorCode;
  // private DigitalInput proximitySwitch1, proximitySwitch2, proximitySwitch3; //creates the proximity switches for conveyer and intake
  private Timer timer; //creates timer

  @Override
  public void robotInit() {

    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4)); // assigns the left motors on CAN 1 and CAN 4
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3)); // assigns the right motors on CAN 2 and CAN 3
    //liftMotor = new WPI_VictorSPX(7); // assigns liftMotor to CAN 7
    driveTrain = new DifferentialDrive(leftMotors, rightMotors); // makes the drivetrain a differential drive made of the left and right motors
    compressor = new Compressor();
   
    controller = new Gamepad(new Joystick(0)); // Creates the controller on USB 0
    buttonPanel = new Joystick(1); // Creat2es the button panel on USB 1

    winchNLift = new WinchNLift(new WPI_VictorSPX(9), new WPI_VictorSPX(10), new WPI_VictorSPX(7), controller);  // assigns the winch motors on CAN 9 and CAN 10
    intakeConveyer = new IntakeConveyer(new WPI_VictorSPX(6), new WPI_VictorSPX(8), buttonPanel, new DigitalInput(0), new DigitalInput(1), new DigitalInput(2));
    intakeSolenoid = new IntakeSolenoid(new DoubleSolenoid(2, 3), controller);
    colorSensorCode = new ColorSensorCode(new WPI_VictorSPX(5), buttonPanel, new ColorSensorV3(I2C.Port.kOnboard));

    timer = new Timer(); //timer method for autonomous

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
  }
    /* if(controller.getDPadAngle() > 45 && controller.getDPadAngle() < 135){ 
    //   controlPanelMotor.set(1);
    // //If the A button is pressed, then the control panel motor will be set to -1
    // } else if(controller.getDPadAngle() > 225 && controller.getDPadAngle() < 315){ 
    //   controlPanelMotor.set(-1);
    //   //If neither A or Y button is pressed, then the control panel motor will be set to 0
    // } else if (!isSpinActive){ // prevent the motor from being set to 0 if it is currently spinning
    //   controlPanelMotor.set(0);
    // } */
    /*
    // Converts a boolean to a 1 or 0 based on the A button state
    if (controller.getA()) { 
      intakeMotor.set(1);  
    // Converts a boolean to a -1 or 0 based on the Y button state
    } if(controller.getY()) { 
      intakeMotor.set(-1);   //sets the motor to run in reverse when Y is pressed
    } else { //The motor power will be sat to 0 when neither button is pressed
      intakeMotor.set(0);
    }
    */
    /*Sets the motor value based on the value of the X button and the B button
    // if (controller.getX()) {
    //   conveyerMotor.set(1);
    // } if (controller.getB()) {
    //   conveyerMotor.set(-1);
    // } else {
    //   conveyerMotor.set(0);
    // } */
/*
    // LiftControls(); //method for lift motor code below

    
   // WinchMotor(); //method for winch motor below
 // }

  // private void WinchMotor() {
  //   //winch motor controls
  //   if (controller.getB()){
  //     winchMotors.set(controller.getRT());
  //   } else {
  //     winchMotors.set(controller.getRT() * -1);
  //   }
  // }
  */
  /*
  // private void SolenoidControls() {
  //   //solenoid controls
  //   if(controller.getDPadAngle() > 135 && controller.getDPadAngle() < 225){ 
  //     intakeSolenoid.set(DoubleSolenoid.Value.kForward);
  //   //If the up of the dpad is pressed, then the conveyor and intake motors speed will be set to -1
  //   } else if(controller.getDPadAngle() > 315 && controller.getDPadAngle() < 45){ 
  //     intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
  //   //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
  //   } else{
  //     intakeSolenoid.set(DoubleSolenoid.Value.kOff);
  //   }
  // }
  */
/*
  // private void LiftControls() {
  //   if(controller.getLB()){  //If the LB Button is pressed, then the lift motor speed will be set to 1
  //     liftMotorValue = 1;
  //   } else if(controller.getRB()){ //If the RB Button is pressed, then the lift motor speed will be set to -1
  //     liftMotorValue = -1;
  //   } else if(!controller.getRB() && !controller.getLB()){  //If neither LB or RB button is pressed, then the lift motor speed will be set to 0
  //     liftMotorValue = 0;
  //   }
  //     liftMotor.set(liftMotorValue); //Sets the lift motor speed to the variable liftMotorValue
  // }
*/
/*
  private void IntakeNConveyer() {
    //if prox1 and prox 2 are broken, then the conveyer motor will stop
    if(!proximitySwitch1.get() && !proximitySwitch2.get()){  
      conveyerMotor.set(0);
      //if prox1 and prox 2 are true, then the conveyer motor power will be set to 0.5
    } else {
      conveyerMotor.set(0.5);
    }
    //if prox2 and prox3 are broken, then the intake motor will stop
    if(!proximitySwitch2.get() && !proximitySwitch3.get()){  
      intakeMotor.set(0);
      //if prox2 and prox3 are true, then the intake motor power will be set to 0.5
    } else {
      intakeMotor.set(0.5);  

    //If button 1 on the control panel is pressed, then the conveyer and intake motor power is set to 0.5
    if(buttonPanel.getRawButton(1)){  
      conveyerMotor.set(0.5);
      intakeMotor.set(0.5);
    } 
    //If button 2 on the control panel is pressed, then the conveyer and intake motor power is set to -0.5
    if(buttonPanel.getRawButton(2)){  
      conveyerMotor.set(-0.5);
      intakeMotor.set(-0.5);
    }

    SmartDashboard.putBoolean("Proximity Switch 1", proximitySwitch1.get());
    SmartDashboard.putBoolean("Proximity Switch 2", proximitySwitch2.get());
    SmartDashboard.putBoolean("Proximity Switch 3", proximitySwitch3.get());
    
    }
  }
*/
/*
//    private void colorSensorCode() {
//     Color detectedColor = m_colorSensor.getColor();

//     /**
//      * Run the color match algorithm on our detected color
//      */
/*
//     String colorString;
//     ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

//     if (match.color == kBlueTarget) {
//       colorString = "Blue";
//     } else if (match.color == kRedTarget) {
//       colorString = "Red";
//     } else if (match.color == kGreenTarget) {
//       colorString = "Green";
//     } else if (match.color == kYellowTarget) {
//       colorString = "Yellow";
//     } else {
//       colorString = "Unknown";
//     }

//     /**
//      * Open Smart Dashboard or Shuffleboard to see the color detected by the 
//      * sensor.
//      */
/*
//     SmartDashboard.putNumber("Red", detectedColor.red);
//     SmartDashboard.putNumber("Green", detectedColor.green);
//     SmartDashboard.putNumber("Blue", detectedColor.blue);
//     SmartDashboard.putNumber("Confidence", match.confidence);
//     SmartDashboard.putString("Detected Color", colorString);
//     //color sensor code - current color is stored in currentColor
//     detectedColor = m_colorSensor.getColor();
//     match = m_colorMatcher.matchClosestColor(detectedColor);
//     if (match.color == kBlueTarget) {
//       currentColor = "Blue";
//     } else if (match.color == kRedTarget) {
//       currentColor = "Red";
//     } else if (match.color == kGreenTarget) {
//       currentColor = "Green";
//     } else if (match.color == kYellowTarget) {
//       currentColor = "Yellow";
//     } else {
//       currentColor = "Unknown";

 
//       if (buttonPanel.getRawButton(5)){ // when the X button is clicked, it turns on the WoFMotor, then resets 
//         controlPanelMotor.set(1);
//         colorChanges = 0;
//         previousColor = "Unknown";
//         isSpinActive = true;
//       }
//       if (isSpinActive) { //it checks if the WoFMotor is still spinning due to the X button
//         if (!currentColor.equals(previousColor)){ // if the color changes, it increases the number of color changes by one
//           colorChanges++;
//           previousColor = currentColor;
//         }
//         if (colorChanges >= 32){ // if the color has changed more than 32 times, it will stop the motor
//           controlPanelMotor.set(0);
//           isSpinActive = false;
//         }
//       }
//     }
// }
// private void initColorMap(){
//   colorMap = new HashMap<String, Color>();
//   colorMap.put("B", kRedTarget);
//   colorMap.put("Y", kGreenTarget);
//   colorMap.put("R", kBlueTarget);
//   colorMap.put("G", kYellowTarget);
// }*/

@Override
  public void autonomousInit(){
    timer.reset();
    timer.start();
  }

  @Override
  public void disabledInit(){
    compressor.stop();
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