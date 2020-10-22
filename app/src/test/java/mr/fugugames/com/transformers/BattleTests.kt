package mr.fugugames.com.transformers

import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.war.Battle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BattleTests {
    private lateinit var sut: Battle

    @BeforeEach
    fun setup(){
        sut = Battle()
    }

    @Test
    fun `test AutoBots win battle and Decepticon is dead`(){
        val result = sut.startBattle(getAutoBotsWinTestData())
        assertThat(result.winner).isEqualTo("AutoBots")
        val decepticon = result.bots.filter { it.team == "D" }
        assertThat(decepticon.all { !it.isAlive }).isTrue()
    }

    @Test
    fun `test skill base victory`(){
        val result = sut.startBattle(getSkillBasedTestData())
        assertThat(result.winner).isEqualTo("Decepticons")
        val autoBots = result.bots.filter { it.team == "A" }
        assertThat(autoBots.all { !it.isAlive }).isTrue()
    }

    @Test
    fun `test runaways are still alive`(){
        val result = sut.startBattle(getRunawayBasedTestData())
        assertThat(result.winner).isEqualTo("Decepticons")
        val autoBots = result.bots.filter { it.team == "A" }
        assertThat(autoBots.all { it.isAlive }).isTrue()
        assertThat(autoBots.all { it.hasRunAway }).isTrue()
    }

    private fun getAutoBotsWinTestData(): MutableList<Transformers>{
      val data = mutableListOf<Transformers>()
        data.add(Transformers("auto1", name = "auto1", isAlive = true, strength = 10,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 10,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("auto2", name = "auto2", isAlive = true, strength = 5,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("des1", name = "des1", isAlive = true, strength = 3,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "D",team_icon = ""))
        return data
    }

    private fun getSkillBasedTestData(): MutableList<Transformers>{
        val data = mutableListOf<Transformers>()
        data.add(Transformers("auto1", name = "auto1", isAlive = true, strength = 10,speed = 3,skill = 1,intelligence = 1,firepower = 6,rank = 10,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("auto2", name = "auto2", isAlive = true, strength = 5,speed = 3,skill = 7,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("des1", name = "des1", isAlive = true, strength = 3,speed = 3,skill = 10,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "D",team_icon = ""))
        return data
    }

    private fun getRunawayBasedTestData(): MutableList<Transformers>{
        val data = mutableListOf<Transformers>()
        data.add(Transformers("auto1", name = "auto1", isAlive = true, strength = 3,speed = 3,skill = 1,intelligence = 1,firepower = 6,rank = 10,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("des1", name = "des1", isAlive = true, strength = 10,speed = 3,skill = 7,intelligence = 1,firepower = 6,rank = 2,endurance = 10,courage = 10,team = "D",team_icon = ""))
        return data
    }
}