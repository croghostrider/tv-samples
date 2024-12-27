/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vinko.viptv.data.repositories

import com.vinko.viptv.data.entities.ThumbnailType
import com.vinko.viptv.data.entities.toMovie
import com.vinko.viptv.data.util.AssetsReader
import com.vinko.viptv.data.util.StringConstants
import javax.inject.Inject

class TvDataSource @Inject constructor(
    assetsReader: AssetsReader
) {
    private val mostPopularTvShowsReader = CachedDataReader {
        readMovieData(assetsReader, StringConstants.Assets.MostPopularTVShows)
    }

    suspend fun getTvShowList() = mostPopularTvShowsReader.read().subList(0, 5).map {
        it.toMovie(ThumbnailType.Long)
    }

    suspend fun getBingeWatchDramaList() = mostPopularTvShowsReader.read().subList(6, 15).map {
        it.toMovie()
    }
}
