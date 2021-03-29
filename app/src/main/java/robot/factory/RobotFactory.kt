package robot.factory

import robot.enums.PartsAndPrices
import robot.interfaces.IRobotFactory
import robot.interfaces.IRobotPartFactory

class RobotFactory(private val partFactory: IRobotPartFactory) : IRobotFactory {

    override fun createRobot(): Robot {
        return Robot(
            this.partFactory.createPart(PartsAndPrices.HEAD),
            this.partFactory.createPart(PartsAndPrices.HAND),
            this.partFactory.createPart(PartsAndPrices.LEG)
        )
    }
}