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
  
  private DifferentialDrive myRobot; //Creates the object for the drivetrain
  private Joystick controller; //Creates the object for the contoller

  private WPI_VictorSPX intakeMotor; //Creates the object for the intake motor
  private JoystickButton aButton; //Creates a container to receive a boolean from the A Button
  private JoystickButton bButton; //Creates a container to receive a boolean from the B Button
  private int intakeMotorValue; //Creating a variable for the speed of the intake motor controller

  private WPI_VictorSPX conveyerMotor; //creates the object for the conveyer
  private JoystickButton buttonX; //Creates a container to receive a boolean from the X Button
  private JoystickButton buttonY; //Creates a container to receive a boolean from the Y Button
  private int conveyerMotorValue;

  private SpeedControllerGroup winchMotors;

  @Override
  public void robotInit() {
    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */

    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4));
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3));

    myRobot = new DifferentialDrive(leftMotors, rightMotors);
    //Creates the controller on USB 0
    controller = new Joystick(0);
    
    intakeMotor = new WPI_VictorSPX(5); //Assigns intake motor as CAN bus 5
    aButton = new JoystickButton(controller,1); //Assigns the aButton variable to the A button on Controller 0
    bButton = new JoystickButton(controller,2); //Assigns the bButton variable to the B button on Controller 0

    // Assigns button X and Y to button 3 and 4 on the controller
    buttonX = new JoystickButton(controller, 3);
    buttonY = new JoystickButton(controller, 4);
    conveyerMotor = new WPI_VictorSPX(6); //Assigns conveyer motor to CAN 6

    winchMotors = new SpeedControllerGroup(new WPI_VictorSPX(9), new WPI_VictorSPX(10));
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    myRobot.tankDrive((controller.getRawAxis(1)*-1), (controller.getRawAxis(5)*-1));

    
    //Converts a boolean to a 1 or 0 based on the A button state
    if(aButton.get() == true) 
      intakeMotorValue = 1;   
    // Converts a boolean to a -1 or 0 based on the B button state
    if(bButton.get() == true) 
      intakeMotorValue = (-1);   //sets the motor to run in reverse when B is pressed
    //The motor power will be sat to 0 when both A and B are not pressed
    else if(aButton.get() == false && bButton.get() == false)
      intakeMotorValue = 0;
    //Sets the intake motor speed to the variable intakeMotorValue
    intakeMotor.set(intakeMotorValue);


    //Sets the motor value based on the value of buttonX and buttonY
    if(buttonX.get() == true) 
      conveyerMotorValue = 1;
    if(buttonY.get() == true)
      conveyerMotorValue = (-1);
    else if(buttonX.get() == false && buttonY.get() == false)
      conveyerMotorValue = 0;

    conveyerMotor.set(conveyerMotorValue);
  }
}
