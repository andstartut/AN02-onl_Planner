package robot.factory

import robot.enums.BrandsAndMarkupIndex
import robot.enums.PartsAndPrices
import robot.interfaces.IRobotPart
import robot.interfaces.IRobotPartFactory
import robot.parts.RobotPart
import java.util.*

class RandomRobotPartFactory : IRobotPartFactory {
    private val ENUM_LENGTH = BrandsAndMarkupIndex.values().size
    private val random: Random = Random()

    override fun createPart(partName: PartsAndPrices): IRobotPart {
        return RobotPart(partName, selectBrand())
    }

    private fun selectBrand(): BrandsAndMarkupIndex {
        return BrandsAndMarkupIndex.values()[random.nextInt(ENUM_LENGTH)]
    }
}