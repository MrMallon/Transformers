package mr.fugugames.com.transformers.war

import mr.fugugames.com.transformers.models.BattleResult
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersRepo

class Battle{
    private val autoBots: MutableList<Transformers> = mutableListOf()
    private val decepticons: MutableList<Transformers> = mutableListOf()

    fun startBattle(transformers: MutableList<Transformers>): BattleResult {
        autoBots.addAll(transformers.filter { it.team == "A"})
        decepticons.addAll(transformers.filter { it.team == "D"})

        if(!decepticons.any())
            return BattleResult("autoBots", transformers)
        if(!autoBots.any())
            return BattleResult("decepticons", transformers)

        decepticons.sortBy { it.rank }
        autoBots.sortBy { it.rank }

        return startFight()
    }

    private fun startFight() : BattleResult{

        for (decepticon in decepticons){
            for (autoBot in autoBots){
                if(autoBot.isAlive && !autoBot.hasRunAway) {
                    val dead = fight(decepticon, autoBot)
                    if (dead == "decepticon" || dead == "Both Dead")
                        break
                    else
                        continue
                }
            }
        }
        val winners = winningTeam()
        val transformers = mutableListOf<Transformers>()
        transformers.addAll(autoBots)
        transformers.addAll(decepticons)
        autoBots.clear()
        decepticons.clear()
        return BattleResult(winners, transformers)
    }

    private fun fight(decepticon: Transformers, autoBot: Transformers):String{
        val runAway = runAwayCheck(decepticon, autoBot)
        if(runAway != "NA")
            return runAway

        val deadRobot = checkSkillDiff(decepticon,autoBot)
        if(deadRobot != "NA")
            return deadRobot

        return checkDeath(decepticon,autoBot)
    }

    private fun runAwayCheck(decepticon: Transformers, autoBot: Transformers): String{
        val courage = decepticon.courage - autoBot.courage
        val strength = decepticon.strength - autoBot.strength
        if(courage >= 4 && strength >= 3) {
            autoBot.hasRunAway = true
            return "autoBot"
        }
        else if(courage <= -4 && strength <= -3) {
            decepticon.hasRunAway = true
            return "decepticon"
        }
        return "NA"
    }

    private fun checkSkillDiff(decepticon: Transformers, autoBot: Transformers): String{
        val skillDiff = decepticon.skill - autoBot.skill
        if(skillDiff >= 3){
            autoBot.isAlive = false
            return "autoBot"
        }
        else if (skillDiff <= -3){
            decepticon.isAlive = false
            return "decepticon"
        }
        return "NA"
    }

    private fun checkDeath(decepticon: Transformers, autoBot: Transformers): String{
        val autoTotal = overAllScore(autoBot)
        val decTotal = overAllScore(decepticon)
        if(autoTotal < decTotal){
            autoBot.isAlive = false
            return "autoBot"
        }
        if(autoTotal > decTotal){
            decepticon.isAlive = false
            return "decepticon"}
        return "Both Dead"
    }

    private fun overAllScore(transformer: Transformers):Int {
        return transformer.intelligence + transformer.strength + transformer.speed + transformer.endurance + transformer.courage + transformer.rank + transformer.skill + transformer.firepower
    }

    private fun winningTeam():String{
        val autoBotCount = autoBots.count { it.isAlive && !it.hasRunAway }
        val decepticonCount = decepticons.count { it.isAlive && !it.hasRunAway }
        return if(autoBotCount >decepticonCount)
            "AutoBots"
        else
            "Decepticons"
    }
}