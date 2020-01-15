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
import edu.wpi.first.wpilibj.TimedRobot; //Need to look up
import edu.wpi.first.wpilibj.drive.DifferentialDrive; //Unsure, may be drivetrain with motors running in different directions


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot { 
  private SpeedControllerGroup leftMotors; //Creates the object for the left motors
  private SpeedControllerGroup rightMotors; //Creates the object for the right motors
  
  private DifferentialDrive m_myRobot; //Creates the object for the drivetrain
  private Joystick m_leftStick; //Creates the object for the left joystick
  private Joystick m_rightStick; //Creates the object for the right joystick

  private WPI_VictorSPX intakeMotor; //Creates the object for the intake motor
  private JoystickButton aButton; //Creates a container to receive a boolean from the A Button
  private int intakeMotorValue; //Creating a variable for the speed of the intake motor controller

  @Override
  public void robotInit() {
    /*
    Assigns the motor controllers to speed controller groups
    Argument(value in parenthises) is the CAN bus address
    */

    leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(4));
    rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(2), new WPI_VictorSPX(3));

    m_myRobot = new DifferentialDrive(leftMotors, rightMotors);
    //Creates the joysticks on controller 0
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(0);
    
    intakeMotor = new WPI_VictorSPX(5); //Assigns intake motor as CAN bus 5
    aButton = new JoystickButton(new Joystick(0),1); //Assigns the aButton variable to the A button on Controller 0
    
  }

  @Override
  public void teleopPeriodic() {
    //Sets tankDrive to the inverse of the values from the joysticks, leftStick value is 1 and rightStick value is 5
    m_myRobot.tankDrive((m_leftStick.getRawAxis(1)*-1), (m_rightStick.getRawAxis(5)*-1));

    //Converts a boolean to a 1 or 0 based on the A button state
    if(aButton.get() == true) 
        intakeMotorValue = 1;   
    else if(aButton.get() == false)
        intakeMotorValue = 0;
    //Sets the intake motor speed to the variable intakeMotorValue
    intakeMotor.set(intakeMotorValue); 
  }
}
