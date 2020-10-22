package mr.fugugames.com.transformers

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersRepo
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewTransformersViewModelTests {
    private val repo: ViewTransformersRepo = mockk(relaxed = true)
    private lateinit var sut: ViewTransformersViewModel
    private val defaultScope = CoroutineScope(Dispatchers.Default)

    @BeforeEach
    fun setup() {
        clearAllMocks()
        sut = ViewTransformersViewModel(repo)
    }

    @Test
    fun `getTransformers calls local repo`(){
        defaultScope.launch {
            coEvery { repo.getLocalTransformers() } returns mutableListOf()
            sut.getTransformers()
            coVerify { repo.getLocalTransformers() }
        }
    }

    @Test
    fun `deleteTransformer calls local repo updateds _successMessage and robotList`(){
        defaultScope.launch {
            val testData = getTestData()
            val removeBot = testData.first()
            coEvery { repo.getLocalTransformers() } returns testData
            testData.remove(removeBot)
            coEvery { repo.deleteTransformer(removeBot.id) } returns testData
            sut.getTransformers()
            sut.deleteTransformer(removeBot)
            coVerify { repo.deleteTransformer(removeBot.id) }
            assertThat(sut.robotListLiveData.value).doesNotContain(removeBot)
            assertThat(sut.successMessage.value).isEqualTo("Successfully to Deleted Transformer: ${removeBot.name}")
        }
    }

    @Test
    fun `deleteTransformer shows error if repo returns null`(){
        defaultScope.launch {
            val testData = getTestData()
            val removeBot = testData.first()
            coEvery { repo.getLocalTransformers() } returns testData
            coEvery { repo.deleteTransformer(removeBot.id) } returns null
            sut.getTransformers()
            sut.deleteTransformer(removeBot)
            coVerify { repo.deleteTransformer(removeBot.id) }
            assertThat(sut.errorMessage.value).isEqualTo("Failed to Delete Transformer: ${removeBot.name} ")
        }
    }



    private fun getTestData(): MutableList<Transformers>{
        val data = mutableListOf<Transformers>()
        data.add(Transformers("auto1", name = "auto1", isAlive = true, strength = 10,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 10,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("auto2", name = "auto2", isAlive = true, strength = 5,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "A",team_icon = ""))
        data.add(Transformers("des1", name = "des1", isAlive = true, strength = 3,speed = 3,skill = 4,intelligence = 1,firepower = 6,rank = 2,endurance = 4,courage = 5,team = "D",team_icon = ""))
        return data
    }
}