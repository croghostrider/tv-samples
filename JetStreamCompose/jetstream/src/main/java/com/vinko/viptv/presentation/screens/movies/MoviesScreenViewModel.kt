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

package com.vinko.viptv.presentation.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinko.viptv.data.entities.MovieList
import com.vinko.viptv.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MoviesScreenViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState = combine(
        movieRepository.getMoviesWithLongThumbnail(),
        movieRepository.getPopularFilmsThisWeek(),
    ) { (movieList, popularFilmsThisWeek) ->
        MoviesScreenUiState.Ready(
            movieList = movieList,
            popularFilmsThisWeek = popularFilmsThisWeek
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoviesScreenUiState.Loading
    )
}

sealed interface MoviesScreenUiState {
    data object Loading : MoviesScreenUiState
    data class Ready(
        val movieList: MovieList,
        val popularFilmsThisWeek: MovieList
    ) : MoviesScreenUiState
}
