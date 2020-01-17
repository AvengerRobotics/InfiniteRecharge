/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; //The robot

import edu.wpi.first.wpilibj.Joystick; //The controller
import edu.wpi.first.wpilibj.buttons.JoystickButton; //The A B Y and X buttons
import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //The VictorSPX motor controllers
import edu.wpi.first.wpilibj.TimedRobot; //The class that a user program is based on -- not much other info is given
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //used for driving differential drive/skid-steer drive platforms


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
  private DifferentialDrive myRobot; //Creates the object for the drivetrain
  private Joystick controller; //Creates the object for the contoller

  //private WPI_VictorSPX intakeMotor; //Creates the object for the intake motor
  private JoystickButton buttonA; //Creates a container to receive a boolean from the A Button
  private JoystickButton buttonB; //Creates a container to receive a boolean from the B Button
  private int intakeMotorsValue; //Creating a variable for the speed of the intake motor controller
  private int liftMotorValue; //Creating a variable for the speed of the lift motor controller
  private JoystickButton buttonX; //Creates a container to receive a boolean from the X Button
  private JoystickButton buttonY; //Creates a container to receive a boolean from the Y Button
  private JoystickButton buttonL; //Creates a container to receive a boolean form the LB button
  private JoystickButton buttonR; //Creates a container to receive a boolean form the RB button
  //private WPI_VictorSPX conveyerMotor; //creates the object for the conveyer
  //private int conveyerMotorValue;


  @Override
  public void robotInit() {
    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */
    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4));
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3));
    intakeMotors = new SpeedControllerGroup(new WPI_VictorSPX(5), new WPI_VictorSPX(6));
    winchMotors = new SpeedControllerGroup(new WPI_VictorSPX(9), new WPI_VictorSPX(10));
    liftMotor = new WPI_VictorSPX(7);
    WoFMotor = new WPI_VictorSPX(8);
    // intakeMotor = new WPI_VictorSPX(5); //Assigns intake motor as CAN bus 5
    // conveyerMotor = new WPI_VictorSPX(6); //Assigns conveyer motor to CAN 6


    myRobot = new DifferentialDrive(leftMotors, rightMotors);
    // Creates the controller on USB 0
    controller = new Joystick(0);
    
    buttonA = new JoystickButton(controller,1); //Assigns the buttonA variable to the A button on Controller 0
    buttonB = new JoystickButton(controller,2); //Assigns the buttonB variable to the B button on Controller 0
    // Assigns button X and Y to button 3 and 4 on the controller
    buttonX = new JoystickButton(controller, 3); //
    buttonY = new JoystickButton(controller, 4);
    buttonL = new JoystickButton(controller,5); //left bumper of the controller
    buttonR = new JoystickButton(controller,6); //right bumper of the controller
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    myRobot.tankDrive((controller.getRawAxis(1)*-1), (controller.getRawAxis(5)*-1));
    controller.getPOV();
    if(controller.getPOV() > 45 && controller.getPOV() < 135 == true){ 
      WoFMotor.set(1);
    //If the A button is pressed, then the conveyor and intake motors speed will be set to -1
    } else if(controller.getPOV() > 225 && controller.getPOV() < 315 == true){ 
      WoFMotor.set(-1);
      //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
    } else{
      WoFMotor.set(0);
    }
      //Sets the intake motors speed to the variable intakeMotorsValue
    intakeMotors.set(intakeMotorsValue);

    if (buttonB.get()){
      winchMotors.set(-1*controller.getRawAxis(3));
    } else {
      winchMotors.set(controller.getRawAxis(3));
    }

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
    //Sets the intake motors speed to the variable intakeMotorsValue
    intakeMotors.set(intakeMotorsValue);
   
    if(buttonL.get() == true){  //If the LB Button is pressed, then the lift motor speed will be set to 1
      liftMotorValue = 1;
    } else if(buttonR.get() == true){ //If the RB Button is pressed, then the lift motor speed will be set to -1
      liftMotorValue = -1;
    } else if(buttonL.get() == false && buttonR.get() == false){  //If neither LB or RB button is pressed, then the lift motor speed will be set to 0
      liftMotorValue = 0;
    }
    liftMotor.set(liftMotorValue); //Sets the lift motor speed to the variable liftMotorValue
  }
}