package robot.interfaces

import robot.factory.Robot

interface IRobotFactory {
    fun createRobot(): Robot
}