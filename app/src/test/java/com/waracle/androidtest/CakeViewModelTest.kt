package com.waracle.androidtest

import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class CakeViewModelTest {

    private val cakeLoaderTask = CakeViewModel().CakeLoaderTask()

    private val jsonResponse = "[{\"title\":\"Lemon cheesecake\",\"desc\":\"A cheesecake made of lemon\",\"image\":\"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"}," +
            "{\"title\":\"victoria sponge\",\"desc\":\"sponge with jam\",\"image\":\"http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg\"}]"

    @Test
    fun `the JSON response is converted correctly`() {
        val jsonArray = JSONArray(jsonResponse)
        val listOfCakes = cakeLoaderTask.convertJSONResponse(jsonArray)

        assertEquals(listOfCakes.size, 2)

        assertEquals(listOfCakes[0].title, "Lemon cheesecake")
        assertEquals(listOfCakes[0].description, "A cheesecake made of lemon")
        assertEquals(listOfCakes[0].imageUrl, "https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")

        assertEquals(listOfCakes[1].title, "victoria sponge")
        assertEquals(listOfCakes[1].description, "sponge with jam")
        assertEquals(listOfCakes[1].imageUrl, "http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg")
    }
}