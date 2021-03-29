package robot

import robot.exceptions.CreateRobotPartException
import robot.factory.RandomRobotPartFactory
import robot.factory.Robot
import robot.factory.RobotFactory

fun main() {
    val partFactory = RandomRobotPartFactory()

    val robotFactory = RobotFactory(partFactory)

    try {
        val r1: Robot = robotFactory.createRobot()
        val r2: Robot = robotFactory.createRobot()
        val r3: Robot = robotFactory.createRobot()

        val robots = arrayOf(r1, r2, r3)
        for (robot in robots) {
            robot.action()
        }
        RobotPrice(robots).findTheMostExpensive()

    } catch (e: CreateRobotPartException) {
        println(e)
    }
}