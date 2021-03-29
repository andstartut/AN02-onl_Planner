package robot.parts

import robot.enums.BrandsAndMarkupIndex
import robot.enums.PartsAndPrices
import robot.interfaces.IRobotPart
import robot.util.RobotPartUtil

class RobotPart(private val part: PartsAndPrices, private val brand: BrandsAndMarkupIndex) :
    IRobotPart {

    override fun info(): String = RobotPartUtil.partOperation(getPartName(), getBrandName())

    override fun getPrice(): Int = (part.price * brand.markupIndex).toInt()

    override fun getPartName(): String = RobotPartUtil.convertingConstantToName(part.name)

    override fun getBrandName(): String = RobotPartUtil.convertingConstantToName(brand.name)
}