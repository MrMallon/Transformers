package mr.fugugames.com.transformers

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mr.fugugames.com.transformers.createTransformers.CreateTransformerRepo
import mr.fugugames.com.transformers.createTransformers.CreateTransformerViewModel
import mr.fugugames.com.transformers.models.Transformers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateTransformerViewModelTests {
    private val repo: CreateTransformerRepo = mockk(relaxed = true)
    private lateinit var sut: CreateTransformerViewModel
    private val defaultScope = CoroutineScope(Dispatchers.Default)

    @BeforeEach
    fun setup() {
        clearAllMocks()
        sut = CreateTransformerViewModel(repo)
    }

    @Test
    fun `getLocalTransformer calls db for data`() {
        defaultScope.launch {
            val testData = mockUserInput()
            assertThat(sut.name).isEqualTo(testData.name)
            assertThat(sut.strength).isEqualTo(testData.strength)
            assertThat(sut.intelligence).isEqualTo(testData.intelligence)
            assertThat(sut.speed).isEqualTo(testData.speed)
            assertThat(sut.endurance).isEqualTo(testData.endurance)
            assertThat(sut.rank).isEqualTo(testData.rank)
            assertThat(sut.courage).isEqualTo(testData.courage)
            assertThat(sut.firepower).isEqualTo(testData.firepower)
            assertThat(sut.skill).isEqualTo(testData.skill)
            assertThat(sut.team.value).isEqualTo(testData.team)
            assertThat(sut.teamIcon).isEqualTo(testData.team_icon)
        }
    }

    @Test
    fun `createTransformer calls repo and updated _successMessage`(){
        defaultScope.launch {
            val testData = mockUserInput()
            sut.createTransformer()
            coVerify{repo.createTransformer(testData)}
            assertThat(sut.successMessage.value).isEqualTo("Successfully to create Transformer: ${testData.name}")
        }
    }

    @Test
    fun `createTransformer shows error if no name entered`(){
        defaultScope.launch {
            sut.createTransformer()
            assertThat(sut.errorMessage.value).isEqualTo("A Name is required")
        }
    }

    @Test
    fun `createTransformer shows error if no team selected`(){
        defaultScope.launch {
            mockUserInput("John Snow", team = "")
            sut.createTransformer()
            assertThat(sut.errorMessage.value).isEqualTo("A Team is required")
        }
    }

    @Test
    fun `createTransformer calls update if isEditTransformer`(){
        defaultScope.launch {
            sut.isEditTransformer = true
            val data = mockUserInput("Jack Black", team = "A")
            sut.createTransformer()
            coVerify { repo.updateTransformer(data) }
        }
    }

    @Test
    fun `createTransformer calls create if isEditTransformer is false`(){
        defaultScope.launch {
            sut.isEditTransformer = false
            val data = mockUserInput("Frank Underwood", team = "A")
            sut.createTransformer()
            coVerify { repo.createTransformer(data) }
        }
    }

    private fun mockUserInput(name:String = "TestName", team: String = "A"):Transformers{
        val testData = Transformers(
            "sjdfn",
            name = name,
            isAlive = true,
            strength = 10,
            speed = 3,
            skill = 1,
            intelligence = 1,
            firepower = 6,
            rank = 10,
            endurance = 4,
            courage = 5,
            team = team,
            team_icon = ""
        )
        coEvery { repo.getLocalTransformer(testData.id) } returns testData
        sut.getLocalTransformer(testData.id)

        return testData
    }
}