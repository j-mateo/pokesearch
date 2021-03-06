package com.mateoj.pokesearch

import androidx.lifecycle.*
import com.mateoj.pokesearch.db.PokemonSearchSuggestionsDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val searchSuggestionsDb: PokemonSearchSuggestionsDB) : ViewModel() {
    private val _term = MutableLiveData<String>()
    private val _suggestions = MutableLiveData<Array<String>>()
    val suggestions: LiveData<Array<String>>
        get() = _suggestions

    fun setTerm(term: String) {
        _term.value = term

        if(term.trim().length < 2) {
            _suggestions.value = arrayOf()
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val suggestions = searchSuggestionsDb.pokemonDao().getSuggestions("$term%").map { it.name }.toTypedArray()
                _suggestions.postValue(suggestions)
            }
        }
    }
}