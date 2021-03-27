package robot.interfaces

import robot.enums.PartsAndPrices

interface IRobotPartFactory {
    fun createPart(partName: PartsAndPrices): IRobotPart
}